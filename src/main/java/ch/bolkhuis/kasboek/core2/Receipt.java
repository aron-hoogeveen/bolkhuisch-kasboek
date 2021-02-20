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

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;
import java.util.Objects;

public class Receipt {

    @NotNull
    private final LocalDate date;
    private final int id;
    @NotNull
    private final String description;
    @NotNull
    private ObservableSet<Integer> transactionIdSet; // FIXME Transactions should be recognizable by a (LocalDate, int) tuple
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
     * @param transactionIdSet a set containing the ids of the transactions that belong to this receipt
     * @param payer the entry that payed for this receipt
     * @throws IllegalArgumentException if {@code transactionIdSet} or {@code description} does not adhere to the contract
     * @see StringUtils#checkString
     */
    public Receipt(@NotNull LocalDate date, int id, @NotNull String description, @NotNull Set<Integer> transactionIdSet,
                   int payer) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(date);
        Objects.requireNonNull(description);
        Objects.requireNonNull(transactionIdSet);

        if (!(checkTransactionIdSet(transactionIdSet) && StringUtils.checkString(description)))
            throw new IllegalArgumentException();

        this.date = date;
        this.id = id;
        this.description = description;
        this.transactionIdSet = FXCollections.observableSet(new TreeSet<>(transactionIdSet));
        this.payer = payer;
    }

    /**
     * Checks whether or not the provided {@code set} adheres to the contract specified by this class.
     *
     * More specifically, the set must not be null or contain any null values.
     *
     * @param set the Set to check
     * @return {@code true} if the set adheres to the contract, {@code false} otherwise
     */
    public static boolean checkTransactionIdSet(@Nullable Set<Integer> set) {
        if (set == null) return false;

        return !set.contains(null);
    }
}
