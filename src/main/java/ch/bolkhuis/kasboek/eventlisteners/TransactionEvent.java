package ch.bolkhuis.kasboek.eventlisteners;

import java.util.EventObject;

public class TransactionEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public TransactionEvent(Object source) {
        super(source);
    }
}
