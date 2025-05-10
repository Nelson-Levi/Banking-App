import java.time.LocalDate;
import java.util.Scanner;

/**
 * Derived class representing a certificate of deposit bank account.
 * Provides an interest rate and interval that is different from other account types.
 * Overrides the inherited withdrawal method to allow for a 30% withdrawal penalty.
 */
public class CertificateAccount extends Account {
    /** Scanner object for reading input from the console. */
    private static final Scanner scannerObject = new Scanner(System.in);

    /** 
     * Constructs a CertificateAccount object.
     * @param name              Name of the account.
     * @param amount            Initial amount in the account.
     * @param lastCheckedDate   Date the account was last checked for interest. Important in interest calculations. 
     * InterestRate, CheckIntervalMonth, and TypeID are updated with values unique to Savings Account objects.
    */
    public CertificateAccount(String name, Double amount, LocalDate lastCheckedDate) {
        super(name, amount, lastCheckedDate);
        setInterestRate(0.0395);
        setCheckIntervalMonth(1);
        setTypeID(2);
    }

    /**
     * Overrides the withdrawal method for certificate accounts by applying a 30% penalty on the withdrawal amount. 
     * Prompts the user for confirmation before proceeding.
     *
     * <p>If the withdrawal amount causes the account to overdraw, an additional prompt 
     * is shown to confirm the overdraw. The penalty is calculated as 30% of the 
     * withdrawal amount and deducted from the account along with the original withdrawal.
     *
     * <p>Example: Withdrawing $100 results in a $30 penalty. A total of $130 is 
     * deducted from the account balance if approved by the user.
     *
     * @param withdrawalAmount The amount the user wishes to withdraw from the account.
     */
    @Override 
    public void withdrawal(double withdrawalAmount) {
        double amountAfter = getAmount() - withdrawalAmount;
        System.out.println("Certificate accounts receive a 30% penalty on withdrawals. This penalty is added to the withdrawal amount. Are you sure you would like to continue? [Y/N]");
        String firstInput = scannerObject.nextLine();
        String upperFirstInput = firstInput.toUpperCase();

        switch (upperFirstInput) {
            case "Y":
                if (amountAfter < 0) {
                    System.out.println("This will result in an overdraw of your account by $" + (withdrawalAmount - getAmount()) + ". Are you sure you would like to continue? [Y/N]");
                    String input = scannerObject.nextLine();
                    String upperInput = input.toUpperCase();
                    switch (upperInput) {
                        case "Y":
                            double penalty = (withdrawalAmount * .3);
                            amountAfter = amountAfter - penalty;
                            setAmount(amountAfter);
                            displayBalance();
                            break;
                        case "N":
                            System.out.println("Withdrawal canceled. ");
                            break;
                        default:
                            System.out.println("Invalid input. Withdrawal canceled. ");
                            break;
                    }
                }
                else {
                    double penalty = (withdrawalAmount * .3);
                    amountAfter = amountAfter - penalty;
                    setAmount(amountAfter);
                    displayBalance();
                }
                break;
            case "N":
                System.out.println("Withdrawal canceled. ");
                break;
            default:
                System.out.println("Invalid input. Withdrawal canceled. ");
                break;
        }
    }
}
