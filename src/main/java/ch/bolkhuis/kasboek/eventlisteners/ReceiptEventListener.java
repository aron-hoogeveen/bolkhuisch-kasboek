package ch.bolkhuis.kasboek.eventlisteners;

import java.util.EventListener;

/**
 * ReceiptEventListener is the interface that all classes should implement who need to know whenever a Collection of
 * Receipts is updated.
 *
 * @version 0.2
 * @author Aron Hoogeveen
 */
public interface ReceiptEventListener extends EventListener {
    void receiptCollectionChanged(ReceiptEvent receiptEvent);
}
