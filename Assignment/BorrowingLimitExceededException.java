/**
 * USER-DEFINED EXCEPTION.
 *
 * Thrown when a member tries to issue a book while already holding
 * as many books as their member type allows (Member.getMaxBorrowLimit()).
 *
 * It is a CHECKED exception (extends Exception, not RuntimeException)
 * so that any method which can throw it must declare it with
 * "throws BorrowingLimitExceededException", and every caller is forced
 * to handle it with try-catch - this is deliberate so the GUI layer
 * cannot forget to handle the business rule.
 */
public class BorrowingLimitExceededException extends Exception {

    public BorrowingLimitExceededException(String message) {
        super(message);
    }
}
