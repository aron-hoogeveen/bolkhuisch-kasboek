package ch.bolkhuis.kasboek.core2;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.*;

/**
 * TransactionTreeMap is the data structure in which Transactions can be efficiently stored. This TreeMap
 * consists of TreeMap buckets containing the actual transactions. The parent TreeMap sorts on LocalDates
 * and the other TreeMap sorts on Integers (id's of the Transactions).
 */
public class TransactionMap extends TreeMap<LocalDate, TreeMap<Integer, Transaction>> {

    /**
     * The number of Transactions currently in this TransactionMap
     */
    private int size = 0;

    /**
     * Adds the Transaction {@code t} to this TransactionTreeMap.
     *
     * @param t the Transaction to add
     * @return The previous value associated with a Transaction with the same date and id, or {@code null} if there was no such Transaction
     * @throws NullPointerException if {@code t} is null
     */
    @Nullable
    public Transaction putTransaction(@NotNull Transaction t) {
        Objects.requireNonNull(t);

        /*
         * Check if there is already a bucket associated with t.getDate().
         * If there is not, create the bucket.
         * Add the transaction at the correct place in the bucket.
         */
        if (!containsKey(t.getDate())) {
            put(t.getDate(), new TreeMap<>());
        }
        TreeMap<Integer, Transaction> bucket = get(t.getDate());

        // put the new Transaction in the bucket and return the old associated value
        Transaction old = bucket.put(t.getId(), t);
        if (old == null) {
            size++;
        }
        return old;
    }

    /**
     * Removes the Transaction that has {@code date} and {@code id} equal to the values of the {@code TransactionKey}
     * from this TransactionTreeMap.
     *
     * @param key the key values of the Transaction to remove
     * @return the Transaction that was removed, or {@code null} if there was no mapping
     */
    public Transaction removeTransaction(@NotNull TransactionKey key) {
        /*
         * Get the bucket the Transaction would be in.
         * If there is no such bucket, there is also no such Transaction
         * If there is such a bucket, get the mapping for the id
         * If there is no Transaction, return null,
         * else, remove the Transaction from the bucket.
         * If the bucket is now empty, remove the bucket from this TransactionTreeMap
         */
        TreeMap<Integer, Transaction> bucket = get(key.getDate());
        // check if a mapping exists
        if (bucket == null || !bucket.containsKey(key.getId())) return null;

        Transaction old = bucket.remove(key.getId());

        // if the bucket is now empty, remove it from this TransactionTreeMap to aid garbage collection
        if (bucket.size() == 0) {
            remove(key.getDate());
        }

        size--;
        return old;
    }

    /**
     * Returns whether or not this TransactionTreeMap contains a Transaction with the TransactionKey {@code key}.
     *
     * @param key the key of the Transaction
     * @return {@code true} if this map contains a mapping, {@code false} otherwise
     * @throws NullPointerException if {@code key} is null
     */
    public boolean containsTransactionKey(@NotNull TransactionKey key) {
        Objects.requireNonNull(key);

        TreeMap<Integer, Transaction> bucket = get(key.getDate());
        return bucket != null && bucket.containsKey(key.getId());
    }

    /**
     * Returns if this map contains a mapping for the {@code transaction}.
     *
     * @param transaction the transaction to search for
     * @return {@code true} if this map contains a mapping for the {@code transaction}, {@code false} otherwise
     * @throws NullPointerException if {@code transaction} is null
     */
    public boolean containsTransaction(@NotNull Transaction transaction) {
        Objects.requireNonNull(transaction);

        /*
         * Find the correct bucket.
         * Return if it contains the transaction
         */
        TreeMap<Integer, Transaction> bucket = get(transaction.getDate());
        return bucket != null && bucket.containsValue(transaction);
    }

    /**
     * Returns the mapping of the key {@code key}.
     *
     * @param key the key
     * @return the mapping, or {@code null} if there is not mapping for this key
     * @throws NullPointerException if key is {@code null}
     */
    public Transaction getTransaction(@NotNull TransactionKey key) {
        Objects.requireNonNull(key);

        TreeMap<Integer, Transaction> bucket = get(key.getDate());
        return bucket.get(key.getId());
    }

    /**
     * Returns an ordered List with all the transactions that have a data between {@code from} inclusive and {@code from}
     * exclusive.
     *
     * @param from the lowest date
     * @param to the highest date
     * @return ordered List of Transactions
     */
    public List<Transaction> subListTransactions(@NotNull LocalDate from, @NotNull LocalDate to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        // ordered List
        ArrayList<Transaction> list = new ArrayList<>();

        /*
         * Get the submap with all the buckets that have a key that is between `from` inclusive and `to` exclusive
         * Iterate through all the buckets in-order and add each Transaction to the ordered List
         */
        SortedMap<LocalDate, TreeMap<Integer, Transaction>> subMap = subMap(from, to);

        // iterate through all buckets
        for (TreeMap<Integer, Transaction> bucket : subMap.values()) {
            // add all Transactions in the bucket, in-order, to the list
            list.addAll(bucket.values());
        }

        return list;
    }


    /**
     * Returns the id of the Transaction with the highest id in the bucket with key {@code date}.
     *
     * @param date the key of the bucket
     * @return the highest id, or {@code -1} if there is no bucket with that key
     */
    public int highestTransactionId(@NotNull LocalDate date) {
        /*
         * Check if there is a bucket for that date.
         * If there is, get the max key and return it
         */
        TreeMap<Integer, Transaction> bucket = get(date);
        return (bucket == null) ? -1 : bucket.lastKey();
    }

    @Override
    public int size() {
        return 0;
    }

    /**
     * Puts all the transactions that are in {@code map} in this TransactionMap.
     *
     * @param map the map with values to put in this map
     * @throws NullPointerException if {@code map} is {@code null} or any of the mappings contains a null-value
     */
    public void putAllTransactions(@NotNull Map<? extends TransactionKey, ? extends Transaction> map) {
        Objects.requireNonNull(map);

        map.values().forEach(this::putTransaction);
    }

    /**
     * Returns an ordered Collection of all Transactions in this TransactionMap.
     *
     * @return ordered Collection of Transactions
     */
    @NotNull
    public Collection<Transaction> transactionValues() {
        /*
         * Iterate through all buckets, and add all the Transactions to a Collection
         */
        Collection<Transaction> values = new ArrayList<>();

        for (TreeMap<Integer, Transaction> bucket : values()) {
            values.addAll(bucket.values());
        }

        return values;
    }

}
