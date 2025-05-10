import java.util.Scanner;

public class App {
    private static final Scanner scannerObject = new Scanner(System.in);

    public static void main(String[] args) {
        int input = 0;
        System.out.println("Welcome to the Bank Advisor.\n1. Log In\n2. Create Account");
        String welcomeInput = scannerObject.nextLine();
        int welcomeInputInt = Integer.parseInt(welcomeInput);

        if (welcomeInputInt == 1) {
            User loadedUser = User.Login();
            if (loadedUser != null) {
                while (input != 6) {
                    System.out.println("\nWhat would you like to do? ");
                    System.out.println("1. Check Balance\n2. Withdraw\n3. Deposit\n4. Save Data\n5. Open New Account\n6. Quit");
                    String stringInput = scannerObject.nextLine();
                    if (stringInput.isEmpty()) {
                        System.out.println("No input found. Please enter a number.");
                        continue;
                    }
        
                    input = Integer.parseInt(stringInput);
        
                    switch (input) {
                        // Check Balance
                        case 1:
                            loadedUser.CheckBalance();
                            break;
                        // Withdraw
                        case 2:
                            loadedUser.UserWithdrawal();
                            break;
                        // Deposit
                        case 3:
                            loadedUser.UserDeposit();
                            break;
                        // Save
                        case 4:
                            loadedUser.SaveAccount();
                            break;
                        // Create Bank Account
                        case 5:
                            loadedUser.CreateAccount();
                            break;
                    }
                }
            }    
        } else if (welcomeInputInt == 2) {
            User.CreateNewUser();
        } else {
            System.out.println("Invalid input. Try again later.");
        }
    }      
}