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

import org.jetbrains.annotations.NotNull;

/**
 * The abstract for all Accounting Entries that are of type credit.
 */
public abstract class CreditEntry extends AbstractAccountingEntry {

    /**
     * Default constructor.
     *
     * @param name    the name of this Accounting Entry
     * @param balance the balance of this Accounting Entry
     * @throws IllegalArgumentException if name does not adhere to the contract specified by this class
     */
    public CreditEntry(@NotNull String name, double balance) throws IllegalArgumentException {
        super(name, balance);
    }

    /**
     * Results the new new balance of this DebitEntry if it was credited with {@code amount}.
     *
     * @param amount the amount to credit
     * @return the new balance
     * @throws IllegalArgumentException if the amount is negative
     */
    @Override
    protected double creditChange(double amount) throws IllegalArgumentException {
        if (amount < 0)
            throw new IllegalArgumentException();

        return getBalance() + amount;
    }

    /**
     * Results the new new balance of this DebitEntry if it was debited with {@code amount}.
     *
     * @param amount the amount to debit
     * @return the new balance
     * @throws IllegalArgumentException if the amount is negative
     */
    @Override
    protected double debitChange(double amount) throws IllegalArgumentException {
        if (amount < 0)
            throw new IllegalArgumentException();

        return getBalance() - amount;
    }

}
