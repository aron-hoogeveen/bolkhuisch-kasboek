package ch.bolkhuis.kasboek.core2;

import org.jetbrains.annotations.NotNull;

public class Liability extends CreditEntry {

    private final int id;

    /**
     * Default constructor.
     *
     * @param name    the name of this Accounting Entry
     * @param balance the balance of this Accounting Entry
     * @throws IllegalArgumentException if name does not adhere to the contract specified by this class
     */
    public Liability(int id, @NotNull String name, double balance) throws IllegalArgumentException {
        super(name, balance);

        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Liability liability = (Liability) o;

        return id == liability.id;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        return result;
    }

    @Override
    Liability debit(double amount) throws IllegalArgumentException {
        return new Liability(id, getName(), debitChange(amount));
    }

    @Override
    Liability credit(double amount) throws IllegalArgumentException {
        return new Liability(id, getName(), creditChange(amount));
    }

    @Override
    protected boolean checkName(String name) {
        return AccountingEntryNameCheck.checkName(name);
    }

}
