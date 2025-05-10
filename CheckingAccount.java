import java.time.LocalDate;

public class CheckingAccount extends Account{
    
    public CheckingAccount(String name, Double amount, LocalDate lastCheckedDate) {
        super(name, amount, lastCheckedDate);
        SetInterestRate(0.0003);
        SetCheckIntervalMonth(3);
        SetTypeID(0);
    }
}
