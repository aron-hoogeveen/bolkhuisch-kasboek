package ch.bolkhuis.kasboek.core2;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

/**
 * The comparable interface that should be implemented by Receipts so that
 * the Receipt itself can be used as key
 */
public interface ReceiptKey extends Comparable<ReceiptKey> {

    LocalDate getDate();
    int getId();

    @Override
    default int compareTo(@NotNull ReceiptKey o) {
        if (this.getDate().compareTo(o.getDate()) < 0)
            return -1;
        if (this.getDate().compareTo(o.getDate()) > 0)
            return 1;

        return getId() - o.getId();
    }

}
