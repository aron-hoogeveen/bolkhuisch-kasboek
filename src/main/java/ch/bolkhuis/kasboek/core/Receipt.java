package ch.bolkhuis.kasboek.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * Class Receipt holds a collection of Transaction ids from related Transactions.
 *
 * TODO add field that indicates that the payer is already invoiced for this Receipt. If the field is true, than this Receipt cannot be edited
 *
 * @author Aron Hoogeveen
 * @version 0.2-pre-alpha
 */
public class Receipt {
    private final int id;
    private @NotNull final String name;
    private @NotNull ObservableSet<Integer> transactionIdSet;
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
        this.transactionIdSet = FXCollections.observableSet(transactionIdSet);
        this.date = date;
        this.payer = payer;
    }

    public int getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull ObservableSet<Integer> getTransactionIdSet() {
        return transactionIdSet;
    }

    public @NotNull LocalDate getDate() {
        return date;
    }

    public int getPayer() {
        return payer;
    }

    /**
     * Returns {@code true} if the set did not already contain the transaction id.
     *
     * @param transactionId the id of the Transaction to register with this Receipt
     * @return {@code true} if the set did not already contain this Transaction
     */
    public boolean registerTransaction(int transactionId) {
        return transactionIdSet.add(transactionId);
    }

    @Override
    public String toString() {
        return name + " (" + date + ")";
    }
}
