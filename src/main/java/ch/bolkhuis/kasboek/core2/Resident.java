package ch.bolkhuis.kasboek.core2;

import org.jetbrains.annotations.NotNull;

public class Resident extends Equity {

    /**
     * Default constructor.
     *
     * @param id
     * @param name    the name of this Accounting Entry
     * @param balance the balance of this Accounting Entry
     * @throws IllegalArgumentException if name does not adhere to the contract specified by this class
     */
    public Resident(int id, @NotNull String name, double balance) throws IllegalArgumentException {
        super(id, name, balance);
    }

}
