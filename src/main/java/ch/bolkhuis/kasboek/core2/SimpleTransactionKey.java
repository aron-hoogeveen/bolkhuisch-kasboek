package ch.bolkhuis.kasboek.core2;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Objects;

public class SimpleTransactionKey implements TransactionKey {

    @NotNull
    private final LocalDate date;
    private final int id;

    public SimpleTransactionKey(@NotNull LocalDate date, int id) {
        Objects.requireNonNull(date);

        this.date = date;
        this.id = id;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public int getId() {
        return id;
    }
}
