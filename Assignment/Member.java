import java.io.Serializable;

/**
 * Abstract base class for all library members.
 *
 * Demonstrates ENCAPSULATION: all fields are private and only reachable
 * through public getters/setters.
 *
 * Demonstrates the base of INHERITANCE: Student and Faculty extend this
 * class and are forced (via abstract methods) to supply their own
 * borrowing limit and fine rate. When those methods are invoked through
 * a Member reference at runtime, RUNTIME POLYMORPHISM occurs (see
 * Library.issueBook() and Library.returnBook() where a Member variable
 * holds a Student or Faculty object and the correct overridden method
 * is picked automatically by the JVM).
 */
public abstract class Member implements Serializable {

    private final String memberId;
    private String name;
    private String contactNumber;

    protected Member(String memberId, String name, String contactNumber) {
        this.memberId = memberId;
        this.name = name;
        this.contactNumber = contactNumber;
    }

    // ---- Encapsulated access (getters / setters) ----
    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * Maximum number of books this member type is allowed to borrow
     * at the same time. Overridden by every subclass -> POLYMORPHISM.
     */
    public abstract int getMaxBorrowLimit();

    /**
     * Fine charged per day (in rupees) once a book issued to this
     * member becomes overdue. Overridden by every subclass -> POLYMORPHISM.
     */
    public abstract double getFineRatePerDay();

    /**
     * Human readable member type, used by the GUI and DB layer.
     */
    public abstract String getMemberType();

    /**
     * Fine calculation shared by all member types, but the RATE used
     * inside it depends on which subclass actually overrode
     * getFineRatePerDay() -> this method itself is NOT overridden, but
     * the value it depends on is resolved polymorphically at runtime.
     */
    public double calculateFine(long overdueDays) {
        if (overdueDays <= 0) {
            return 0.0;
        }
        return overdueDays * getFineRatePerDay();
    }

    @Override
    public String toString() {
        return String.format("%s [ID=%s, Name=%s, Contact=%s, MaxBooks=%d, FineRate=Rs.%.2f/day]",
                getMemberType(), memberId, name, contactNumber, getMaxBorrowLimit(), getFineRatePerDay());
    }
}
