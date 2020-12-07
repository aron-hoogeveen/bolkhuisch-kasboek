/**
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

import org.jetbrains.annotations.NotNull;

public class InmateEntity extends AccountingEntity {
    /**
     * The vale of {@code balance} at the time the last invoice was generated for this InmateEntity
     */
    private final double previousBalance;
    /**
     * Constructs a new AccountingEntry with {@code id} and {@code name}.
     * <br />
     * For the constraints of {@code id} and {@code name} see {@link AccountingEntity#isCorrectId(int)} and
     * {@link AccountingEntity#isCorrectName(String)} respectively. An Inmate is always an {@link AccountType#LIABILITY}.
     *
     * @param id           a unique identifier
     * @param name         the name of this AccountingEntry
     * @param previousBalance the value of endBalance at the time the last invoice was generated for this InmateEntity
     * @param balance   the current balance of this InmateEntity
     * @see AccountingEntity#isCorrectId(int)
     * @see AccountingEntity#isCorrectName(String)
     */
    public InmateEntity(int id, @NotNull String name, double previousBalance, double balance) {
        super(id, name, AccountType.LIABILITY, balance);

        this.previousBalance = previousBalance;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    @Override
    public @NotNull InmateEntity debit(double amount) {
        if (amount < 0) { throw new IllegalArgumentException("You should not debit a negative amount, credit instead"); }

        double balance = (accountType.isDebit()) ? this.balance.get() + amount : this.balance.get() - amount;
        return new InmateEntity(id, name.get(), previousBalance, balance);
    }

    @Override
    public @NotNull InmateEntity credit(double amount) {
        if (amount < 0) { throw new IllegalArgumentException("You should not credit a negative amount, debit instead"); }

        double balance = (accountType.isDebit()) ? this.balance.get() - amount : this.balance.get() + amount;
        return new InmateEntity(id, name.get(), previousBalance, balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        InmateEntity that = (InmateEntity) o;

        return Double.compare(that.previousBalance, previousBalance) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(previousBalance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
