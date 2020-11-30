package ch.bolkhuis.kasboek.eventlisteners;

import java.util.EventListener;

/**
 * TransactionEventListener is the interface that all classes should implement who need to know whenever a Collection of
 * Transactions is updated.
 *
 * @version 0.2
 * @author Aron Hoogeveen
 */
public interface TransactionEventListener extends EventListener {
    void transactionCollectionChanged(TransactionEvent transactionEvent);
}
