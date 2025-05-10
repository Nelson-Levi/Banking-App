import java.util.Scanner;

/**
 * The App class serves as the entry point for the Banking App program.
 * It prompts the user to either log in or create a new account and then provides a menu for managing bank accounts.
 *
 * <p>Actions include checking balances, making deposits and withdrawals,
 * saving data, and opening new accounts.
 */
public class App {
    private static final Scanner scannerObject = new Scanner(System.in);

    /**
     * The main method runs the command-line interface for the banking application.
     * It handles the initial login or account creation and displays a menu to
     * interact with the user's accounts until they choose to quit.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        int input = 0;
        System.out.println("Welcome to the Banking App.\n1. Log In\n2. Create Account");
        String welcomeInput = scannerObject.nextLine();
        int welcomeInputInt = Integer.parseInt(welcomeInput);

        if (welcomeInputInt == 1) {
            User loadedUser = User.login();
            if (loadedUser != null) {
                while (input != 6) {
                    System.out.println("\nWhat would you like to do? ");
                    System.out.println("1. Check Balance\n2. Withdraw\n3. Deposit\n4. Open New Account\n5. Save Data\n6. Quit");
                    String stringInput = scannerObject.nextLine();
                    if (stringInput.isEmpty()) {
                        System.out.println("No input found. Please enter a number.");
                        continue;
                    }
        
                    input = Integer.parseInt(stringInput);
        
                    switch (input) {
                        // Check Balance
                        case 1:
                            loadedUser.checkBalance();
                            break;
                        // Withdraw
                        case 2:
                            loadedUser.userWithdrawal();
                            break;
                        // Deposit
                        case 3:
                            loadedUser.userDeposit();
                            break;
                        // Create Bank Account
                        case 4:
                            loadedUser.createAccount();
                            break;
                        // Save Data
                        case 5:
                            loadedUser.saveAccount();
                            break;
                    }
                }
            }    
        } else if (welcomeInputInt == 2) {
            User.createNewUser();
        } else {
            System.out.println("Invalid input. Try again later.");
        }
    }      
}