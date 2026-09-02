import java.util.List;

/**
 * OPTIONAL bonus requirement: a simple background thread that
 * periodically scans the issue log and flags overdue books, without
 * freezing the GUI (it runs on its own thread, separate from the AWT
 * Event Dispatch Thread that handles button clicks).
 *
 * SYNCHRONIZATION NOTE (for the viva / discussion write-up):
 * This thread reads Library's issueRecords list (via getOverdueRecords())
 * at the same time the GUI thread may be adding a new issue record
 * (issueBook) or updating one (returnBook). Library.issueRecords is a
 * plain ArrayList, which is NOT thread-safe - concurrent add() and
 * iteration can throw ConcurrentModificationException or corrupt
 * internal state. In this simple student project we avoid the problem
 * by only ever reading a defensive copy (getOverdueRecords() already
 * builds a new list), but in a production system the shared list
 * should be wrapped with Collections.synchronizedList(...) or replaced
 * with a thread-safe collection such as CopyOnWriteArrayList, and
 * updates to a single IssueRecord (return date / fine / status) should
 * be done inside a synchronized block to prevent a lost update if two
 * threads modify the same record at once.
 */
public class OverdueFineScanner extends Thread {

    private final Library library;
    private final long scanIntervalMillis;
    private volatile boolean running = true;

    public OverdueFineScanner(Library library, long scanIntervalMillis) {
        super("OverdueFineScanner");
        this.library = library;
        this.scanIntervalMillis = scanIntervalMillis;
        setDaemon(true); // don't prevent the JVM from exiting
    }

    public void stopScanning() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        while (running) {
            try {
                List<IssueRecord> overdue = library.getOverdueRecords();
                if (!overdue.isEmpty()) {
                    System.out.println("[OverdueFineScanner] " + overdue.size() + " overdue book(s) detected.");
                }
                Thread.sleep(scanIntervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
