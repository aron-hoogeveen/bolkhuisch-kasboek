/*
 * Copyright (C) 2021 Aron Hoogeveen
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

package ch.bolkhuis.kasboek.core2;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;
import java.util.Objects;

/**
 * This class makes no guarantees as to that all transactionKeys in the {@code transactionKeySet} point to Transactions
 * that exist, or that it points to Transactions for which holds that either the debtorId or the creditorId points to
 * the same id as the field {@code payer}. Classes that encapsulate this class should ensure that those properties hold
 * (or at least that either the debtorId or the creditorId is the same as field {@code payer}).
 */
public class Receipt implements ReceiptKey {

    @NotNull
    private final LocalDate date;
    private final int id;
    @NotNull
    private final SimpleStringProperty description;
    @NotNull
    private final ObservableSet< @NotNull TransactionKey> transactionKeySet = FXCollections.observableSet(new TreeSet<>());
    private final int payer;

    /**
     * Returns a new Receipt with the specified field values.<br />
     * <br />
     * It is required that the {@code transactionIdSet} does not contain any null values. This constructor creates a new
     * ObservableSet of the values of the provided {@code transactionIdSet} that is backed by a TreeSet, so that the set
     * is ordered using their natural ordering.<br />
     *
     * @param date the date of this receipt
     * @param id a unique id to differentiate other receipts with the same date
     * @param description a descriptive string
     * @param transactionKeySet a set containing the ids of the transactions that belong to this receipt
     * @param payer the entry that payed for this receipt
     * @throws IllegalArgumentException if {@code transactionIdSet} or {@code description} does not adhere to the contract
     * @see StringUtils#checkString
     */
    public Receipt(@NotNull LocalDate date, int id, @NotNull String description, @NotNull Set<TransactionKey> transactionKeySet,
                   int payer) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(date);
        Objects.requireNonNull(description);
        Objects.requireNonNull(transactionKeySet);

        if (!(checkTransactionIdSet(transactionKeySet) && StringUtils.checkString(description)))
            throw new IllegalArgumentException();

        this.date = date;
        this.id = id;
        this.description = new SimpleStringProperty(description);
        // do not parse the transactionKeySet directly into the constructor, because we want the natural ordering of the elements and not the ordering specified by transactionKeySet (see constructors documentation of TreeSet)
        this.transactionKeySet.addAll(transactionKeySet);
        this.payer = payer;
    }

    @Override
    @NotNull
    public LocalDate getDate() {
        return date;
    }

    @Override
    public int getId() {
        return id;
    }

    @NotNull
    public ReadOnlyStringProperty descriptionProperty() {
        return description;
    }
    @NotNull
    public String getDescription() {
        return description.get();
    }

    /**
     * Sets the description of this Receipt to the new String.
     *
     * @param str the new description
     * @throws IllegalArgumentException if {@code str} does not adhere to the contract specified by this class
     * @throws NullPointerException if {@code str} is null
     */
    public void setDescription(@NotNull String str) {
        Objects.requireNonNull(str);

        if (!StringUtils.checkString(str))
            throw new IllegalArgumentException();

        description.set(str);
    }

    /**
     * Returns an unmodifiable version of the {@code transactionIdSet}.
     *
     * @return the {@code transactionIdSet}
     */
    @NotNull
    public ObservableSet<TransactionKey> getTransactionKeySet() {
        return FXCollections.unmodifiableObservableSet(transactionKeySet);
    }

    public int getPayer() {
        return payer;
    }

    /**
     * Adds a TransactionKey to the set of TransactionKeys.
     *
     * @param key the key to add
     * @return {@code true} if the set did not already contain the key
     */
    public boolean addTransactionKey(TransactionKey key) {
        return transactionKeySet.add(key);
    }

    /**
     * Removes the TransactionKey from the set of TransactionKeys.
     *
     * @param key the key to remove
     * @return {@code true} if the removal was successful
     */
    public boolean removeTransactionKey(TransactionKey key) {
        return transactionKeySet.remove(key);
    }

    /**
     * Returns whether or not the set of TransactionKeys contains the provided key.
     *
     * @param key the key to check
     * @return {@code true} if the set contains the key
     */
    public boolean containsTransactionKey(TransactionKey key) {
        return transactionKeySet.contains(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receipt receipt = (Receipt) o;

        if (id != receipt.id) return false;
        if (payer != receipt.payer) return false;
        if (!date.equals(receipt.date)) return false;
        if (!getDescription().equals(receipt.getDescription())) return false;
        return transactionKeySet.equals(receipt.transactionKeySet);
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + id;
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + transactionKeySet.hashCode();
        result = 31 * result + payer;
        return result;
    }

    /**
     * Checks whether or not the provided {@code set} adheres to the contract specified by this class.
     *
     * More specifically, the set must not be null or contain any null values.
     *
     * @param set the Set to check
     * @return {@code true} if the set adheres to the contract, {@code false} otherwise
     */
    public static boolean checkTransactionIdSet(@Nullable Set<TransactionKey> set) {
        if (set == null) return false;

        return !set.contains(null);
    }

}
