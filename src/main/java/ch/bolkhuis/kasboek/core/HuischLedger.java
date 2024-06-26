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

import ch.bolkhuis.kasboek.eventlisteners.ReceiptEvent;
import ch.bolkhuis.kasboek.eventlisteners.ReceiptEventListener;
import ch.bolkhuis.kasboek.exceptions.IllegalTemplateFormatException;
import ch.bolkhuis.kasboek.exceptions.UnsupportedVersionException;
import ch.bolkhuis.kasboek.gson.CustomizedGson;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * Class HuischLedger is a Ledger that has extra functionality to meet the demands of the Bolkhuisch. AccountingEntities
 * with negative ids are reserved by this class. It is possible to create AccountingEntities with negative ids, but if
 * that id collides with one of the reserved ids of this class that AccountingEntity will be overwritten at some point
 * without giving feedback.
 *
 * @author Aron Hoogeveen
 * @version 0.2-pre-alpha
 */
public final class HuischLedger extends Ledger {
    private final static String introText = "Please replace this string with some info text";

    private final static int placeholderEntityId = -1;

    private final ObservableMap<Integer, Receipt> receipts;
    private int nextReceiptId = 0; // FIXME add initializers, update typeadapter,

    public HuischLedger() {
        this.receipts = FXCollections.observableHashMap();
    }

    public HuischLedger(@NotNull Ledger old) {
        super(old);
        this.receipts = FXCollections.observableHashMap();
    }

    public HuischLedger(@NotNull ObservableMap<Integer, AccountingEntity> accountingEntities) {
        super(accountingEntities);
        this.receipts = FXCollections.observableHashMap();
    }

    public HuischLedger(@NotNull ObservableMap<Integer, AccountingEntity> accountingEntities,
                        @NotNull ObservableMap<Integer, Transaction> transactions) {
        super(accountingEntities, transactions);
        this.receipts = FXCollections.observableHashMap();
    }

    public HuischLedger(@NotNull ObservableMap<Integer, AccountingEntity> accountingEntities,
                        @NotNull ObservableMap<Integer, Transaction> transactions,
                        @NotNull ObservableMap<Integer, Receipt> receipts) {
        super(accountingEntities, transactions);
        this.receipts = Objects.requireNonNull(receipts, "Parameter receipts cannot be null");
    }

    /**
     * Generates an invoice for an InmateEntity with id {@code inmateEntityId} and writes it to the file {@code out}.
     * Transactions that belong to a Receipt are grouped together and shown as one on the Invoice. The counter-entity
     * for the Receipts will the denoted as "various" (or the locale different version from the provided resource file)
     * <br />
     * <br />
     * This method uses template version 1.
     *
     * @param out the output file
     * @param template the template for the invoice
     * @param huischLedger the huischLedger containing the AccountingEntity
     * @param inmateEntityId the id of the AccountingEntity to generate an invoice for
     * @param resourceBundle the ResourceBundle for supplying Locale specific Strings
     * @throws IOException when there are problems reading/writing the Files
     * @throws UnsupportedVersionException when the template version is not supported
     */
    private static void generateInmateInvoice(@NotNull File out, @NotNull File template, @NotNull HuischLedger huischLedger,
                                              int inmateEntityId, @NotNull LocalDate from,
                                              @NotNull LocalDate to, @NotNull ResourceBundle resourceBundle)
            throws IOException {
        Objects.requireNonNull(out, "Parameter out cannot be null");
        Objects.requireNonNull(template, "Parameter template cannot be null");
        Objects.requireNonNull(huischLedger, "Parameter huischLedger cannot be null");
        Objects.requireNonNull(from, "Parameter from cannot be null");
        Objects.requireNonNull(to, "Parameter to cannot be null");
        Objects.requireNonNull(resourceBundle, "Parameter resourceBundle cannot be null");

        // Check that the provided inmateEntityId matches an AccountingEntity in the Ledger
        AccountingEntity accountingEntity = huischLedger.getAccountingEntityById(inmateEntityId);
        if (accountingEntity == null) {
            throw new IllegalArgumentException("The provided inmateEntityId has not corresponding AccountingEntity in the provided Ledger");
        }

        // Check that the provided Entity is an InmateEntity
        if (!(accountingEntity instanceof InmateEntity)) {
            throw new IllegalArgumentException("The provided inmateEntityId does not belong to an inmate");
        }
        InmateEntity inmateEntity = (InmateEntity) accountingEntity;

        // Update the string for the placeholderEntity
        huischLedger.accountingEntities.put(placeholderEntityId, new PlaceholderEntity(resourceBundle.getString("various")));


        // TODO change the used currency based on the provided Locale
        Currency currency = Currency.getInstance("EUR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.GERMAN);
        numberFormat.setCurrency(currency);

        // Compatible template version(s)
        String templateVersionString = "<!-- version:1 -->";

        String templateString;
        // Read in the template
        try (BufferedReader reader = new BufferedReader(new FileReader(template))) {
            // make sure compatible versions are being provided
            String fileVersion = reader.readLine();
            if (!fileVersion.equals(templateVersionString)) {
                throw new UnsupportedVersionException("Version \"" + fileVersion + "\" is not supported");
            }

            StringBuilder templateStringBuilder = new StringBuilder();
            reader.lines().forEach((s) -> templateStringBuilder.append(s).append("\n"));
            templateString = templateStringBuilder.toString();

            // Make sure all fields are in the template
            List<String> fields = List.of(
                    "${NAME}",
                    "${INTRO_TEXT}",
                    "${START_BALANCE}",
                    "${END_BALANCE}",
                    "${TABLE_DATA}"
            );
            String finalTemplateString = templateString;
            fields.forEach(e -> {
                if (!finalTemplateString.contains(e)) {
                    throw new IllegalTemplateFormatException("Field '" + e + "' is missing from the provided template file");
                }
                if (StringUtils.countMatches(finalTemplateString, e) > 1) {
                    throw new IllegalTemplateFormatException("Field '" + e + "' is declared too many times");
                }
            });

            // Get all transactions
            Set<Transaction> standAloneTransactionsSet = huischLedger.getTransactionsOf(inmateEntityId, from, to);
            Set<Transaction> standAloneTransactionsSetCopy = new HashSet<>(standAloneTransactionsSet);
//            TreeMap<Integer, Transaction> copyOfStandAloneTransactions = new TreeMap<>(standAloneTransactions);

            // Separate the Transactions that belong to a Receipt from the stand-alone Transactions
            TreeMap<Integer, List<Transaction>> receiptIdToTransactionList = new TreeMap<>();
            standAloneTransactionsSetCopy.forEach(transaction -> {
                huischLedger.receipts.forEach((r_id, receipt) -> {
                    // If there is no list of transactions for this receipt, initialize an empty list
                    receiptIdToTransactionList.computeIfAbsent(r_id, k -> new ArrayList<>());
                    if (receipt.getTransactionIdSet().contains(transaction.getId())) {
                        // Add the transaction to its correct receiptIdToTransactionList mapping
                        receiptIdToTransactionList.computeIfPresent(r_id, (integer, transactions) -> {
                            // Removes the transaction from the stand-alone list and adds it to its corresponding receipt
                            standAloneTransactionsSet.remove(transaction);
                            transactions.add(transaction);
                            return transactions;
                        });
                    }
                });
            });

            // Create stand-alone transactions for the grouped transactions per receipt
            receiptIdToTransactionList.forEach((r_id, transactions) -> {
                // FIXME make receipts getting more efficient by passing it higher up the method maybe?
                Receipt receipt = huischLedger.receipts.get(r_id);
                // Only create the representing transaction if the current accountingEntity is the payer
                if (receipt.getPayer() == accountingEntity.getId()) {
                    // create a stand-alone Transaction that resembles the result of all the transactions
                    // Calculate the resulting balance change
                    double balanceChange = 0;
                    for (Transaction t : transactions) {
                        if (t.getDebtorId() == accountingEntity.getId()) {
                            balanceChange += accountingEntity.debitBalanceChange(t.getAmount());
                        } else {
                            assert accountingEntity.getId() == t.getCreditorId() : "We should only have transactions that are connected to the accountinEntity";
                            // creditor
                            balanceChange += accountingEntity.creditBalanceChange(t.getAmount());
                        }
                    }

                    // fix the reversing of the amount on the invoice later on at the amountString
                    int debtorId, creditorId;
                    if (balanceChange < 0 && accountingEntity.accountType.isDebit()) {
                        debtorId = accountingEntity.getId();
                        creditorId = placeholderEntityId;
                    } else {
                        debtorId = placeholderEntityId;
                        creditorId = accountingEntity.getId();
                    }


                    // Create the transaction representing the receipt and add it to the stand-alone transactions
                    standAloneTransactionsSet.add(
                            new Transaction(
                                    -666,
                                    debtorId,
                                    creditorId,
                                    balanceChange,
                                    receipt.getDate(),
                                    receipt.getName()));
                }
            });

            // Generate the TABLE_DATA from the transactions
            StringBuilder tableDateStringBuilder = new StringBuilder();
            standAloneTransactionsSet.forEach((t) -> {
                if (t == null) { return; }
                // Get the counter-entity.
                int counterEntityId = (t.getDebtorId() == inmateEntityId) ? t.getCreditorId() : t.getDebtorId();
                AccountingEntity counterEntity = huischLedger.getAccountingEntityById(counterEntityId);

                // Use an empty string if the counterEntity is not available
                String counterEntityName = (counterEntity == null) ? "" : counterEntity.getName();

                // Format the amount (amount is larger than or equal to zero)
                double amount = t.getAmount();
                String amountString;

                if ((inmateEntity.getAccountType().isDebit() && t.getDebtorId() == inmateEntityId)
                        || (!inmateEntity.getAccountType().isDebit() && t.getCreditorId() == inmateEntityId)) {
                    amountString = numberFormat.format(amount);
                } else {
                    amountString = numberFormat.format(-1 * amount);
                }

                // FIXME implement the rest of this function

                // Add a table row for this transaction
                tableDateStringBuilder
                        .append("\t\t<tr>\n")
                        .append("\t\t\t<td>\n")
                        .append("\t\t\t\t")
                        .append(t.getDateString())
                        .append("\n\t\t\t</td>\n")
                        .append("\t\t\t<td>\n")
                        .append("\t\t\t\t")
                        .append(counterEntityName)
                        .append("\n\t\t\t</td>\n")
                        .append("\t\t\t<td>\n")
                        .append("\t\t\t\t")
                        .append(t.getDescription())
                        .append("\n\t\t\t</td>\n")
                        .append("\t\t\t<td>\n")
                        .append("\t\t\t\t")
                        .append(amountString)
                        .append("\n\t\t\t</td>\n")
                        .append("\t\t</tr>\n");
            });

            // Populate field ${NAME}
            templateString = StringUtils.replace(templateString, "${NAME}", inmateEntity.getName());
            templateString = StringUtils.replace(templateString, "${INTRO_TEXT}", introText);
            templateString = StringUtils.replace(templateString, "${START_BALANCE}", numberFormat.format(inmateEntity.getPreviousBalance()));
            templateString = StringUtils.replace(templateString, "${END_BALANCE}", numberFormat.format(inmateEntity.getBalance()));
            templateString = StringUtils.replace(templateString, "${TABLE_DATA}", tableDateStringBuilder.toString());
        }

        // Write to the out file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {
            writer.write(templateString);
        }
    }

    /**
     * Creates an invoice using the default ResourceBundle of this class ( ).
     *
     * @param out the output file
     * @param template the template for the invoice
     * @param ledger the ledger containing the AccountingEntity
     * @param inmateEntityId the id of the AccountingEntity to generate an invoice for
     * @throws IOException when there are problems reading/writing the Files
     * @throws UnsupportedVersionException when the template version is not supported
     */
    public static void generateInmateInvoice(@NotNull File out, @NotNull File template, @NotNull HuischLedger ledger,
                                             int inmateEntityId, @NotNull LocalDate from, @NotNull LocalDate to) throws IOException {
        ResourceBundle defaultInvoiceResourceBundle = ResourceBundle.getBundle("HuischInvoiceStrings");
        generateInmateInvoice(out, template, ledger, inmateEntityId, from, to, defaultInvoiceResourceBundle);
    }

    /**
     * Creates an invoice using the supplied {@code locale} to select the appropriate ResourceBundle
     * @param out the output file
     * @param template the template for the invoice
     * @param ledger the ledger containing the AccountingEntity
     * @param inmateEntityId the id of the AccountingEntity to generate an invoice for
     * @param locale the Locale to use
     * @throws IOException when there are problems reading/writing the Files
     * @throws UnsupportedVersionException when the template version is not supported
     * @throws MissingResourceException when no resource if found for the requested Locale
     */
    public static void generateInmateInvoice(@NotNull File out, @NotNull File template, @NotNull HuischLedger ledger,
                                             int inmateEntityId, @NotNull LocalDate from,
                                             @NotNull LocalDate to, @NotNull Locale locale) throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("HuischInvoiceStrings", locale);
        generateInmateInvoice(out, template, ledger, inmateEntityId, from, to, resourceBundle);
    }

    public @Deprecated TreeMap<Integer, Receipt> copyOfReceipts() {
        return new TreeMap<>(receipts);
    }

    /**
     * Returns an unmodifiable version of the ObservableMap of Receipts.
     *
     * @return unmodifiable map of Receipts
     */
    public ObservableMap<Integer, Receipt> getReceipts() {
        return FXCollections.unmodifiableObservableMap(receipts);
    }

    public int getAndIncrementNextReceiptId() {
        return nextReceiptId++;
    }

    public void addReceipt(@NotNull Receipt receipt) {
        Objects.requireNonNull(receipt, "Parameter receipt cannot be null");

        if (receipts.containsKey(receipt.getId())) {
            throw new IllegalArgumentException("Duplicate key for new receipt");
        }
        // TODO should we register the receipt with the transactions that do not have the correct receiptId?
        receipts.put(receipt.getId(), receipt);
    }

    /**
     * Returns a JSON string that is parsable by {@link Ledger#fromJson(Reader)}.
     *
     * @param ledger the Ledger to convert to JSON
     * @return a JSON string representing {@code ledger}
     */
    public static String toJson(HuischLedger ledger) {
        return CustomizedGson.gson.toJson(ledger, HuischLedger.class);
    }

    /**
     * Returns a new Ledger from a {@code reader} supplying a JSON String as generated by {@link Ledger#toJson(Ledger)}.
     *
     * @param reader the reader providing the JSON String
     * @return a Ledger as represented by the JSON String
     * @throws com.google.gson.JsonSyntaxException see GSON docs
     * @throws com.google.gson.JsonIOException see GSON docs
     */
    public static HuischLedger fromJson(@NotNull Reader reader) {
        Objects.requireNonNull(reader, "Parameter reader cannot be null");

        BufferedReader bufferedReader = new BufferedReader(reader);
        return CustomizedGson.gson.fromJson(bufferedReader, HuischLedger.class);
    }

    /**
     * Writes {@code ledger} to the provided {@code file}.
     *
     * @param file the file to write to
     * @param ledger the ledger that needs to be saved
     * @throws IOException when some IO exception occurs
     */
    public static void toFile(@NotNull File file, HuischLedger ledger) throws IOException {
        Objects.requireNonNull(file, "Parameter file cannot be null");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(HuischLedger.toJson(ledger));
        }
    }

    /**
     * Creates a new Ledger from a file as written by {@link Ledger#toFile(File, Ledger)}.
     *
     * @param file the file to read from
     * @return new Ledger as represented by the content of {@code file}
     * @throws IOException when some IO exception occurs
     */
    public static HuischLedger fromFile(@NotNull File file) throws IOException {
        Objects.requireNonNull(file, "Parameter file cannot be null");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return CustomizedGson.gson.fromJson(reader, HuischLedger.class);
        }
    }

    /**
     * Adds the MapChangeListener {@code listener} to the listeners of the accountingEntities.
     *
     * @param listener the MapChangeListener
     */
    public void addEntityListener(MapChangeListener<Integer, AccountingEntity> listener) {
        accountingEntities.addListener(listener);
    }

    /**
     * Removes the listener from the observable AccountingEntities.
     *
     * @param listener the listener to remove
     */
    public void removeEntityListener(MapChangeListener<Integer, AccountingEntity> listener) {
        accountingEntities.removeListener(listener);
    }

    /**
     * Adds the MapChangeListener {@code listener} to the listeners of the transactions.
     *
     * @param listener the MapChangeListener
     */
    public void addTransactionListener(MapChangeListener<Integer, Transaction> listener) {
        transactions.addListener(listener);
    }

    /**
     * Removes the listener from the observable Transactions.
     *
     * @param listener the listener to remove
     */
    public void removeTransactionListener(MapChangeListener<Integer, Transaction> listener) {
        transactions.removeListener(listener);
    }

    /**
     * Adds the MapChangeListener {@code listener} to the listeners of the receipts.
     *
     * @param listener the MapChangeListener
     */
    public void addReceiptListener(MapChangeListener<Integer, Receipt> listener) {
        receipts.addListener(listener);
    }

    /**
     * Removes the listener from the observable Receipts.
     *
     * @param listener the listener to remove
     */
    public void removeReceiptListener(MapChangeListener<Integer, Receipt> listener) {
        receipts.removeListener(listener);
    }

    @Override
    public void addTransaction(@NotNull Transaction transaction) {
        // Check that the receipt exists
        if (transaction.getReceiptId() != null) {
            if (receipts.containsKey(transaction.getReceiptId())) {
                super.addTransaction(transaction);
                receipts.get(transaction.getReceiptId()).registerTransaction(transaction.getId());
            } else {
                throw new IllegalArgumentException("The transaction has a non-existent receiptId: (T.Id:" + transaction.getId() + ",T.rId:" + transaction.getReceiptId() + ")");
            }
        } else {
            super.addTransaction(transaction);
        }
    }
}
