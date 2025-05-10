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

public class User {
    private static final Scanner scannerObject = new Scanner(System.in);

    private String username;
    private String password;
    private ArrayList<Account> accounts;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<Account>();
    }

    public String GetUsername() {
        return username;
    }

    public Account SelectAccount() {
        System.out.println("Select an account. ");

        int i = 0;
        for (Account account : accounts) {

            String accountName = account.GetName();
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
        // scannerObject.close();
    }

    public void CheckBalance() {
        Account selectedAccount = SelectAccount();
        if (selectedAccount != null) {
            selectedAccount.CheckDate();
            selectedAccount.DisplayBalance();
        }
    }

    public void UserWithdrawal() {
            Account selectedAccount = SelectAccount();
            if (selectedAccount != null) {

                selectedAccount.DisplayBalance();
                System.out.println("Enter withdrawal amount. ");
                Double inputDouble = ReadDoubleFromUser();
            if (inputDouble != null) {
                selectedAccount.Withdrawal(inputDouble);
            }
                // scannerObject.close();
        }
    }

    public void UserDeposit() {
            Account selectedAccount = SelectAccount();
            if (selectedAccount != null) {

                selectedAccount.DisplayBalance();
                System.out.println("Enter deposit amount. ");
                Double inputDouble = ReadDoubleFromUser();
            if (inputDouble != null) {
                selectedAccount.Deposit(inputDouble);
            }
            // scannerObject.close();
        }
    }

    public void SaveAccount() {
        String fileName = String.format("%s.csv", username);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append(username + "," + password);
            writer.append("\n");
            for (Account account : accounts) {
                writer.append(account.GetCSVString());
                writer.append("\n");
            }
            System.out.println("Finish");
        } catch (IOException error) {
            System.err.println("An error occured writing the file. ");
            error.printStackTrace();
        }
    }

    public Double ReadDoubleFromUser() {
        String input = scannerObject.nextLine();
        try {
            Double inputDouble = Double.parseDouble(input);
            return inputDouble;
        } catch (NumberFormatException e) {
            System.out.println("Non-numerical value entered. ");
            return null;
        }
    }

    public void CreateAccount() {
        System.out.println("What kind of account would you like to create? ");
        System.out.println("1. Checking\n2. Savings\n3. Certificate");
        String inputString = scannerObject.nextLine();
        int inputInt = Integer.parseInt(inputString);

        switch (inputInt) {
            case 1:
                Account newChecking = new CheckingAccount(GetNewAccountName(), GetNewAccountBalance(), LocalDate.now());
                newChecking.DisplayBalance();
                accounts.add(newChecking);
                break;
            case 2:
                Account newSavings = new SavingsAccount(GetNewAccountName(), GetNewAccountBalance(), LocalDate.now());
                newSavings.DisplayBalance();
                accounts.add(newSavings);
                break;
            case 3:
                Account newCertificate = new CertificateAccount(GetNewAccountName(), GetNewAccountBalance(), LocalDate.now());
                newCertificate.DisplayBalance();
                accounts.add(newCertificate);
                break;
            default:
                System.out.println("Invalid option. ");
                break;
        }
    }

    public String GetNewAccountName() {
        System.out.println("What will the account name be? ");
        return scannerObject.nextLine();
    }

    public Double GetNewAccountBalance() {
        System.out.println("Input a starting account balance. Enter 0 for no starting balance. ");
        return ReadDoubleFromUser();
    }

    public static User Login() {
        System.out.println("Enter username (CaSE seNSItivE).");
        String inputUsername = scannerObject.nextLine();
        System.out.println("Enter password (CaSE seNSItivE).");
        String inputPassword = scannerObject.nextLine();

        File loginFile = FindFileByUsername(inputUsername);

        if (loginFile != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(loginFile))) {
                String line = reader.readLine();
                String check = inputUsername + "," + inputPassword;
                if (line.equals(check)) {
                    System.out.println("Login successful. ");
                    User LoadedUser = new User(inputUsername, inputPassword);
                    LoadedUser.LoadUserAccounts(LoadedUser.username);
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
    
    public void LoadUserAccounts(String file) {
        File loginFile = FindFileByUsername(file);
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
                CreateLoadedAccount(title, amount, localDateCheckTime, typeID);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public void CreateLoadedAccount(String title, double amount, LocalDate lastCheckedDate, int typeID) {
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

    public static void CreateNewUser() {
        System.out.println("Enter username. ");
        String username = scannerObject.nextLine();
        System.out.println("Enter password. ");
        String password = scannerObject.nextLine();
        User newUser = new User(username, password);

        System.out.println("Every user must have at least one account. Create your first account by following the instructions: ");
        newUser.CreateAccount();
        newUser.SaveAccount();
        System.out.println("The program will now end. Run the program again to log in to your account. ");
        return;
    }

    public static File FindFileByUsername(String username) {
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
}

