package ch.bolkhuis.kasboek.core;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * Class Receipt holds a collection of Transaction ids from related Transactions.
 *
 * @author Aron Hoogeveen
 * @version 0.2-pre-alpha
 */
public class Receipt {
    private final int id;
    private @NotNull final String name;
    private @NotNull final Set<Integer> transactionIdSet;
    private final @NotNull LocalDate date;
    private final int payer;

    /**
     * Creates a Receipt. Fail-fast.
     * @param id the identifier
     * @param name the name
     * @param transactionIdSet a Set with integers that correspond to related Transaction ids
     * @param date the date this receipt 'happened'
     */
    public Receipt(int id, @NotNull String name, @NotNull Set<Integer> transactionIdSet, @NotNull LocalDate date, int payer) {
        Objects.requireNonNull(name, "Parameter name cannot be null");
        Objects.requireNonNull(transactionIdSet, "Parameter transactionIdSet cannot be null");
        Objects.requireNonNull(date, "Parameter date cannot be null");

        this.id = id;
        this.name = name;
        this.transactionIdSet = transactionIdSet;
        this.date = date;
        this.payer = payer;
    }

    public int getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Set<Integer> getTransactionIdSet() {
        return new HashSet<>(transactionIdSet);
    }

    public @NotNull LocalDate getDate() {
        return date;
    }

    public int getPayer() {
        return payer;
    }

    @Override
    public String toString() {
        return name + " (" + date + ")";
    }
}
