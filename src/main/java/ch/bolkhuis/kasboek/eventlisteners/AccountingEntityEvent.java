package ch.bolkhuis.kasboek.eventlisteners;

import java.util.EventObject;

public class AccountingEntityEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public AccountingEntityEvent(Object source) {
        super(source);
    }
}
