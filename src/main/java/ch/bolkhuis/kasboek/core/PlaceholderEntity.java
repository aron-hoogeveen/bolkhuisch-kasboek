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
     * @see AccountingEntity#isCorrectId(int)
     * @see AccountingEntity#isCorrectName(String)
     */
    public PlaceholderEntity(@NotNull String name) {
        super(999, name, AccountType.NON_EXISTENT, 0);
    }
}
