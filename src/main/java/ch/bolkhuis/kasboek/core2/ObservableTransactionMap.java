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
     * @return the old mapping
     */
    @Nullable
    @Override
    public Transaction put(TransactionKey key, Transaction value) {
        return map.putTransaction(value);
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
        throw new UnsupportedOperationException(); // FIXME implement values()
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
}
