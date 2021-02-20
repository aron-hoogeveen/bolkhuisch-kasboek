package ch.bolkhuis.kasboek.core2;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.time.LocalDate;

public class Transaction implements Comparable<Transaction> {

    @NotNull
    private final LocalDate date;
    private final int id;
    private final int debtorId;
    private final int creditorId;
    private final double amount;
    @NotNull
    private final SimpleStringProperty description;
    @Nullable
    private final Integer receiptId;

    private Transaction() { throw new UnsupportedOperationException(); }

    /**
     * Returns a new Transaction with the specified field values.
     *
     * @param date the date this transaction occurred on
     * @param id the unique id to distinguish this transaction from other transactions with the same date
     * @param debtorId the id of the debtor
     * @param creditorId the id of the creditor
     * @param amount the amount
     * @param description a descriptive string
     * @param receiptId the id of the receipt this transaction is tied to. Can be null
     * @throws IllegalArgumentException if {@code amount} or {@code description} does not adhere to the contracts this
     *          class specifies for the fields
     * @throws NullPointerException if {@code date} or {@code description} is null
     */
    public Transaction(@NotNull LocalDate date, int id, int debtorId, int creditorId, double amount,
                       @NotNull String description, @Nullable Integer receiptId) throws IllegalArgumentException {
        Objects.requireNonNull(date);
        Objects.requireNonNull(description);

        // clean the description
        description = cleanDescription(description);

        // check the field contracts specified by this class
        if (!checkAmount(amount) || !checkDescription(description))
            throw new IllegalArgumentException();

        this.date = date;
        this.id = id;
        this.debtorId = debtorId;
        this.creditorId = creditorId;
        this.amount = amount;
        this.description = new SimpleStringProperty(description);
        this.receiptId = receiptId;
    }

    @NotNull
    public LocalDate getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public int getDebtorId() {
        return debtorId;
    }

    public int getCreditorId() {
        return creditorId;
    }

    public double getAmount() {
        return amount;
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
     * Changes the current {@code description} of this Transaction.
     *
     * @param str the new description
     * @throws IllegalArgumentException if {@code str} does not adhere to the description contract of this class
     * @throws NullPointerException if {@code str} is null
     */
    public void setDescription(@NotNull String str) throws IllegalArgumentException {
        Objects.requireNonNull(str);

        str = cleanDescription(str);
        if (!checkDescription(str))
            throw new IllegalArgumentException();

        description.set(str);
    }

    /**
     * Returns the String representation of the {@code date} of this Transaction formatted as "dd-MM-uuuu".
     *
     * @return formatted date
     * @see DateTimeFormatter#ofPattern(String)
     */
    @NotNull
    public String getDateString() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
    }

    @Nullable
    public Integer getReceiptId() {
        return receiptId;
    }

    /**
     * Checks if the provided double adheres to the contract specified by this method.
     * A valid amount should be greater than 0.
     *
     * @param d the double to check
     * @return {@code true} if {@code d} adheres to the contract, {@code false} otherwise
     */
    public static boolean checkAmount(double d) {
        return (d >= 0.005); // 0.005 will be equal to 1 cents, so will be treated as greater than zero
    }

    /**
     * Checks if the provided String adheres to the contract specified by this method.
     * A valid description has a minimum length of 5 and does not begin or end with whitespaces.
     *
     * @param str the String to check
     * @return {@code true} if {@code str} adheres to the contract, {@code false} otherwise
     */
    public static boolean checkDescription(@Nullable String str) {
        if (str == null) return false;

        String tmp = str.strip();
        if (tmp.length() != str.length()) return false;

        return str.length() >= 5;
    }

    /**
     * Cleans the provided String for use as {@code description} of a Transaction.
     * The current implementation of this class only {@code strip}s the String.
     *
     * @param str the String to clean
     * @return a cleaned version of {@code str}
     * @throws NullPointerException if {@code str} is null
     */
    public static String cleanDescription(@NotNull String str) {
        Objects.requireNonNull(str);

        return str.strip();
    }

    /**
     * Returns a Transaction with the same field values, except for the {@code receiptId} field
     * which is set to null.
     *
     * @return a new Transaction
     */
    @NotNull
    public Transaction clearReceiptId() {
        return new Transaction(date, id, debtorId, creditorId, amount, getDescription(), null);
    }

    @Override
    public String toString() {
        return getDescription();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!date.equals(that.date)) return false;
        if (id != that.id) return false;
        if (debtorId != that.debtorId) return false;
        if (creditorId != that.creditorId) return false;
        if (Double.compare(that.amount, amount) != 0) return false;
        if (!getDescription().equals(that.getDescription())) return false;
        return receiptId != null ? receiptId.equals(that.receiptId) : that.receiptId == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = date.hashCode();
        result = 31 * result + id;
        result = 31 * result + debtorId;
        result = 31 * result + creditorId;
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + (receiptId != null ? receiptId.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(@NotNull Transaction o) {
        Objects.requireNonNull(o);

        // date
        if (this.date.isBefore(o.date)) {
            return -1;
        } else if (this.date.isAfter(o.date)) {
            return 1;
        }

        // id
        if (this.id < o.id) {
            return -1;
        } else if (this.id > o.id) {
            return 1;
        }

        //
        if (this.receiptId == null) {
            if (o.receiptId != null) {
                return -1;
            }
        } else if (o.receiptId == null) {
            return 1;
        } else if (this.receiptId < o.receiptId) {
            return -1;
        } else if (this.receiptId > o.receiptId) {
            return 1;
        }

        // debtorId
        if (this.debtorId < o.debtorId) {
            return -1;
        } else if (this.debtorId > o.debtorId) {
            return 1;
        }

        // creditorId
        if (this.creditorId < o.creditorId) {
            return -1;
        } else if (this.creditorId > o.creditorId) {
            return 1;
        }

        // amount
        if (this.amount < o.amount) {
            return -1;
        } else if (this.amount > o.amount) {
            return 1;
        }

        // Equal amounts. Check description
        return this.getDescription().compareTo(o.getDescription());
    }

    // TODO add toJson and fromJson methods

}
