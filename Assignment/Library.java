import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Core business-logic layer. Holds the in-memory catalogue and issue
 * log, and forwards every change to the database through LibraryDAO so
 * the two stay in sync.
 *
 * COLLECTIONS / GENERICS / ITERATOR requirement:
 *  - catalogue is a Map<String, Book>            (HashMap -> Collection Framework)
 *  - members   is a Map<String, Member>           (HashMap -> Collection Framework)
 *  - issueRecords is a List<IssueRecord>           (ArrayList -> Collection Framework)
 *  - searchByTitle()/searchByAuthor()/getOverdueRecords() walk their
 *    collection using an explicit Iterator<T> (generic iterator) rather
 *    than a for-each loop, to demonstrate Iterator usage directly.
 *
 * EXCEPTION HANDLING requirement:
 *  - issueBook() throws the user-defined BorrowingLimitExceededException
 *  - issueBook() also throws/uses built-in exceptions:
 *      NoSuchElementException  -> book or member not found
 *      IllegalStateException   -> book exists but has 0 copies available
 *        ("attempting to issue an unavailable book")
 *  - returnBook() demonstrates try-catch-finally around the DB update.
 */
public class Library {

    private final Map<String, Book> catalogue = new HashMap<>();
    private final Map<String, Member> members = new HashMap<>();
    private final List<IssueRecord> issueRecords = new ArrayList<>();
    private final LibraryDAO dao = new LibraryDAO();

    // ---------------------------------------------------------------
    // Member / Book management
    // ---------------------------------------------------------------

    public void addMember(Member member) {
        members.put(member.getMemberId(), member);
        dao.insertMember(member);
    }

    public void addBook(Book book) {
        catalogue.put(book.getBookId(), book);
        dao.insertBook(book);
    }

    public Member getMember(String memberId) {
        return members.get(memberId);
    }

    public Book getBook(String bookId) {
        return catalogue.get(bookId);
    }

    public Collection<Book> getAllBooks() {
        return catalogue.values();
    }

    public Collection<Member> getAllMembers() {
        return members.values();
    }

    public List<IssueRecord> getAllIssueRecords() {
        return issueRecords;
    }

    // ---------------------------------------------------------------
    // Catalogue search - Iterator + Generics demonstration
    // ---------------------------------------------------------------

    public List<Book> searchByTitle(String keyword) {
        List<Book> results = new ArrayList<>();
        Iterator<Book> it = catalogue.values().iterator(); // generic Iterator<Book>
        String needle = keyword.toLowerCase();
        while (it.hasNext()) {
            Book b = it.next();
            if (b.getTitle().toLowerCase().contains(needle)) {
                results.add(b);
            }
        }
        return results;
    }

    public List<Book> searchByAuthor(String keyword) {
        List<Book> results = new ArrayList<>();
        Iterator<Book> it = catalogue.values().iterator(); // generic Iterator<Book>
        String needle = keyword.toLowerCase();
        while (it.hasNext()) {
            Book b = it.next();
            if (b.getAuthor().toLowerCase().contains(needle)) {
                results.add(b);
            }
        }
        return results;
    }

    /** Counts how many books a member currently has out, using an Iterator. */
    private int countCurrentlyIssued(String memberId) {
        int count = 0;
        Iterator<IssueRecord> it = issueRecords.iterator();
        while (it.hasNext()) {
            IssueRecord r = it.next();
            if (r.getMemberId().equals(memberId) && IssueRecord.STATUS_ISSUED.equals(r.getStatus())) {
                count++;
            }
        }
        return count;
    }

    // ---------------------------------------------------------------
    // Issue / Return workflow
    // ---------------------------------------------------------------

    /**
     * Issues a book to a member.
     *
     * @throws NoSuchElementException          (built-in) if the member or book id is unknown
     * @throws IllegalStateException            (built-in) if the book has no available copies
     * @throws BorrowingLimitExceededException  (user-defined) if the member is already at their limit
     */
    public IssueRecord issueBook(String memberId, String bookId)
            throws BorrowingLimitExceededException {

        Member member = members.get(memberId);
        if (member == null) {
            throw new NoSuchElementException("No member found with ID: " + memberId);
        }

        Book book = catalogue.get(bookId);
        if (book == null) {
            throw new NoSuchElementException("No book found with ID: " + bookId);
        }

        if (!book.isAvailable()) {
            // Built-in exception used for "attempting to issue an unavailable book"
            throw new IllegalStateException("Book '" + book.getTitle() + "' has no available copies right now.");
        }

        int alreadyIssued = countCurrentlyIssued(memberId);
        if (alreadyIssued >= member.getMaxBorrowLimit()) {
            throw new BorrowingLimitExceededException(
                    member.getName() + " (" + member.getMemberType() + ") has reached the borrowing limit of "
                            + member.getMaxBorrowLimit() + " book(s).");
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(14); // 2-week loan period
        IssueRecord record = new IssueRecord(memberId, bookId, issueDate, dueDate);

        book.decrementAvailable();
        int generatedId = dao.insertIssueRecord(record);
        record.setIssueId(generatedId > 0 ? generatedId : (issueRecords.size() + 1));
        issueRecords.add(record);
        dao.updateBookAvailability(bookId, book.getAvailableCopies());

        return record;
    }

    /**
     * Returns a book, calculates any overdue fine and updates both the
     * in-memory log and the database.
     *
     * Demonstrates try-catch-finally explicitly: the DB update is
     * attempted in try, SQL-related problems are caught, and the
     * finally block always logs that the return attempt finished,
     * regardless of outcome.
     */
    public double returnBook(int issueId) {
        IssueRecord found = null;
        Iterator<IssueRecord> it = issueRecords.iterator();
        while (it.hasNext()) {
            IssueRecord r = it.next();
            if (r.getIssueId() == issueId && IssueRecord.STATUS_ISSUED.equals(r.getStatus())) {
                found = r;
                break;
            }
        }

        if (found == null) {
            throw new NoSuchElementException("No active issue record found with ID: " + issueId);
        }

        double fine;
        try {
            LocalDate today = LocalDate.now();
            long overdueDays = Math.max(0, today.toEpochDay() - found.getDueDate().toEpochDay());
            Member member = members.get(found.getMemberId());
            fine = (member != null) ? member.calculateFine(overdueDays) : 0.0;

            found.setReturnDate(today);
            found.setFineAmount(fine);
            found.setStatus(IssueRecord.STATUS_RETURNED);

            Book book = catalogue.get(found.getBookId());
            if (book != null) {
                book.incrementAvailable();
                dao.updateBookAvailability(book.getBookId(), book.getAvailableCopies());
            }
            dao.updateIssueRecordOnReturn(found.getIssueId(), today, fine);
        } catch (RuntimeException e) {
            // Any unexpected runtime issue during the return process
            System.err.println("Error while returning book: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("Return attempt processed for issue #" + issueId);
        }

        return fine;
    }

    /** Overdue issue records, found using an explicit Iterator. */
    public List<IssueRecord> getOverdueRecords() {
        List<IssueRecord> overdue = new ArrayList<>();
        LocalDate today = LocalDate.now();
        Iterator<IssueRecord> it = issueRecords.iterator();
        while (it.hasNext()) {
            IssueRecord r = it.next();
            if (r.isOverdue(today)) {
                overdue.add(r);
            }
        }
        return overdue;
    }

    public boolean deleteBook(String bookId) {
        catalogue.remove(bookId);
        return dao.deleteBook(bookId);
    }

    public boolean updateBookCopies(String bookId, int newAvailableCopies) {
        Book book = catalogue.get(bookId);
        if (book == null) {
            throw new NoSuchElementException("No book found with ID: " + bookId);
        }
        book.setAvailableCopies(newAvailableCopies);
        return dao.updateBookAvailability(bookId, newAvailableCopies);
    }
}
