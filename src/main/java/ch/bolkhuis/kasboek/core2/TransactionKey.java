package ch.bolkhuis.kasboek.core2;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

/**
 * The comparable interface that should be implemented by Transactions so that
 * the Transaction itself can be used as key
 */
public interface TransactionKey extends Comparable<TransactionKey> {
    LocalDate getDate();
    int getId();

    @Override
    default int compareTo(@NotNull TransactionKey o) {
        if (this.getDate().compareTo(o.getDate()) < 0)
            return -1;
        if (this.getDate().compareTo(o.getDate()) > 0)
            return 1;

        return getId() - o.getId();
    }
}
