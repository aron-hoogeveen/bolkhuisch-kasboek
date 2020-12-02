package ch.bolkhuis.kasboek.core;

import ch.bolkhuis.kasboek.eventlisteners.AccountingEntityEvent;
import ch.bolkhuis.kasboek.eventlisteners.AccountingEntityEventListener;
import ch.bolkhuis.kasboek.eventlisteners.TransactionEvent;
import ch.bolkhuis.kasboek.eventlisteners.TransactionEventListener;
import ch.bolkhuis.kasboek.gson.CustomizedGson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * The immutable class Ledger contains a collection of processed and unprocessed Transactions as well as a collection
 * of {@code AccountingEntities}. This class enforces that for all transactions the debtor as well as the creditor
 * are present in the collection of {@code AccountingEntities}.
 * The keys for the processed- and unprocessed Transactions and the AccountingEntities are equal to their getId() result.
 * This logically results in that no two AccountingEntities, processed- or unprocessed Transactions can have the same id.
 *
 *
 * Note: versions v0.2-pre-alpha and higher are not compatible with earlier versions.
 *
 * @version v0.2-pre-alpha
 * @author Aron Hoogeveen
 */
public class Ledger {
    protected  @NotNull final ObservableMap<Integer, Transaction> transactions;
    protected  @NotNull final ObservableMap<Integer, AccountingEntity> accountingEntities;
    protected int nextTransactionId;
    protected int nextAccountingEntityId;
    /**
     * Contains EventListeners of type AccountingEntityEventListener and TransactionEventListener
     */
    protected final Vector<EventListener> eventListeners = new Vector<>();

    /**
     * Creates a new Ledger with empty collections, and sets the {@code next***Id} fields to zero.
     */
    public Ledger() {
        this.transactions = FXCollections.observableHashMap();
        this.accountingEntities = FXCollections.observableHashMap();
        this.nextTransactionId = 0;
        this.nextAccountingEntityId = 0;
    }

    /**
     * Creates a new Ledger that is equal to the {@code old} Ledger.
     *
     * @throws IllegalArgumentException whenever {@code old} is in an invalid state
     * @see Ledger#equals(Object)
     */
    public Ledger(@NotNull Ledger old) {
        Objects.requireNonNull(old, "Parameter old cannot be null");

        // Do not construct a copy if old is in an illegal state.
        for (Transaction transaction : old.transactions.values()) {
            // check if debtor and creditor exist
            if (!old.accountingEntities.containsKey(transaction.getDebtorId()) ||
                    !old.accountingEntities.containsKey(transaction.getCreditorId())) {
                throw new IllegalArgumentException("old is in an illegal state. Some processed Transactions have missing AccountingEntities");
            }
            // check if key and value.getId() are the same
            Integer key = getKey(old.transactions, transaction);
            if (key == null) { throw new RuntimeException("old got modified before new was constructed"); }
            if (!(transaction.getId() == key)) { throw new IllegalArgumentException("id has te match the key"); }
        }
        for (Map.Entry<Integer, AccountingEntity> entityEntry : old.accountingEntities.entrySet()) {
            if (entityEntry.getKey() != entityEntry.getValue().getId()) { throw new IllegalArgumentException("id has to match the key"); }
        }

        this.accountingEntities = old.accountingEntities;
        this.transactions = old.transactions;
        this.nextTransactionId = old.nextTransactionId;
        this.nextAccountingEntityId = old.nextAccountingEntityId;
    }

    /**
     * Creates a new Ledger with initial AccountingEntities.
     *
     * @param accountingEntities the initial AccountingEntities
     */
    public Ledger(@NotNull ObservableMap<Integer, AccountingEntity> accountingEntities) {
        Objects.requireNonNull(accountingEntities, "Parameter accountingEntities cannot be null");

        for (Map.Entry<Integer, AccountingEntity> entityEntry : accountingEntities.entrySet()) {
            if (entityEntry.getKey() != entityEntry.getValue().getId()) {
                throw new IllegalArgumentException("id has to match the key");
            }
        }

        this.accountingEntities = accountingEntities;
        this.transactions = FXCollections.observableHashMap();
        this.nextTransactionId = 0;
        Set<Integer> keySet = accountingEntities.keySet();
        int biggestKey = 0;
        for (int key : keySet) {
            biggestKey = (biggestKey > key) ? biggestKey : key;
        }
        this.nextAccountingEntityId = biggestKey + 1;
    }

    /**
     * Creates a new Ledger with initial AccountingEntities, processed- and unprocessed Transactions; sets the
     * {@code nextTransactionId} and {@code nextAccountingEntityId} accordingly.
     *
     * @param accountingEntities TreeMap containing at least all the Entities for the processed- and unprocessed Transactions
     * @param transactions the transactions that already have been processed
     */
    public Ledger(@NotNull ObservableMap<Integer, AccountingEntity> accountingEntities,
                  @NotNull ObservableMap<Integer, Transaction> transactions) {
        Objects.requireNonNull(accountingEntities, "Parameter accountingEntities cannot be null");
        Objects.requireNonNull(transactions, "Parameter transactions cannot be null");

        int nextTransactionId = 0;
        for (Transaction transaction : transactions.values()) {
            // check if debtor and creditor exist
            if (!accountingEntities.containsKey(transaction.getDebtorId()) ||
                    !accountingEntities.containsKey(transaction.getCreditorId())) {
                throw new IllegalArgumentException("Some processed Transactions have missing AccountingEntities");
            }
            // check if key and value.getId() are the same
            Integer key = getKey(transactions, transaction);
            if (key == null) { throw new RuntimeException("old got modified before new was constructed"); }
            if (!(transaction.getId() == key)) { throw new IllegalArgumentException("id has te match the key"); }

            nextTransactionId = (transaction.getId() > nextTransactionId) ? transaction.getId() + 1 : nextTransactionId;
        }
        for (Map.Entry<Integer, AccountingEntity> entityEntry : accountingEntities.entrySet()) {
            if (entityEntry.getKey() != entityEntry.getValue().getId()) {
                throw new IllegalArgumentException("id has to match the key");
            }
        }

        this.accountingEntities = accountingEntities;
        this.transactions = transactions;
        this.nextTransactionId = nextTransactionId;
        Set<Integer> keySet = accountingEntities.keySet();
        int biggestKey = 0;
        for (int key : keySet) {
            biggestKey = (biggestKey > key) ? biggestKey : key;
        }
        this.nextAccountingEntityId = biggestKey + 1;
    }

    /**
     * Adds {@code transaction} to the list of Transactions.
     *
     * @param transaction the Transaction to add
     * @throws IllegalArgumentException when transaction does not adhere to the contract this class has for Transactions
     */
    public void addTransaction(@NotNull Transaction transaction) {
        Objects.requireNonNull(transaction, "Parameter transaction cannot be null");

        if (!accountingEntities.containsKey(transaction.getDebtorId()) || !accountingEntities.containsKey(transaction.getCreditorId())) {
            throw new IllegalArgumentException("Not all AccountingEntities are available in this ledger");
        }
        if (transactions.containsKey(transaction.getId())) { throw new IllegalArgumentException("Cannot add a transaction with a duplicate key"); }

        processAndPutTransaction(transaction);

        // increase the nextTransactionId if applicable
        if (transaction.getId() >= nextTransactionId)
            nextTransactionId = transaction.getId() + 1;

        // Notify all listeners that the Collection of Transactions changed
        fireTransactionEvent();
    }

    private Transaction removeTransactionInternal(Transaction transaction) {
        return unprocessAndRemoveTransaction(transaction);
    }

    /**
     * Removes a Transaction, adjusting the affecting balances of accompanying AccountingEntities accordingly.
     *
     * @param id the id of the Transaction to remove
     * @return the Transaction that got removed, or {@code null} if there was no such Transaction
     */
    public Transaction removeTransaction(int id) {
        Transaction transaction = transactions.get(id);
        if (transaction == null) { return null; }

        // Remove the transaction
        Transaction removed = removeTransactionInternal(transaction);
        // Notify all listeners that the Collection of Transactions changed
        fireTransactionEvent();
        fireAccountingEntityEvent();
        // return the removed Transaction
        return removed;
    }

    /**
     * Processed the Transaction that has id {@code id}. The balances of accompanying AccountingEntities are updated
     * accordingly.<br />
     * <br />
     * Note: this method does not validate the {@code transactions}.
     *
     * @param transaction the Transaction to process
     * @return the old Transaction if there was one
     * @throws IllegalArgumentException if there exists no Transaction with that {@code id}
     */
    private Transaction processAndPutTransaction(@NotNull Transaction transaction) {
        Objects.requireNonNull(transaction, "Parameter transaction cannot be null");

        // Adjust the balances of the AccountingEntities
        AccountingEntity debtor = accountingEntities.get(transaction.getDebtorId());
        AccountingEntity creditor = accountingEntities.get(transaction.getCreditorId());

        if (debtor == null || creditor == null) {
            throw new AssertionError("This class needs to enforce that all AccountingEntities exist for all" +
                    "added Transactions");
        }

        debtor = debtor.debit(transaction.getAmount());
        creditor = creditor.credit(transaction.getAmount());

        // update the AccountingEntities
        accountingEntities.put(debtor.getId(), debtor);
        accountingEntities.put(creditor.getId(), creditor);

        return transactions.put(transaction.getId(), transaction);
    }

    /**
     * Unprocesses the Transaction that has id {@code id}. The balances of accompanying AccountingEntities are updated
     * accordingly.<br />
     * <br />
     * Note: this method does not validate the {@code transactions}.
     *
     * @param transaction the Transaction to process
     * @return the old Transaction if there was one
     * @throws IllegalArgumentException if there exists no Transaction with that {@code id}
     */
    private Transaction unprocessAndRemoveTransaction(@NotNull Transaction transaction) {
        Objects.requireNonNull(transaction, "Parameter transaction cannot be null");

        // Adjust the balances of the AccountingEntities
        AccountingEntity debtor = accountingEntities.get(transaction.getDebtorId());
        AccountingEntity creditor = accountingEntities.get(transaction.getCreditorId());

        if (debtor == null || creditor == null) {
            throw new AssertionError("This class needs to enforce that all AccountingEntities exist for all" +
                    "added Transactions");
        }

        // reverse the debiting and crediting
        debtor = debtor.credit(transaction.getAmount());
        creditor = creditor.debit(transaction.getAmount());

        // update the AccountingEntities
        accountingEntities.put(debtor.getId(), debtor);
        accountingEntities.put(creditor.getId(), creditor);

        return transactions.remove(transaction.getId());
    }

    /**
     * Returns {@code true} if this key is already contained in the Transaction collection.
     *
     * @param id the id to check
     * @return {@code true} if this key is already contained in the Transaction collection, {@code false} otherwise
     */
    public boolean containsTransactionById(int id) {
        return transactions.containsKey(id);
    }

    /**
     * Adds an entity to the AccountingEntity collection.
     *
     * @param accountingEntity the AccountingEntry to add
     * @throws IllegalArgumentException when {@code accountingEntity} does not adhere to the contract of this class
     */
    public void addAccountingEntity(@NotNull AccountingEntity accountingEntity) {
        Objects.requireNonNull(accountingEntity, "Parameter accountingEntity cannot be null");

        if (accountingEntities.containsKey(accountingEntity.getId())) { throw new IllegalArgumentException("Key already exists"); }

        // if the user supplied an AccountingEntity with id equal to or greater than nextAccountingEntityId, then increment it
        if (accountingEntity.getId() >= nextAccountingEntityId) {
            nextAccountingEntityId = accountingEntity.getId() + 1;
        }

        accountingEntities.put(accountingEntity.getId(), accountingEntity);

        // Notify all listeners that the Collection of AccountingEntities changed
        fireAccountingEntityEvent();
    }

    /**
     * Updates the accountingEntity with the same id as the provided {@code accountingEntity}.
     *
     * @param accountingEntity the value to replace the old AccountingEntity with
     * @return the previous value of AccountingEntity that is replaced
     */
    public AccountingEntity updateAccountingEntity(@NotNull AccountingEntity accountingEntity) {
        Objects.requireNonNull(accountingEntity, "Parameter accountingEntity cannot be null");

        // make sure that the id already exists
        if (!accountingEntities.containsKey(accountingEntity.getId())) {
            throw new IllegalArgumentException("There does not exists an AccountingEntity with that id");
        }

        // update the AccountingEntity and return the previous value
        AccountingEntity updated = accountingEntities.put(accountingEntity.getId(), accountingEntity);
        // Notify all listeners that the Collection of AccountingEntities changed
        fireAccountingEntityEvent();
        // Return the previous value
        return updated;
    }

    /**
     * Returns a deep copy of {@code accountingEntries}. This method is linear time.
     * @return new {@link SortedMap}
     * @deprecated
     */
    public @Deprecated TreeMap<Integer, AccountingEntity> copyOfAccountingEntities() {
        return new TreeMap<>(accountingEntities);
    }

    public @NotNull ObservableMap<Integer, AccountingEntity> getAccountingEntities() { return accountingEntities; }

    /**
     * Returns a deep copy of {@code transactions}. This method is linear time.
     *
     * @deprecated
     */
    public @Deprecated TreeMap<Integer, Transaction> copyOfTransactions() {
        return new TreeMap<>(transactions);
    }

    public @NotNull ObservableMap<Integer, Transaction> getTransactions() { return transactions; }

    /**
     * Returns if Object {@code o} is equal to this Ledger. This method does not take the Vector of EventListeners into
     * account.
     *
     * @param o the Object to compare to
     * @return if this and o are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ledger ledger = (Ledger) o;

        if (nextTransactionId != ledger.nextTransactionId) return false;
        if (nextAccountingEntityId != ledger.nextAccountingEntityId) return false;
        if (!transactions.equals(ledger.transactions)) return false;
        return accountingEntities.equals(ledger.accountingEntities);
    }

    @Override
    public int hashCode() {
        int result = transactions.hashCode();
        result = 31 * result + accountingEntities.hashCode();
        result = 31 * result + nextTransactionId;
        result = 31 * result + nextAccountingEntityId;
        return result;
    }

    /**
     * Returns a JSON string that is parsable by {@link Ledger#fromJson(Reader)}.
     *
     * @param ledger the Ledger to convert to JSON
     * @return a JSON string representing {@code ledger}
     */
    public static String toJson(Ledger ledger) {
        return CustomizedGson.gson.toJson(ledger, Ledger.class);
    }

    /**
     * Returns a new Ledger from a {@code reader} supplying a JSON String as generated by {@link Ledger#toJson(Ledger)}.
     *
     * @param reader the reader providing the JSON String
     * @return a Ledger as represented by the JSON String
     * @throws com.google.gson.JsonSyntaxException see GSON docs
     * @throws com.google.gson.JsonIOException see GSON docs
     */
    public static Ledger fromJson(@NotNull Reader reader) {
        Objects.requireNonNull(reader, "Parameter reader cannot be null");

        BufferedReader bufferedReader = new BufferedReader(reader);
        return CustomizedGson.gson.fromJson(bufferedReader, Ledger.class);
    }

    /**
     * Writes {@code ledger} to the provided {@code file}.
     *
     * @param file the file to write to
     * @param ledger the ledger that needs to be saved
     * @throws IOException when some IO exception occurs
     */
    public static void toFile(@NotNull File file, Ledger ledger) throws IOException {
        Objects.requireNonNull(file, "Parameter file cannot be null");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(Ledger.toJson(ledger));
        }
    }

    /**
     * Creates a new Ledger from a file as written by {@link Ledger#toFile(File, Ledger)}.
     *
     * @param file the file to read from
     * @return new Ledger as represented by the content of {@code file}
     * @throws IOException when some IO exception occurs
     */
    public static Ledger fromFile(@NotNull File file) throws IOException {
        Objects.requireNonNull(file, "Parameter file cannot be null");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return CustomizedGson.gson.fromJson(reader, Ledger.class);
        }
    }

    /**
     * Returns the id that should be used for the next AccountingEntity.
     *
     * @return AccountingEntity id
     */
    public int getNextAccountingEntityId() {
        return nextAccountingEntityId;
    }

    /**
     * Returns the id that should be used for the next AccountingEntity and increments the field by 1.
     *
     * @return next AccountingEntity id
     */
    public int getAndIncrementNextAccountingEntityId() {
        return nextAccountingEntityId++;
    }

    /**
     * Returns the id that should be used for the next Transactions.
     * Note: use {@link Ledger#getAndIncrementNextTransactionId()} if you need to create multiple Transactions but
     * do not want to add them immediately.
     *
     * @return Transaction id
     * @see Ledger#getAndIncrementNextTransactionId()
     */
    public int getNextTransactionId() {
        return nextTransactionId;
    }

    /**
     * Returns the id that should be used for the next Transaction and increments the local field by 1.
     *
     * @return Transaction id
     */
    public int getAndIncrementNextTransactionId() {
        return  nextTransactionId++;
    }

    /**
     * Gets the key for this {@code value}.
     */
    public static <K, V> K getKey(Map<K, V> map, V value) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(value);

        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue()))
                return entry.getKey();
        }
        return null;
    }

    /**
     * Get the id of the AccountingEntity with name {@code name}.
     *
     * @param name the name of the AccountingEntity
     * @return the id of the AccountingEntity with name {@code name} or {@code null} if it was nog found
     */
    public Integer getAccountingEntityId(@NotNull String name) {
        Objects.requireNonNull(accountingEntities);
        Objects.requireNonNull(name);

        for (Map.Entry<Integer, AccountingEntity> entry : accountingEntities.entrySet()) {
            if (name.equals(entry.getValue().getName())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Returns the AccountingEntity with id {@code id} or {@code null} if no AccountingEntity with that id is found.
     *
     * @param id the id of the AccountingEntity
     * @return the corresponding AccountingEntity or {@code null}
     */
    public AccountingEntity getAccountingEntityById(int id) {
        return accountingEntities.get(id);
    }

    /**
     * Returns an array with all Transactions connected to the AccountingEntity with id {@code entityId}.
     * Currently this function can return at most 99 Transactions of the same type.
     *
     * @param entityId the id of the AccountingEntity
     * @return TODO change the return type to something scalable and intuitive
     * @deprecated use {@link Ledger#getAllTransactionsOf(int)}
     */
    public @Deprecated Transaction[][] getAllTransactionsFromAccountingEntityAsArray(int entityId) {
        Transaction[][] list = new Transaction[1][99]; // transactions
        int i = 0;
        for (Transaction t : transactions.values()) {
            if (t.getDebtorId() == entityId || t.getCreditorId() == entityId) {
                if (i == 99) break;

                list[0][i] = t;
                i++;
            }
        }

        return list;
    }

    /**
     * FIXME add JavaDoc for getAllTransactionsFromAccountingEntity
     * @param entityId
     * @return
     */
    public TreeMap<Integer, Transaction> getAllTransactionsOf(int entityId) {
        TreeMap<Integer, Transaction> result = new TreeMap<>();
        for (Map.Entry<Integer, Transaction> entry : transactions.entrySet()) {
            Transaction t = entry.getValue();
            if (t.getDebtorId() == entityId || t.getCreditorId() == entityId) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    /**
     * Returns a Set of all Transactions that involve AccountingEntity with id {@code entityId} and which are between
     * {@code from} date and {@code to} date inclusive.
     *
     * @param entityId the id of an AccountingEntity
     * @param from the start date inclusive
     * @param to the end date inclusive
     * @return a set of Transactions
     */
    public Set<Transaction> getTransactionsOf(
            int entityId,
            @NotNull LocalDate from,
            @NotNull LocalDate to) {
        Objects.requireNonNull(from, "Parameter from cannot be null");
        Objects.requireNonNull(to, "Parameter to cannot be null");

        // traverse all transactions
        Set<Transaction> result = new HashSet<>();
        transactions.forEach(((integer, transaction) -> {
            if (from.compareTo(transaction.getDate()) <= 0 && to.compareTo(transaction.getDate()) >= 0) {
                // transaction.getDate() is between from and to inclusive
                if (transaction.getDebtorId() == entityId || transaction.getCreditorId() == entityId) {
                    result.add(transaction);
                }
            }
        }));
        return result;
    }

    /**
     * Returns whether this Ledger has a AccountingEntity with id matching {@code accountingEntityId}.
     *
     * @param accountingEntityId the id to search for
     * @return {@code true} if this Ledger contains an AccountingEntity with corresponding id, {@code false} otherwise
     */
    private boolean containsAccountingEntity(int accountingEntityId) {
        return accountingEntities.containsKey(accountingEntityId);
    }

    /**
     * Adds the TransactionEventListener {@code transactionEventListener} to the Vector of EventListeners.
     *
     * @param transactionEventListener the TransactionEventListener to add
     */
    public void addTransactionEventListener(TransactionEventListener transactionEventListener) {
        eventListeners.add(transactionEventListener);
    }

    public void addAccountingEntityEventListener(AccountingEntityEventListener accountingEntityEventListener) {
        eventListeners.add(accountingEntityEventListener);
    }

    /**
     * Notify all TransactionEventListeners that the Collection of Transactions is updated.
     */
    private void fireTransactionEvent() {
        for (EventListener eventListener : eventListeners) {
            if (eventListener instanceof TransactionEventListener) {
                ((TransactionEventListener)eventListener).transactionCollectionChanged(new TransactionEvent(this));
            }
        }
    }

    /**
     * Notify all AccountingEntityEventListeners that the Collection of AccountingEntities is udpated.
     */
    private void fireAccountingEntityEvent() {
        for (EventListener eventListener : eventListeners) {
            if (eventListener instanceof AccountingEntityEventListener) {
                ((AccountingEntityEventListener)eventListener).accountingEntityCollectionChanged(new AccountingEntityEvent(this));
            }
        }
    }

    /**
     * Manually set the field {@code nextAccountingEntityId}. Do not use this method unless you are very sure what you
     * are doing.
     *
     * @param id the new {@code nextAccountingEntityId}
     */
    public void setNextAccountingEntityId(int id) {
        this.nextAccountingEntityId = id;
    }
}
