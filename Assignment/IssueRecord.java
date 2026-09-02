import java.time.LocalDate;

/**
 * Represents a single "book issued to a member" transaction.
 * Used both for the in-memory issue log (Library.java) and for
 * mapping rows of the issue_records database table (LibraryDAO.java).
 */
public class IssueRecord {

    public static final String STATUS_ISSUED = "ISSUED";
    public static final String STATUS_RETURNED = "RETURNED";

    private int issueId;          // primary key once stored in DB (0 until assigned)
    private final String memberId;
    private final String bookId;
    private final LocalDate issueDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;  // null while the book is still out
    private double fineAmount;
    private String status;

    public IssueRecord(String memberId, String bookId, LocalDate issueDate, LocalDate dueDate) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.fineAmount = 0.0;
        this.status = STATUS_ISSUED;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getBookId() {
        return bookId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOverdue(LocalDate today) {
        return STATUS_ISSUED.equals(status) && today.isAfter(dueDate);
    }

    @Override
    public String toString() {
        return String.format("Issue#%d [Member=%s, Book=%s, Issued=%s, Due=%s, Returned=%s, Fine=Rs.%.2f, Status=%s]",
                issueId, memberId, bookId, issueDate, dueDate,
                returnDate == null ? "-" : returnDate, fineAmount, status);
    }
}
