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

import ch.bolkhuis.kasboek.gson.CustomizedGson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * The immutable class AccountingEntry resembles an account with value. Some fields adhere to the contracts specified
 * in the functions isCorrectXXX() where XXX is equal to the fields' name.
 * FIXME correctly handle Double inf/NaN
 */
public class AccountingEntity {
    protected final int id;
    protected  @NotNull final ReadOnlyStringProperty name;
    protected  @NotNull final AccountType accountType;
    protected final ReadOnlyDoubleProperty balance;
    /**
     * Constructs a new AccountingEntry with {@code id} and {@code name}.
     * <br />
     * For the constraints of {@code name} see {@link AccountingEntity#isCorrectName(String)}.
     *
     * @param id a unique identifier
     * @param name the name of this AccountingEntry
     * @param accountType the AccountType of this AccountingEntity
     * @param balance the current balance
     * @see AccountingEntity#isCorrectName(String)
     */
    public AccountingEntity(int id, @NotNull String name, @NotNull AccountType accountType, double balance) {
        Objects.requireNonNull(name, "Parameter name must not be null");
        Objects.requireNonNull(accountType, "Parameter accountType must not be null");

        if (!isCorrectName(name)) {
            throw new IllegalArgumentException("Illegal name");
        }

        // Strip the string of the whitespaces before setting
        this.id = id;
        this.name = new SimpleStringProperty(name.strip());
        this.accountType = accountType;
        this.balance = new SimpleDoubleProperty(balance);
    }

    public int getId() {
        return id;
    }

    public final ReadOnlyStringProperty nameProperty() { return name; }
    public String getName() { return name.get(); }

    public final ReadOnlyDoubleProperty balanceProperty() { return balance; }
    public double getBalance() { return balance.get(); }

    @NotNull
    public AccountType getAccountType() {
        return accountType;
    }

    /**
     * Returns a new AccountingEntity with {@code amount} added to the balance of this AccountingEntity.
     *
     * @deprecated use {@link AccountingEntity#debit(double)} and {@link AccountingEntity#credit(double)} instead
     * @param amount the amount to add to balance. Could be negative
     * @return new AccountingEntity with the added balance
     */
    @NotNull
    @Deprecated
    public AccountingEntity addBalance(double amount) {

        return new AccountingEntity(id, name.get(), accountType, balance.get() + amount);
    }

    /**
     * Returns a duplicate of this AccountingEntity after debiting {@code amount}.
     *
     * @param amount the amount to debit
     * @return duplicate of AccountingEntity
     */
    public @NotNull AccountingEntity debit(double amount) {
        if (amount < 0) { throw new IllegalArgumentException("You should not debit a negative amount, credit instead"); }

        double newEndBalance = (accountType.isDebit()) ? this.balance.get() + amount : this.balance.get() - amount;
        return new AccountingEntity(id, name.get(), accountType, newEndBalance);
    }

    /**
     * Returns a duplicate of this AccountingEntity after crediting {@code amount}.
     *
     * @param amount the amount to credit
     * @return duplicate of AccountingEntity
     */
    public @NotNull AccountingEntity credit(double amount) {
        if (amount < 0) { throw new IllegalArgumentException("You should not credit a negative amount, debit instead"); }

        double newEndBalance = (accountType.isDebit()) ? this.balance.get() - amount : this.balance.get() + amount;
        return new AccountingEntity(id, name.get(), accountType, newEndBalance);
    }

    /**
     * Calculate the change in balance when {@code amount} is debited on this AccountingEntity.
     *
     * @param amount the amount to debit
     * @return the balance change
     */
    public double debitBalanceChange(double amount) {
        if (amount < 0) { throw new IllegalArgumentException("You should not debit a negative amount"); }
        return (accountType.isDebit() ? amount : -1 * amount);
    }

    /**
     * Calculate the change in balance when {@code amount} is credited on this AccountingEntity.
     *
     * @param amount the amount to credit
     * @return the balance change
     */
    public double creditBalanceChange(double amount) {
        if (amount < 0) { throw new IllegalArgumentException("You should not credit a negative amount"); }
        return (accountType.isDebit() ? -1 * amount : amount);
    }

    /**
     * @see com.google.gson.Gson#toJson(Object, Type)
     */
    public static String toJson(AccountingEntity accountingEntity) {
        return CustomizedGson.gson.toJson(accountingEntity, AccountingEntity.class);
    }

    /**
     * Returns a new AccountingEntity that is compatible with the JSON returned from {@link AccountingEntity#toJson(AccountingEntity)}.
     *
     * @param reader input Reader
     * @return new AccountingEntity
     * @throws JsonSyntaxException see GSON docs
     * @throws JsonIOException see GSON docs
     */
    public static AccountingEntity fromJson(@NotNull Reader reader) {
        Objects.requireNonNull(reader, "Parameter reader cannot be null");

        BufferedReader bufferedReader = new BufferedReader(reader);
        return CustomizedGson.gson.fromJson(bufferedReader, AccountingEntity.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountingEntity that = (AccountingEntity) o;

        if (id != that.id) return false;
        if (Double.compare(that.balance.get(), balance.get()) != 0) return false;
        if (!name.get().equals(that.name.get())) return false;
        return accountType == that.accountType;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + name.get().hashCode();
        result = 31 * result + accountType.hashCode();
        temp = Double.doubleToLongBits(balance.get());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Returns whether the provided name is a correct name for constructing a new AccountingEntity. Name constraints are:<br />
     * - Not null;<br />
     * - Not empty;<br />
     * - Not whitespace only;<br />
     * - Max length of 256 characters;<br />
     * - Does not contain newline characters.<br />
     * - Is all alphabetical.<br />
     *
     * @param name the name to check
     * @return true if this is a correct name, false otherwise
     */
    public static boolean isCorrectName(String name) {
        return (name != null) && (!name.isBlank()) && (name.strip().length() <= 256) && (!name.contains("\n"))
                && (!name.contains("\r")) && (name.strip().matches("[a-zA-Z ]*"));
    }

    /**
     * Returns the name of this AccountingEntity.
     */
    @Override
    public String toString() {
        return name.get();
    }
}
