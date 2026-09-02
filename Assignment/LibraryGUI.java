import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Simple AWT desktop GUI for the Library Management System.
 *
 * Demonstrates:
 *  - AWT components: Frame, Panel, Label, TextField, Choice, Button, TextArea
 *  - Layout managers: BorderLayout (overall) + GridLayout (each form section)
 *  - Event handling: this class implements ActionListener and every
 *    button is registered with addActionListener(this); actionPerformed()
 *    dispatches on the event source, which satisfies the "2+ event
 *    listeners wired to controls" requirement (there are 8 here).
 */
public class LibraryGUI extends Frame implements ActionListener {

    private final Library library;

    // Add Member section
    private final TextField memberIdField = new TextField(8);
    private final TextField memberNameField = new TextField(10);
    private final TextField memberContactField = new TextField(10);
    private final Choice memberTypeChoice = new Choice();
    private final TextField memberExtra1Field = new TextField(8); // Roll No. / Designation
    private final TextField memberExtra2Field = new TextField(8); // Department
    private final Button addMemberBtn = new Button("Add Member");

    // Add Book section
    private final TextField bookIdField = new TextField(6);
    private final TextField bookTitleField = new TextField(10);
    private final TextField bookAuthorField = new TextField(10);
    private final TextField bookIsbnField = new TextField(8);
    private final TextField bookCopiesField = new TextField(3);
    private final Button addBookBtn = new Button("Add Book");

    // Search Book section
    private final TextField searchField = new TextField(10);
    private final Choice searchTypeChoice = new Choice();
    private final Button searchBtn = new Button("Search Book");

    // Issue Book section
    private final TextField issueMemberIdField = new TextField(6);
    private final TextField issueBookIdField = new TextField(6);
    private final Button issueBtn = new Button("Issue Book");

    // Return Book section
    private final TextField returnIssueIdField = new TextField(6);
    private final Button returnBtn = new Button("Return Book");

    // View Overdue
    private final Button overdueBtn = new Button("View Overdue");

    // Update Book section
    private final TextField updateBookIdField = new TextField(6);
    private final TextField updateCopiesField = new TextField(3);
    private final Button updateBtn = new Button("Update Book");

    // Delete Book section
    private final TextField deleteBookIdField = new TextField(6);
    private final Button deleteBtn = new Button("Delete Book");

    // Output console
    private final TextArea outputArea = new TextArea(12, 60);

    public LibraryGUI(Library library) {
        super("Library Management System");
        this.library = library;

        memberTypeChoice.add("Student");
        memberTypeChoice.add("Faculty");
        searchTypeChoice.add("Title");
        searchTypeChoice.add("Author");

        setLayout(new BorderLayout());
        add(buildFormPanel(), BorderLayout.CENTER);
        add(buildOutputPanel(), BorderLayout.SOUTH);

        registerListeners();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setSize(760, 640);
        setVisible(true);
    }

    private Panel buildFormPanel() {
        Panel main = new Panel(new GridLayout(0, 1, 4, 4));

        Panel memberPanel = new Panel(new GridLayout(2, 1));
        Panel memberRow1 = new Panel();
        memberRow1.add(new Label("Member ID:"));
        memberRow1.add(memberIdField);
        memberRow1.add(new Label("Name:"));
        memberRow1.add(memberNameField);
        memberRow1.add(new Label("Contact:"));
        memberRow1.add(memberContactField);
        Panel memberRow2 = new Panel();
        memberRow2.add(new Label("Type:"));
        memberRow2.add(memberTypeChoice);
        memberRow2.add(new Label("Roll No/Designation:"));
        memberRow2.add(memberExtra1Field);
        memberRow2.add(new Label("Department:"));
        memberRow2.add(memberExtra2Field);
        memberRow2.add(addMemberBtn);
        memberPanel.add(memberRow1);
        memberPanel.add(memberRow2);
        main.add(titled("1. Add Member", memberPanel));

        Panel bookPanel = new Panel();
        bookPanel.add(new Label("Book ID:"));
        bookPanel.add(bookIdField);
        bookPanel.add(new Label("Title:"));
        bookPanel.add(bookTitleField);
        bookPanel.add(new Label("Author:"));
        bookPanel.add(bookAuthorField);
        bookPanel.add(new Label("ISBN:"));
        bookPanel.add(bookIsbnField);
        bookPanel.add(new Label("Copies:"));
        bookPanel.add(bookCopiesField);
        bookPanel.add(addBookBtn);
        main.add(titled("2. Add Book", bookPanel));

        Panel searchPanel = new Panel();
        searchPanel.add(new Label("Keyword:"));
        searchPanel.add(searchField);
        searchPanel.add(new Label("Search by:"));
        searchPanel.add(searchTypeChoice);
        searchPanel.add(searchBtn);
        main.add(titled("3. Search Book", searchPanel));

        Panel issuePanel = new Panel();
        issuePanel.add(new Label("Member ID:"));
        issuePanel.add(issueMemberIdField);
        issuePanel.add(new Label("Book ID:"));
        issuePanel.add(issueBookIdField);
        issuePanel.add(issueBtn);
        main.add(titled("4. Issue Book", issuePanel));

        Panel returnPanel = new Panel();
        returnPanel.add(new Label("Issue ID:"));
        returnPanel.add(returnIssueIdField);
        returnPanel.add(returnBtn);
        returnPanel.add(overdueBtn);
        main.add(titled("5. Return Book / Overdue", returnPanel));

        Panel updatePanel = new Panel();
        updatePanel.add(new Label("Book ID:"));
        updatePanel.add(updateBookIdField);
        updatePanel.add(new Label("New Available Copies:"));
        updatePanel.add(updateCopiesField);
        updatePanel.add(updateBtn);
        main.add(titled("6. Update Book", updatePanel));

        Panel deletePanel = new Panel();
        deletePanel.add(new Label("Book ID:"));
        deletePanel.add(deleteBookIdField);
        deletePanel.add(deleteBtn);
        main.add(titled("7. Delete Book", deletePanel));

        return main;
    }

    private Panel titled(String title, Panel content) {
        Panel wrapper = new Panel(new BorderLayout());
        Label label = new Label(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        wrapper.add(label, BorderLayout.NORTH);
        wrapper.add(content, BorderLayout.CENTER);
        return wrapper;
    }

    private Panel buildOutputPanel() {
        Panel panel = new Panel(new BorderLayout());
        panel.add(new Label("Output:"), BorderLayout.NORTH);
        outputArea.setEditable(false);
        panel.add(outputArea, BorderLayout.CENTER);
        return panel;
    }

    private void registerListeners() {
        addMemberBtn.addActionListener(this);
        addBookBtn.addActionListener(this);
        searchBtn.addActionListener(this);
        issueBtn.addActionListener(this);
        returnBtn.addActionListener(this);
        overdueBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if (source == addMemberBtn) {
                handleAddMember();
            } else if (source == addBookBtn) {
                handleAddBook();
            } else if (source == searchBtn) {
                handleSearchBook();
            } else if (source == issueBtn) {
                handleIssueBook();
            } else if (source == returnBtn) {
                handleReturnBook();
            } else if (source == overdueBtn) {
                handleViewOverdue();
            } else if (source == updateBtn) {
                handleUpdateBook();
            } else if (source == deleteBtn) {
                handleDeleteBook();
            }
        } catch (BorrowingLimitExceededException ex) {
            // user-defined checked exception
            log("ERROR (Borrowing Limit): " + ex.getMessage());
        } catch (NoSuchElementException ex) {
            // built-in exception - member/book/issue id not found
            log("ERROR (Not Found): " + ex.getMessage());
        } catch (IllegalStateException ex) {
            // built-in exception - book unavailable
            log("ERROR (Unavailable): " + ex.getMessage());
        } catch (NumberFormatException ex) {
            // built-in exception - bad numeric input from a text field
            log("ERROR (Invalid Number): please enter valid numbers in numeric fields.");
        } finally {
            log("--------------------------------------------------");
        }
    }

    private void handleAddMember() {
        String id = memberIdField.getText().trim();
        String name = memberNameField.getText().trim();
        String contact = memberContactField.getText().trim();
        String extra1 = memberExtra1Field.getText().trim();
        String extra2 = memberExtra2Field.getText().trim();

        Member member;
        if ("Student".equals(memberTypeChoice.getSelectedItem())) {
            member = new Student(id, name, contact, extra1, extra2);
        } else {
            member = new Faculty(id, name, contact, extra1, extra2);
        }
        library.addMember(member);
        log("Added: " + member);
    }

    private void handleAddBook() {
        String id = bookIdField.getText().trim();
        String title = bookTitleField.getText().trim();
        String author = bookAuthorField.getText().trim();
        String isbn = bookIsbnField.getText().trim();
        int copies = Integer.parseInt(bookCopiesField.getText().trim()); // may throw NumberFormatException
        Book book = new Book(id, title, author, isbn, copies);
        library.addBook(book);
        log("Added: " + book);
    }

    private void handleSearchBook() {
        String keyword = searchField.getText().trim();
        List<Book> results = "Title".equals(searchTypeChoice.getSelectedItem())
                ? library.searchByTitle(keyword)
                : library.searchByAuthor(keyword);
        if (results.isEmpty()) {
            log("No books found for keyword: " + keyword);
        } else {
            log("Found " + results.size() + " book(s):");
            for (Book b : results) {
                log("  " + b);
            }
        }
    }

    private void handleIssueBook() throws BorrowingLimitExceededException {
        String memberId = issueMemberIdField.getText().trim();
        String bookId = issueBookIdField.getText().trim();
        IssueRecord record = library.issueBook(memberId, bookId);
        log("Issued successfully: " + record);
    }

    private void handleReturnBook() {
        int issueId = Integer.parseInt(returnIssueIdField.getText().trim()); // may throw NumberFormatException
        double fine = library.returnBook(issueId);
        if (fine > 0) {
            log("Book returned. Overdue fine charged: Rs. " + fine);
        } else {
            log("Book returned on time. No fine.");
        }
    }

    private void handleViewOverdue() {
        List<IssueRecord> overdue = library.getOverdueRecords();
        if (overdue.isEmpty()) {
            log("No overdue books right now.");
        } else {
            log("Overdue records (" + overdue.size() + "):");
            for (IssueRecord r : overdue) {
                log("  " + r);
            }
        }
    }

    private void handleUpdateBook() {
        String bookId = updateBookIdField.getText().trim();
        int newCopies = Integer.parseInt(updateCopiesField.getText().trim()); // may throw NumberFormatException
        boolean ok = library.updateBookCopies(bookId, newCopies);
        log(ok ? "Book " + bookId + " updated. Available copies = " + newCopies
                : "Book updated in memory, but database update failed (check DB connection).");
    }

    private void handleDeleteBook() {
        String bookId = deleteBookIdField.getText().trim();
        boolean ok = library.deleteBook(bookId);
        log(ok ? "Book " + bookId + " deleted." : "Delete failed (book not found in DB, or DB not connected).");
    }

    private void log(String message) {
        outputArea.append(message + "\n");
    }
}
