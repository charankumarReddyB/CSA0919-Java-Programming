import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object - the ONLY class that talks JDBC.
 *
 * Demonstrates:
 *  - Connection            (DatabaseConnection.getConnection())
 *  - PreparedStatement     (every query below is parameterised, no string
 *                           concatenation, which also avoids SQL injection)
 *  - ResultSet             (reading rows back for SELECT queries)
 *  - executeUpdate()       (INSERT / UPDATE / DELETE)
 *  - executeQuery()        (SELECT)
 *  - try-catch-finally     (insertMember below is written with an explicit
 *                           finally block that closes the connection;
 *                           the remaining methods use try-with-resources,
 *                           which is the modern equivalent of try-finally
 *                           for closing JDBC resources)
 */
public class LibraryDAO {

    // ---------------------------------------------------------------
    // CREATE (INSERT)
    // ---------------------------------------------------------------

    /**
     * Inserts a member row. Written with an explicit try-catch-finally
     * (instead of try-with-resources) to show the pattern literally.
     */
    public boolean insertMember(Member member) {
        String sql = "INSERT INTO members (member_id, name, contact_number, member_type, extra_info) "
                + "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, member.getMemberId());
            ps.setString(2, member.getName());
            ps.setString(3, member.getContactNumber());
            ps.setString(4, member.getMemberType());

            String extra;
            if (member instanceof Student s) {
                extra = "Roll:" + s.getRollNumber() + " Dept:" + s.getDepartment();
            } else if (member instanceof Faculty f) {
                extra = "Designation:" + f.getDesignation() + " Dept:" + f.getDepartment();
            } else {
                extra = "";
            }
            ps.setString(5, extra);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("insertMember failed: " + e.getMessage());
            return false;
        } finally {
            // finally block guarantees the resources are released even if
            // an exception was thrown above.
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                System.err.println("Error closing resources: " + closeEx.getMessage());
            }
        }
    }

    public boolean insertBook(Book book) {
        String sql = "INSERT INTO books (book_id, title, author, isbn, total_copies, available_copies) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getBookId());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getIsbn());
            ps.setInt(5, book.getTotalCopies());
            ps.setInt(6, book.getAvailableCopies());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("insertBook failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserts an issue record and returns the auto-generated issue_id,
     * or -1 on failure.
     */
    public int insertIssueRecord(IssueRecord record) {
        String sql = "INSERT INTO issue_records (member_id, book_id, issue_date, due_date, return_date, fine_amount, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, record.getMemberId());
            ps.setString(2, record.getBookId());
            ps.setDate(3, Date.valueOf(record.getIssueDate()));
            ps.setDate(4, Date.valueOf(record.getDueDate()));
            ps.setDate(5, record.getReturnDate() == null ? null : Date.valueOf(record.getReturnDate()));
            ps.setDouble(6, record.getFineAmount());
            ps.setString(7, record.getStatus());

            int rows = ps.executeUpdate();
            if (rows == 0) return -1;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e) {
            System.err.println("insertIssueRecord failed: " + e.getMessage());
            return -1;
        }
    }

    // ---------------------------------------------------------------
    // READ (SELECT)
    // ---------------------------------------------------------------

    public List<IssueRecord> getOverdueRecords() {
        String sql = "SELECT issue_id, member_id, book_id, issue_date, due_date, return_date, fine_amount, status "
                + "FROM issue_records WHERE status = 'ISSUED' AND due_date < CURDATE()";
        List<IssueRecord> overdue = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                overdue.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("getOverdueRecords failed: " + e.getMessage());
        }
        return overdue;
    }

    public List<Book> getAllBooks() {
        String sql = "SELECT book_id, title, author, isbn, total_copies, available_copies FROM books";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Book b = new Book(rs.getString("book_id"), rs.getString("title"),
                        rs.getString("author"), rs.getString("isbn"), rs.getInt("total_copies"));
                b.setAvailableCopies(rs.getInt("available_copies"));
                books.add(b);
            }
        } catch (SQLException e) {
            System.err.println("getAllBooks failed: " + e.getMessage());
        }
        return books;
    }

    private IssueRecord mapRow(ResultSet rs) throws SQLException {
        IssueRecord record = new IssueRecord(
                rs.getString("member_id"),
                rs.getString("book_id"),
                rs.getDate("issue_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate());
        record.setIssueId(rs.getInt("issue_id"));
        Date ret = rs.getDate("return_date");
        record.setReturnDate(ret == null ? null : ret.toLocalDate());
        record.setFineAmount(rs.getDouble("fine_amount"));
        record.setStatus(rs.getString("status"));
        return record;
    }

    // ---------------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------------

    public boolean updateBookAvailability(String bookId, int availableCopies) {
        String sql = "UPDATE books SET available_copies = ? WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, availableCopies);
            ps.setString(2, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("updateBookAvailability failed: " + e.getMessage());
            return false;
        }
    }

    /** Called when a book is returned: sets return_date, fine_amount and status = RETURNED. */
    public boolean updateIssueRecordOnReturn(int issueId, LocalDate returnDate, double fineAmount) {
        String sql = "UPDATE issue_records SET return_date = ?, fine_amount = ?, status = ? WHERE issue_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(returnDate));
            ps.setDouble(2, fineAmount);
            ps.setString(3, IssueRecord.STATUS_RETURNED);
            ps.setInt(4, issueId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("updateIssueRecordOnReturn failed: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------

    public boolean deleteBook(String bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("deleteBook failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMember(String memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("deleteMember failed: " + e.getMessage());
            return false;
        }
    }
}
