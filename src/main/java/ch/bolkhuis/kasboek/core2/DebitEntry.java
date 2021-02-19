package ch.bolkhuis.kasboek.core2;

import org.jetbrains.annotations.NotNull;

/**
 * The abstract for all Accounting Entries that are of type debit.
 */
public abstract class DebitEntry extends AbstractAccountingEntry {

    /**
     * Default constructor.
     *
     * @param name    the name of this Accounting Entry
     * @param balance the balance of this Accounting Entry
     * @throws IllegalArgumentException if name does not adhere to the contract specified by this class
     */
    public DebitEntry(@NotNull String name, double balance) throws IllegalArgumentException {
        super(name, balance);
    }

    /**
     * Results the new new balance of this DebitEntry if it was credited with {@code amount}.
     *
     * @param amount the amount to credit
     * @return the new balance
     * @throws IllegalArgumentException if the amount is negative
     */
    @Override
    protected double creditChange(double amount) throws IllegalArgumentException {
        if (amount < 0)
            throw new IllegalArgumentException();

        return getBalance() - amount;
    }

    /**
     * Results the new new balance of this DebitEntry if it was debited with {@code amount}.
     *
     * @param amount the amount to debit
     * @return the new balance
     * @throws IllegalArgumentException if the amount is negative
     */
    @Override
    protected double debitChange(double amount) throws IllegalArgumentException {
        if (amount < 0)
            throw new IllegalArgumentException();

        return getBalance() + amount;
    }

}
