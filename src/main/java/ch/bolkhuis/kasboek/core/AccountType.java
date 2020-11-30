package ch.bolkhuis.kasboek.core;

import com.google.gson.annotations.SerializedName;

/**
 * @version v0.2-pre-alpha
 * @author Aron Hoogeveen
 */
public enum AccountType {
    @SerializedName("expense")
    EXPENSE(true, "Expense"),
    @SerializedName("asset")
    ASSET(true, "Asset"),
    @SerializedName("dividend")
    DIVIDEND(true, "Dividend"),
    @SerializedName("liability")
    LIABILITY(false, "Liability"),
    @SerializedName("revenue")
    REVENUE(false, "Revenue"),
    @SerializedName("equity")
    EQUITY(false, "Equity"),
    @SerializedName("nonexistent")
    NON_EXISTENT(true, "Non existent");

    private final boolean debit;
    private final String name;

    AccountType(boolean isDebit, String name) {
        this.debit = isDebit;
        this.name = name;
    }

    /**
     * Returns {@code true} when this AccountType is a debit account, {@code false} when this AccountType is a credit
     * account.
     */
    public boolean isDebit() {
        return debit;
    }


    @Override
    public String toString() {
        return name;
    }
}
