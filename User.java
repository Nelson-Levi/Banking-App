import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FilenameFilter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Public class representing a user of the banking system, who can have multiple accounts.
 * A user can log in, create accounts, perform deposits, withdrawals, and save/load data to a CSV file behind a username and password.
 */
public class User {
    /** Scanner object for reading input from the console. */
    private static final Scanner scannerObject = new Scanner(System.in);

    private String username;
    private String password;
    private ArrayList<Account> accounts;

    
    /**
     * Constructs a new User with the specified username and password.
     *
     * @param username The user's username.
     * @param password The user's password.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<Account>();
    }

    
    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Prompts the user to select one of their accounts from a numbered list.
     *
     * @return The selected Account, or null if an invalid option is chosen.
     */
    public Account selectAccount() {
        System.out.println("Select an account. ");

        int i = 0;
        for (Account account : accounts) {

            String accountName = account.getName();
            System.out.println((i + 1) + ". " + accountName);
            i++;
        }

        String input = scannerObject.nextLine();
        try {
            int inputInt = Integer.parseInt(input);
            if (inputInt < 1 || inputInt > accounts.size()) {
                System.out.println("Invalid option. ");
                return null;
            }
            Account ChosenAccount = accounts.get(inputInt - 1);
            return ChosenAccount;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number. ");
            return null;
        }
    }

   /** Allows the user to create a new account and add it to their list of accounts. */
   public void createAccount() {
    System.out.println("What kind of account would you like to create? ");
    System.out.println("1. Checking\n2. Savings\n3. Certificate");
    String inputString = scannerObject.nextLine();
    int inputInt = Integer.parseInt(inputString);

    switch (inputInt) {
        case 1:
            Account newChecking = new CheckingAccount(getNewAccountName(), getNewAccountBalance(), LocalDate.now());
            newChecking.displayBalance();
            accounts.add(newChecking);
            break;
        case 2:
            Account newSavings = new SavingsAccount(getNewAccountName(), getNewAccountBalance(), LocalDate.now());
            newSavings.displayBalance();
            accounts.add(newSavings);
            break;
        case 3:
            Account newCertificate = new CertificateAccount(getNewAccountName(), getNewAccountBalance(), LocalDate.now());
            newCertificate.displayBalance();
            accounts.add(newCertificate);
            break;
        default:
            System.out.println("Invalid option. ");
            break;
        }
    }

   /**
     * Creates and adds an account to the user's list based on data from file.
     *
     * @param title           The account name.
     * @param amount          The account balance.
     * @param lastCheckedDate The date the account was last updated.
     * @param typeID          An integer representing the type of account.
     */
    public void createLoadedAccount(String title, double amount, LocalDate lastCheckedDate, int typeID) {
        switch (typeID) {
            case 0:
                CheckingAccount newCheckingAccount = new CheckingAccount(title, amount, lastCheckedDate);
                this.accounts.add(newCheckingAccount);
            break;
            case 1:
                SavingsAccount newSavingsAccount = new SavingsAccount(title, amount, lastCheckedDate);
                this.accounts.add(newSavingsAccount);
            break;
            case 2:
                CertificateAccount newCertificateAccount = new CertificateAccount(title, amount, lastCheckedDate);
                this.accounts.add(newCertificateAccount);
            break;
        }
    }

    /**
     * Prompts the user to enter a name for a new account.
     *
     * @return The entered account name.
     */
    public String getNewAccountName() {
        System.out.println("What will the account name be? ");
        return scannerObject.nextLine();
    }

    /**
     * Prompts the user to enter a starting balance for a new account.
     *
     * @return The entered balance as a Double.
     */
    public Double getNewAccountBalance() {
        System.out.println("Input a starting account balance. Enter 0 for no starting balance. ");
        return readDoubleFromUser();
    }

    /** Allows the user to check the balance of a selected account. */
    public void checkBalance() {
        Account selectedAccount = selectAccount();
        if (selectedAccount != null) {
            selectedAccount.checkDate();
            selectedAccount.displayBalance();
        }
    }
    
    /** Allows the user to withdraw money from a selected account. */
    public void userWithdrawal() {
            Account selectedAccount = selectAccount();
            if (selectedAccount != null) {

                selectedAccount.displayBalance();
                System.out.println("Enter withdrawal amount. ");
                Double inputDouble = readDoubleFromUser();
            if (inputDouble != null) {
                selectedAccount.withdrawal(inputDouble);
            }
        }
    }

    /** Allows the user to deposit money into a selected account. */
    public void userDeposit() {
            Account selectedAccount = selectAccount();
            if (selectedAccount != null) {

                selectedAccount.displayBalance();
                System.out.println("Enter deposit amount. ");
                Double inputDouble = readDoubleFromUser();
            if (inputDouble != null) {
                selectedAccount.deposit(inputDouble);
            }
        }
    }

    /** Saves the user's data (username, password, and accounts) to a CSV file. */
    public void saveAccount() {
        String fileName = String.format("%s.csv", username);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append(username + "," + password);
            writer.append("\n");
            for (Account account : accounts) {
                writer.append(account.getCSVString());
                writer.append("\n");
            }
            System.out.println("Finish");
        } catch (IOException error) {
            System.err.println("An error occured writing the file. ");
            error.printStackTrace();
        }
    }

    /**
     * Loads account data from a CSV file into the user's account list.
     *
     * @param file The username used to identify the file.
     */
    public void loadUserAccounts(String file) {
        File loginFile = findFileByUsername(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(loginFile))) {
            //Skipping the first line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");
                String title = parts [0];
                double amount = Double.parseDouble(parts[1]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate localDateCheckTime = LocalDate.parse(parts[2], formatter);
                int typeID = Integer.parseInt(parts[3]);
                createLoadedAccount(title, amount, localDateCheckTime, typeID);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

        /**
     * Searches for a file matching the user's username.
     *
     * @param username The username to search for.
     * @return The matching file, or null if no match is found.
     */
    public static File findFileByUsername(String username) {
        File folder = new File(System.getProperty("user.dir"));

        File[] matchingFiles = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.equals(username + ".csv");
            }
        });

        if (matchingFiles != null && matchingFiles.length > 0) {
            return matchingFiles[0];
        } else {
            System.out.println("No file found for this user. You might try opening a new account! ");
            return null;
        }
    }

    /**
     * Reads a double value from the user input.
     *
     * @return The input value as a Double, or null if the input is invalid.
     */
    public Double readDoubleFromUser() {
        String input = scannerObject.nextLine();
        try {
            Double inputDouble = Double.parseDouble(input);
            return inputDouble;
        } catch (NumberFormatException e) {
            System.out.println("Non-numerical value entered. ");
            return null;
        }
    }
    
    /**
     * Attempts to log a user in by verifying username and password against a stored file.
     *
     * @return A User object if login is successful, or null if not.
     */
    public static User login() {
        System.out.println("Enter username (CaSE seNSItivE).");
        String inputUsername = scannerObject.nextLine();
        System.out.println("Enter password (CaSE seNSItivE).");
        String inputPassword = scannerObject.nextLine();

        File loginFile = findFileByUsername(inputUsername);

        if (loginFile != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(loginFile))) {
                String line = reader.readLine();
                String check = inputUsername + "," + inputPassword;
                if (line.equals(check)) {
                    System.out.println("Login successful. ");
                    User LoadedUser = new User(inputUsername, inputPassword);
                    LoadedUser.loadUserAccounts(LoadedUser.username);
                    return LoadedUser;
                }
                else {
                    System.out.println("Login unsuccessful. Please check your credentials.");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("Login unsuccessful.");
            return null;
        }
    }

    /**
     * Creates a new user by asking for a username, password, and an initial account.
     * Saves the user to a file and ends the session.
     */
    public static void createNewUser() {
        System.out.println("Enter username. ");
        String username = scannerObject.nextLine();
        System.out.println("Enter password. ");
        String password = scannerObject.nextLine();
        User newUser = new User(username, password);

        System.out.println("Every user must have at least one account. Create your first account by following the instructions: ");
        newUser.createAccount();
        newUser.saveAccount();
        System.out.println("The program will now end. Run the program again to log in to your account. ");
        return;
    }
}

