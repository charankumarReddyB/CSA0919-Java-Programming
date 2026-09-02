/**
 * Student member type.
 *
 * INHERITANCE: extends Member and reuses memberId/name/contactNumber
 * handling from the base class.
 *
 * POLYMORPHISM (method overriding): supplies its own borrowing limit
 * and fine rate, different from Faculty.
 */
public class Student extends Member {

    private static final int MAX_BOOKS = 3;
    private static final double FINE_PER_DAY = 5.0; // Rs. 5/day for students

    private String rollNumber;
    private String department;

    public Student(String memberId, String name, String contactNumber,
                    String rollNumber, String department) {
        super(memberId, name, contactNumber);
        this.rollNumber = rollNumber;
        this.department = department;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
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
        return "Student";
    }
}
