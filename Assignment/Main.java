/**
 * Entry point. Creates the Library, optionally pre-loads a couple of
 * in-memory demo records (handy for a viva demo even if MySQL is not
 * running yet), starts the background overdue scanner thread, and
 * launches the AWT GUI.
 */
public class Main {

    public static void main(String[] args) {
        Library library = new Library();

        seedDemoData(library);

        // Optional bonus requirement: background thread that scans for
        // overdue books every 30 seconds without blocking the GUI.
        OverdueFineScanner scanner = new OverdueFineScanner(library, 30_000);
        scanner.start();

        // AWT components must be created/updated on the Event Dispatch
        // Thread; invokeLater keeps the GUI responsive from the start.
        java.awt.EventQueue.invokeLater(() -> new LibraryGUI(library));
    }

    /** A few in-memory records so the GUI has something to show immediately. */
    private static void seedDemoData(Library library) {
        library.addMember(new Student("S1", "Arun Kumar", "9876500001", "CS21B01", "CSE"));
        library.addMember(new Faculty("F1", "Dr. Meena Rao", "9876500002", "Professor", "CSE"));

        library.addBook(new Book("B1", "Introduction to Algorithms", "Cormen", "978-0262033848", 2));
        library.addBook(new Book("B2", "Effective Java", "Joshua Bloch", "978-0134685991", 1));
        library.addBook(new Book("B3", "Clean Code", "Robert Martin", "978-0132350884", 3));
    }
}
