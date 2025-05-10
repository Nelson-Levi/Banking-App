import java.time.LocalDate;
import java.util.Scanner;

public class CertificateAccount extends Account {
    private static final Scanner scannerObject = new Scanner(System.in);
    public CertificateAccount(String name, Double amount, LocalDate lastCheckedDate) {
        super(name, amount, lastCheckedDate);
        SetInterestRate(0.0395);
        SetCheckIntervalMonth(1);
        SetTypeID(2);
    }

    @Override 
    public void Withdrawal(double withdrawalAmount) {
        double amountAfter = GetAmount() - withdrawalAmount;
        System.out.println("Certificate accounts receive a 30% penalty on withdrawals. This penalty is added to the withdrawal amount. Are you sure you would like to continue? [Y/N]");
        String firstInput = scannerObject.nextLine();
        String upperFirstInput = firstInput.toUpperCase();

        switch (upperFirstInput) {
            case "Y":
                if (amountAfter < 0) {
                    System.out.println("This will result in an overdraw of your account by $" + (withdrawalAmount - GetAmount()) + ". Are you sure you would like to continue? [Y/N]");
                    String input = scannerObject.nextLine();
                    String upperInput = input.toUpperCase();
                    switch (upperInput) {
                        case "Y":
                            double penalty = (withdrawalAmount * .3);
                            amountAfter = amountAfter - penalty;
                            SetAmount(amountAfter);
                            DisplayBalance();
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
                    SetAmount(amountAfter);
                    DisplayBalance();
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
