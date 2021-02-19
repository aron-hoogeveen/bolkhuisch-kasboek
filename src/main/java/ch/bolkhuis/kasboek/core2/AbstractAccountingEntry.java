package ch.bolkhuis.kasboek.core2;

import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * The base class for all accounting entries with balances connected to them.
 */
public abstract class AbstractAccountingEntry implements Comparable<AbstractAccountingEntry> {

    @NotNull
    private final ReadOnlyStringProperty name;
    @NotNull
    private final ReadOnlyDoubleProperty balance;

    /**
     * Default constructor.
     *
     * @param name the name of this Accounting Entry
     * @param balance the balance of this Accounting Entry
     * @throws IllegalArgumentException if name does not adhere to the contract specified by this class
     * @throws NullPointerException if {@code name} is null
     */
    public AbstractAccountingEntry(@NotNull String name, double balance) throws IllegalArgumentException, NullPointerException {
        Objects.requireNonNull(name);

        // factory method, check the name
        if (!checkName(name))
            throw new IllegalArgumentException();

        this.name = new SimpleStringProperty(name);
        this.balance = new SimpleDoubleProperty(balance);
    }

    abstract public int getId();

    @NotNull
    public ReadOnlyStringProperty nameProperty() {
        return name;
    }
    @NotNull
    public String getName() {
        return name.get();
    }

    @NotNull
    public ReadOnlyDoubleProperty balanceProperty() {
        return balance;
    }
    public double getBalance() {
        return balance.get();
    }

    abstract protected double creditChange(double amount) throws IllegalArgumentException;

    abstract protected double debitChange(double amount) throws IllegalArgumentException;

    abstract AbstractAccountingEntry debit(double amount) throws IllegalArgumentException;

    abstract AbstractAccountingEntry credit(double amount) throws IllegalArgumentException;

    /**
     * Checks if the provided {@code name} adheres to the contract specified by
     * this class.
     *
     * The default implementation always returns {@code true}. Child classes that
     * want to use this class should overwrite this method.
     *
     * @param name the name to check
     * @return true
     */
    protected boolean checkName(String name) {
        return true;
    }

    /**
     * Compares the provided AbstractAccountingEntry {@code o} to this.
     *
     * @param o the object to compare to
     * @return -1, 0, or 1
     */
    @Override
    public int compareTo(@NotNull AbstractAccountingEntry o) {
        Objects.requireNonNull(o);

        if (this.getId() > o.getId())
            return 1;
        if (this.getId() < o.getId())
            return -1;

        if (name.get().compareTo(o.name.get()) > 0)
            return 1;
        if (name.get().compareTo(o.name.get()) < 0)
            return -1;

        return Double.compare(this.balance.get(), o.balance.get());
    }

    /**
     * Checks the other object for equality with this AbstractAccountingEntry. The objects
     * are considered equal when they have the equal names.
     *
     * @param o the other object
     * @return {@code true} if {@code o} is equal to this, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractAccountingEntry that = (AbstractAccountingEntry) o;

        return name.get().equals(that.name.get());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
