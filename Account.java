import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Abstract class representing a generic bank account with interest accrual.
 * Provides common operations such as deposit, withdrawal, and interest calculation, that are shared by all derived classes.
 */

public abstract class Account {
    private String name;
    private Double amount;
    private LocalDate lastCheckedDate;
    private int checkIntervalMonth;
    private double interestRate;
    private int typeID;
    
    /** Shared scanner for console input.*/
    private static final Scanner scannerObject = new Scanner(System.in);

    /** 
     * Constructs an Account object.
     * @param name              Name of the account.
     * @param amount            Initial amount in the account.
     * @param lastCheckedDate   Date the account was last checked for interest. Important in interest calculations. 
    */
    public Account(String name, Double amount, LocalDate lastCheckedDate) {
        this.name = name;
        this.amount = amount;
        this.lastCheckedDate = lastCheckedDate;
    }

    // Getters and Setters

    /** @return The name of the account. */
    public String getName() {
        return name;
    }

    /** @return The current amount in the account. */
    public double getAmount() {
        return amount;
    }

    /** 
     * @return Updates the amount in the account.
     * @param newAmount New account balance.
     */
    public void setAmount(double newAmount) {
        amount = newAmount;
    }

    /** @return The date the account was last checked. */
    public LocalDate getLastCheckedDate() {
        return lastCheckedDate;
    }

    /** @return The number of months between interval checks. */
    public int getCheckIntervalMonth() {
        return checkIntervalMonth;
    }

    /**
     * Sets how frequently (in months) interest should be checked.
     * 
     * @param checkIntervalMonth Interval in months.
     */
    public void setCheckIntervalMonth(int checkIntervalMonth) {
        this.checkIntervalMonth = checkIntervalMonth;
    }

    /** @return The interest rate for this account. */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Sets the interest rate for this account.
     * 
     * @param interestRate Annual interest rate (e.g., 0.05 for 5%).
     */
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * Sets the account type identifier.
     * 
     * @param ID Integer representing the account type.
     */
    public void setTypeID(int ID) {
        typeID = ID;
    }

    // Main methods start here

     /** Displays the current balance of the account. */
    public void displayBalance() {
        System.out.println();
        System.out.printf("%s Balance: $%.2f%n", name, amount);
    }

    /**
     * Deposits a specified amount into the account.
     * 
     * @param depositAmount The amount to deposit.
     */
    public void deposit(Double depositAmount) {
        amount = amount + depositAmount;
        displayBalance();
    }

    /**
     * Withdraws a specified amount from the account.
     * Prompts user for confirmation if the account will be overdrawn.
     * 
     * @param withdrawalAmount The amount to withdraw.
     */
    public void withdrawal(double withdrawalAmount) {
        double amountAfter = amount - withdrawalAmount;
        if (amountAfter < 0) {
            System.out.println("This will result in an overdraw of your account by $" + String.format("%.2f", (withdrawalAmount - amount)) + ". Are you sure you would like to continue? [Y/N]");
            String input = scannerObject.nextLine();
            String upperInput = input.toUpperCase();

            switch (upperInput) {
                case "Y":
                    amount = amountAfter;
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
            amount = amountAfter;
            displayBalance();
        }
    }

    /** Checks how much time has passed since the last interest check, and applies interest if the interval threshold is reached. */
    public void checkDate() {
        LocalDate todayDate = LocalDate.now();
        LocalDate lastCheckedDate = getLastCheckedDate();
        // LocalDate lastCheckedLocalDate = lastCheckedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long monthsBetween = ChronoUnit.MONTHS.between(lastCheckedDate, todayDate);
        double monthsBetweenRounded = Math.floor(monthsBetween);

        if (monthsBetweenRounded >= getCheckIntervalMonth()) {
            accrueInterest(monthsBetweenRounded);
        }
    }

    /**
     * Applies compound interest based on the number of months since the last check.
     * 
     * @param monthsBetweenRounded Number of months for interest to be applied.
     */
    public void accrueInterest(double monthsBetweenRounded) {
        double interest = getAmount() * Math.pow(1 + getInterestRate(), monthsBetweenRounded) - getAmount();
        System.out.println(String.format("$%.2f accrued over %.0f months. ", interest, monthsBetweenRounded));
        deposit(interest);
        this.lastCheckedDate = LocalDate.now();
    }

    /**
     * Converts account information to a CSV-compatible string.
     * 
     * @return Formatted CSV string of account data.
     */
    public String getCSVString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String stringFormattedDate = lastCheckedDate.format(formatter);
        return String.format("%s,%.2f,%s,%d", name, amount, stringFormattedDate,typeID);
    }
}