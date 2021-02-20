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

public class Equity extends CreditEntry {

    protected final int id;

    /**
     * Default constructor.
     *
     * @param name    the name of this Accounting Entry
     * @param balance the balance of this Accounting Entry
     * @throws IllegalArgumentException if name does not adhere to the contract specified by this class
     */
    public Equity(int id, @NotNull String name, double balance) throws IllegalArgumentException {
        super(name, balance);

        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Equity equity = (Equity) o;

        return id == equity.id;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        return result;
    }

    @Override
    Equity debit(double amount) throws IllegalArgumentException {
        return new Equity(id, getName(), debitChange(amount));
    }

    @Override
    Equity credit(double amount) throws IllegalArgumentException {
        return new Equity(id, getName(), creditChange(amount));
    }
    
}
