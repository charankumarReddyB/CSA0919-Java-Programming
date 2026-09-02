/**
 * Represents a single title in the library catalogue.
 * Encapsulated: fields are private, accessed only via getters/setters.
 * A book can have multiple physical copies; availableCopies tracks how
 * many are currently free to issue.
 */
public class Book {

    private final String bookId;
    private String title;
    private String author;
    private String isbn;
    private int totalCopies;
    private int availableCopies;

    public Book(String bookId, String title, String author, String isbn, int totalCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    /** Called when a copy is issued. */
    void decrementAvailable() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    /** Called when a copy is returned. */
    void incrementAvailable() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    @Override
    public String toString() {
        return String.format("Book[ID=%s, Title=%s, Author=%s, ISBN=%s, Available=%d/%d]",
                bookId, title, author, isbn, availableCopies, totalCopies);
    }
}
