import java.time.LocalDate;

public class SavingsAccount extends Account {
        public SavingsAccount(String name, Double amount, LocalDate lastCheckedDate) {
        super(name, amount, lastCheckedDate);
        SetInterestRate(0.001);
        SetCheckIntervalMonth(1);
        SetTypeID(1);
    }
}
