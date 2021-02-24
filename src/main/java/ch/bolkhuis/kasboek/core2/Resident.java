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

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.jetbrains.annotations.NotNull;

public class Resident extends Equity {

    @NotNull
    protected final ReadOnlyDoubleProperty previousBalance;

    /**
     * Default constructor.
     *
     * @param id
     * @param name    the name of this Accounting Entry
     * @param balance the balance of this Accounting Entry
     * @throws IllegalArgumentException if name does not adhere to the contract specified by this class
     */
    public Resident(int id, @NotNull String name, double balance, double previousBalance) throws IllegalArgumentException {
        super(id, name, balance);

        this.previousBalance = new SimpleDoubleProperty(previousBalance);
    }

    /**
     * Returns a new Resident equal to this Resident but with adjusted {@code previousBalance}.
     *
     * @param pb the new previousBalance
     * @return a new Resident
     */
    public Resident setPreviousBalance(double pb) {
        return new Resident(id, getName(), getBalance(), pb);
    }

    @NotNull
    public ReadOnlyDoubleProperty previousBalanceProperty() {
        return previousBalance;
    }
    public final double getPreviousBalance() {
        return previousBalance.get();
    }

    @Override
    Resident debit(double amount) throws IllegalArgumentException {
        return new Resident(id, getName(), debitChange(amount), getPreviousBalance());
    }

    @Override
    Resident credit(double amount) throws IllegalArgumentException {
        return new Resident(id, getName(), creditChange(amount), getPreviousBalance());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) return false;
        return super.equals(o);
    }

}
