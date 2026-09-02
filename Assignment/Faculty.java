/**
 * Faculty member type.
 *
 * INHERITANCE: extends Member.
 *
 * POLYMORPHISM (method overriding): Faculty gets a higher borrowing
 * limit and a lower daily fine rate than Student, proving that the
 * same method call (getMaxBorrowLimit / getFineRatePerDay) behaves
 * differently depending on the actual runtime object.
 */
public class Faculty extends Member {

    private static final int MAX_BOOKS = 5;
    private static final double FINE_PER_DAY = 2.0; // Rs. 2/day for faculty

    private String designation;
    private String department;

    public Faculty(String memberId, String name, String contactNumber,
                    String designation, String department) {
        super(memberId, name, contactNumber);
        this.designation = designation;
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public int getMaxBorrowLimit() {
        return MAX_BOOKS;
    }

    @Override
    public double getFineRatePerDay() {
        return FINE_PER_DAY;
    }

    @Override
    public String getMemberType() {
        return "Faculty";
    }
}
