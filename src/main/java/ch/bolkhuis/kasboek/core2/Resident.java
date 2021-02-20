package ch.bolkhuis.kasboek.core2;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.jetbrains.annotations.NotNull;

public class Resident extends Equity {

    @NotNull
    protected final ReadOnlyDoubleProperty previousBalance;

    /**
     * Default constructor.
     *
     * @param id
     * @param name    the name of this Accounting Entry
     * @param balance the balance of this Accounting Entry
     * @throws IllegalArgumentException if name does not adhere to the contract specified by this class
     */
    public Resident(int id, @NotNull String name, double balance, double previousBalance) throws IllegalArgumentException {
        super(id, name, balance);

        this.previousBalance = new SimpleDoubleProperty(previousBalance);
    }

    /**
     * Returns a new Resident equal to this Resident but with adjusted {@code previousBalance}.
     *
     * @param pb the new previousBalance
     * @return a new Resident
     */
    public Resident setPreviousBalance(double pb) {
        return new Resident(id, getName(), getBalance(), pb);
    }

    @NotNull
    public ReadOnlyDoubleProperty previousBalanceProperty() {
        return previousBalance;
    }
    public double getPreviousBalance() {
        return previousBalance.get();
    }

    @Override
    Resident debit(double amount) throws IllegalArgumentException {
        return new Resident(id, getName(), debitChange(amount), getPreviousBalance());
    }

    @Override
    Resident credit(double amount) throws IllegalArgumentException {
        return new Resident(id, getName(), creditChange(amount), getPreviousBalance());
    }
}
