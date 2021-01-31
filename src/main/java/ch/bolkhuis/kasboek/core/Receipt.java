/*
 * Copyright (C) 2020 Aron Hoogeveen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ch.bolkhuis.kasboek.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class Receipt holds a collection of Transaction ids from related Transactions.
 *
 * TODO add field that indicates that the payer is already invoiced for this Receipt. If the field is true, than this Receipt cannot be edited
 */
public class Receipt {
    private final int id;
    private @NotNull final String name;
    private @NotNull final ObservableSet<Integer> transactionIdSet;
    private final @NotNull LocalDate date;
    private final int payer;

    /**
     * Creates a Receipt. Fail-fast. This method copies the provided transactionIdSet to a modifiable
     * set, to ensure that the ObservableSet remains modifiable.
     *
     * @param id the identifier
     * @param name the name
     * @param transactionIdSet a Set with integers that correspond to related Transaction ids // FIXME bug, user can use an unmodifiable set as backing set. Ensure modifiable sets are provided!
     * @param date the date this receipt 'happened'
     */
    public Receipt(int id, @NotNull String name, Set<Integer> transactionIdSet, @NotNull LocalDate date, int payer) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(date);

        this.id = id;
        this.name = name;
        if (transactionIdSet == null) {
            this.transactionIdSet = FXCollections.observableSet(new HashSet<>());
        } else {
            // ensure the set is modifiable
            this.transactionIdSet = FXCollections.observableSet(new HashSet<>(transactionIdSet));
        }
        this.date = date;
        this.payer = payer;
    }

    public int getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable version of the observable Transaction Set.
     *
     * @return unmodifiable observable set
     */
    @NotNull
    public ObservableSet<Integer> getTransactionIdSet() {
        return FXCollections.unmodifiableObservableSet(transactionIdSet);
    }

    @NotNull
    public LocalDate getDate() {
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

    /**
     * Unregisters the transaction with id {@code transactionId} from this Receipt.
     *
     * @param transactionId the id of the Transaction to unregister
     * @return true if the set contained the id
     */
    public boolean unregisterTransaction(int transactionId) {
        return transactionIdSet.remove(transactionId);
    }

    /**
     * Returns whether {@code transactionIdSet} contains the id.
     *
     * @param transactionId the id to check
     * @return whether the set contains the id
     */
    public boolean containsTransactionId(int transactionId) {
        return transactionIdSet.contains(transactionId);
    }

    @Override
    public String toString() {
        return name + " (" + date + ")";
    }
}
