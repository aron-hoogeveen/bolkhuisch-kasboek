package ch.bolkhuis.kasboek.core;

import org.jetbrains.annotations.NotNull;

public class PlaceholderEntity extends AccountingEntity {
    /**
     * Constructs a new PlaceholderEntity with {@code id} and {@code name}.
     * <br />
     * For the constraints of {@code id} and {@code name} see {@link AccountingEntity#isCorrectId(int)} and
     * {@link AccountingEntity#isCorrectName(String)} respectively.
     *
     * @param id          a unique (negative) identifier
     * @param name        the name of this AccountingEntry
     * @param accountType the AccountType of this AccountingEntity
     * @param balance     the current balance
     * @see AccountingEntity#isCorrectId(int)
     * @see AccountingEntity#isCorrectName(String)
     */
    public PlaceholderEntity(int id, @NotNull String name, @NotNull AccountType accountType, double balance) {
        super(999, name, accountType, balance);
        if (id >= 0) { throw new IllegalArgumentException("Parameter id needs to be negative for a PlaceholderEntity"); }
    }
}
