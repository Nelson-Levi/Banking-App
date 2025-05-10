import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public abstract class Account {
    private String name;
    private Double amount;
    private LocalDate lastCheckedDate;
    private int checkIntervalMonth;
    private double interestRate;
    private int typeID;
    
    private static final Scanner scannerObject = new Scanner(System.in);

    public Account(String name, Double amount, LocalDate lastCheckedDate) {
        this.name = name;
        this.amount = amount;
        this.lastCheckedDate = lastCheckedDate;
    }

    // Getters and Setters
    public String GetName() {
        return name;
    }

    public double GetAmount() {
        return amount;
    }

    public void SetAmount(double newAmount) {
        amount = newAmount;
    }

    public LocalDate GetLastCheckedDate() {
        return lastCheckedDate;
    }

    public int GetCheckIntervalMonth() {
        return checkIntervalMonth;
    }

    public void SetCheckIntervalMonth(int checkIntervalMonth) {
        this.checkIntervalMonth = checkIntervalMonth;
    }

    public double GetInterestRate() {
        return interestRate;
    }

    public void SetInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void SetTypeID(int ID) {
        typeID = ID;
    }

    // Main methods start here
    public void DisplayBalance() {
        System.out.println();
        System.out.printf("%s Balance: $%.2f%n", name, amount);
    }

    public void Deposit(Double depositAmount) {
        amount = amount + depositAmount;
        DisplayBalance();
    }

    public void Withdrawal(double withdrawalAmount) {
        double amountAfter = amount - withdrawalAmount;
        if (amountAfter < 0) {
            System.out.println("This will result in an overdraw of your account by $" + (withdrawalAmount - amount) + ". Are you sure you would like to continue? [Y/N]");
            String input = scannerObject.nextLine();
            String upperInput = input.toUpperCase();

            switch (upperInput) {
                case "Y":
                    amount = amountAfter;
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
            amount = amountAfter;
            DisplayBalance();
        }
    }

    public void CheckDate() {
        LocalDate todayDate = LocalDate.now();
        LocalDate lastCheckedDate = GetLastCheckedDate();
        // LocalDate lastCheckedLocalDate = lastCheckedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long monthsBetween = ChronoUnit.MONTHS.between(lastCheckedDate, todayDate);
        double monthsBetweenRounded = Math.floor(monthsBetween);

        if (monthsBetweenRounded >= GetCheckIntervalMonth()) {
            AccrueInterest(monthsBetweenRounded);
        }
    }

    public void AccrueInterest(double monthsBetweenRounded) {
        double interest = GetAmount() * Math.pow(1 + GetInterestRate(), monthsBetweenRounded) - GetAmount();
        System.out.println("$" + interest + "accrued over" + monthsBetweenRounded + "months. ");
        Deposit(interest);
    }

    public String GetCSVString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String stringFormattedDate = lastCheckedDate.format(formatter);
        return String.format("%s,%.2f,%s,%d", name, amount, stringFormattedDate,typeID);
    }

}