import java.time.LocalDate;

/**
 * Derived class representing a savings bank account.
 * Provides an interest rate and interval that is different from other account types.
 */
public class SavingsAccount extends Account {
    /** 
     * Constructs a SavingsAccount object.
     * @param name              Name of the account.
     * @param amount            Initial amount in the account.
     * @param lastCheckedDate   Date the account was last checked for interest. Important in interest calculations. 
     * InterestRate, CheckIntervalMonth, and TypeID are updated with values unique to Savings Account objects.
    */
    public SavingsAccount(String name, Double amount, LocalDate lastCheckedDate) {
        super(name, amount, lastCheckedDate);
        setInterestRate(0.001);
        setCheckIntervalMonth(1);
        setTypeID(1);
    }
}
