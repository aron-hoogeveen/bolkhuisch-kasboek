package ch.bolkhuis.kasboek.core;

import org.jetbrains.annotations.NotNull;

public class InmateEntity extends AccountingEntity {
    /**
     * The vale of {@code balance} at the time the last invoice was generated for this InmateEntity
     */
    private final double previousBalance;
    /**
     * Constructs a new AccountingEntry with {@code id} and {@code name}.
     * <br />
     * For the constraints of {@code id} and {@code name} see {@link AccountingEntity#isCorrectId(int)} and
     * {@link AccountingEntity#isCorrectName(String)} respectively. An Inmate is always an {@link AccountType#LIABILITY}.
     *
     * @param id           a unique identifier
     * @param name         the name of this AccountingEntry
     * @param previousBalance the value of endBalance at the time the last invoice was generated for this InmateEntity
     * @param balance   the current balance of this InmateEntity
     * @see AccountingEntity#isCorrectId(int)
     * @see AccountingEntity#isCorrectName(String)
     */
    public InmateEntity(int id, @NotNull String name, double previousBalance, double balance) {
        super(id, name, AccountType.LIABILITY, balance);

        this.previousBalance = previousBalance;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    @Override
    public @NotNull InmateEntity debit(double amount) {
        if (amount < 0) { throw new IllegalArgumentException("You should not debit a negative amount, credit instead"); }

        double balance = (accountType.isDebit()) ? this.balance + amount : this.balance - amount;
        return new InmateEntity(id, name, previousBalance, balance);
    }

    @Override
    public @NotNull InmateEntity credit(double amount) {
        if (amount < 0) { throw new IllegalArgumentException("You should not credit a negative amount, debit instead"); }

        double balance = (accountType.isDebit()) ? this.balance - amount : this.balance + amount;
        return new InmateEntity(id, name, previousBalance, balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        InmateEntity that = (InmateEntity) o;

        return Double.compare(that.previousBalance, previousBalance) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(previousBalance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
