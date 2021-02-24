package ch.bolkhuis.kasboek.core2;

import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.*;

public class ObservableTransactionMap implements ObservableMap<TransactionKey, Transaction> {

    private final TransactionMap map;
    private final Vector<MapChangeListener<? super TransactionKey, ? super Transaction>> changeListeners = new Vector<>();
    private final Vector<InvalidationListener> invalidationListeners = new Vector<>();

    /**
     * Constructs a new ObservableTransactionMap with the {@code map} as backing map.
     *
     * @param map the backing map
     */
    public ObservableTransactionMap(@NotNull TransactionMap map) {
        Objects.requireNonNull(map);

        this.map = map;
    }

    @Override
    public void addListener(@NotNull MapChangeListener<? super TransactionKey, ? super Transaction> mapChangeListener) {
        Objects.requireNonNull(mapChangeListener);

        changeListeners.add(mapChangeListener);
    }

    @Override
    public void removeListener(@NotNull MapChangeListener<? super TransactionKey, ? super Transaction> mapChangeListener) {
        Objects.requireNonNull(mapChangeListener);

        changeListeners.remove(mapChangeListener);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsTransactionKey((TransactionKey)key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsTransaction((Transaction)value);
    }

    @Override
    public Transaction get(Object key) {
        Objects.requireNonNull(key);

        // try to cast to TransactionKey
        return map.getTransaction((TransactionKey)key);
    }

    /**
     * Puts the new {@code value} in this map and returns the old mapping.<br />
     * <br />
     * The value of {@code key} is ignored, and only the fields of the {@code value} are used.
     *
     * @param key ignored
     * @param value the Transaction
     * @return the old mapping, or {@code null} if the was no mapping
     */
    @Nullable
    @Override
    public Transaction put(TransactionKey key, Transaction value) {
        Transaction old = map.putTransaction(value);
        informAdded(key, value);
        return old;
    }

    /**
     * Creates a mapping for the {@code transaction} and returns any old mappings.
     *
     * @param transaction the transaction to put in the map
     * @return the old mapping, or {@code null} if the was no mapping
     */
    public Transaction put(Transaction transaction) {
        return put(transaction, transaction);
    }

    @Override
    public Transaction remove(Object key) {
        return map.removeTransaction((TransactionKey)key);
    }

    @Override
    public void putAll(@NotNull Map<? extends TransactionKey, ? extends Transaction> m) {
        map.putAllTransactions(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @NotNull
    @Override
    public Set<TransactionKey> keySet() {
        throw new UnsupportedOperationException(); // FIXME implement keySet()
    }

    @NotNull
    @Override
    public Collection<Transaction> values() {
        return map.transactionValues();
    }

    @NotNull
    @Override
    public Set<Entry<TransactionKey, Transaction>> entrySet() {
        throw new UnsupportedOperationException(); // FIXME implement entrySet()
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        invalidationListeners.add(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        invalidationListeners.remove(invalidationListener);
    }

    /**
     * Inform all ChangeListeners of a removed mapping.
     *
     * @param key the key
     * @param valueRemoved the old value
     */
    private void informRemoved(@NotNull TransactionKey key, @NotNull Transaction valueRemoved) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(valueRemoved);

        changeListeners.forEach((o) -> o.onChanged(new MapChange(
                this,
                false,
                true,
                key,
                null,
                valueRemoved
        )));
    }

    /**
     * Inform all ChangeListeners of a new mapping that was added.
     *
     * @param key the key
     * @param valueAdded the new value
     */
    private void informAdded(@NotNull TransactionKey key, @NotNull Transaction valueAdded) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(valueAdded);

        changeListeners.forEach((o) -> o.onChanged(new MapChange(
                this,
                true,
                false,
                key,
                valueAdded,
                null
        )));
    }

    /**
     * Inform all ChangeListeners of a mapping that changed of value.
     *
     * @param key the key
     * @param valueAdded the new value
     * @param valueRemoved the old value
     */
    private void informChanged(@NotNull TransactionKey key, @NotNull Transaction valueAdded,
                               @NotNull Transaction valueRemoved) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(valueAdded);
        Objects.requireNonNull(valueRemoved);

        changeListeners.forEach((o) -> o.onChanged(new MapChange(
                this,
                true,
                true,
                key,
                valueAdded,
                valueRemoved
        )));
    }

    private class MapChange extends MapChangeListener.Change<TransactionKey, Transaction> {

        private final boolean wasAdded;
        private final boolean wasRemoved;
        private final TransactionKey key;
        private final Transaction valueAdded;
        private final Transaction valueRemoved;

        public MapChange(@NotNull ObservableMap<TransactionKey, Transaction> observableMap, boolean wasAdded, boolean wasRemoved,
                         @NotNull TransactionKey key, @Nullable Transaction valueAdded, @Nullable Transaction valueRemoved) {
            super(observableMap);
            Objects.requireNonNull(key);

            this.wasAdded = wasAdded;
            this.wasRemoved = wasRemoved;
            this.key = key;
            this.valueAdded = valueAdded;
            this.valueRemoved = valueRemoved;
        }

        @Override
        public boolean wasAdded() {
            return wasAdded;
        }

        @Override
        public boolean wasRemoved() {
            return wasRemoved;
        }

        @Override
        public TransactionKey getKey() {
            return key;
        }

        @Override
        public Transaction getValueAdded() {
            return valueAdded;
        }

        @Override
        public Transaction getValueRemoved() {
            return valueRemoved;
        }
    }
}
