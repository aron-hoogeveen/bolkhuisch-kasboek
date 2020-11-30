package ch.bolkhuis.kasboek.eventlisteners;

import java.util.EventListener;

/**
 * AccountingEntityEventListener is the interface that all classes should implement who need to know whenever a Collection of
 * AccountingEntities is updated.
 *
 * @version 0.2
 * @author Aron Hoogeveen
 */
public interface AccountingEntityEventListener extends EventListener {
    void accountingEntityCollectionChanged(AccountingEntityEvent accountingEntityEvent);
}
