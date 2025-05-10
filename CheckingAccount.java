import java.time.LocalDate;

/**
 * Derived class representing a checking bank account.
 * Provides an interest rate and interval that is different from other account types.
 */
public class CheckingAccount extends Account {
    /** 
     * Constructs a CheckingAccount object.
     * @param name              Name of the account.
     * @param amount            Initial amount in the account.
     * @param lastCheckedDate   Date the account was last checked for interest. Important in interest calculations. 
     * InterestRate, CheckIntervalMonth, and TypeID are updated with values unique to Checking Account objects.
    */
    public CheckingAccount(String name, Double amount, LocalDate lastCheckedDate) {
        super(name, amount, lastCheckedDate);
        setInterestRate(0.0003);
        setCheckIntervalMonth(3);
        setTypeID(0);
    }
}
