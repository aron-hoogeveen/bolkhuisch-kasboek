package ch.bolkhuis.kasboek.core2;

import java.util.Comparator;
import java.util.Objects;

/**
 * Comparator that compares two transactions using all fields.
 * FIXME add test class for TransactionComparator
 */
public class TransactionComparator implements Comparator<Transaction> {

    @Override
    public int compare(Transaction o1, Transaction o2) {
        Objects.requireNonNull(o1);
        Objects.requireNonNull(o2);

        // date
        if (o1.getDate().isBefore(o2.getDate())) {
            return -1;
        } else if (o1.getDate().isAfter(o2.getDate())) {
            return 1;
        }

        // id
        if (o1.getId() < o2.getId()) {
            return -1;
        } else if (o1.getId() > o2.getId()) {
            return 1;
        }

        //
        if (o1.getReceiptId() == null) {
            if (o2.getReceiptId() != null) {
                return -1;
            }
        } else if (o2.getReceiptId() == null) {
            return 1;
        } else if (o1.getReceiptId() < o2.getReceiptId()) {
            return -1;
        } else if (o1.getReceiptId() > o2.getReceiptId()) {
            return 1;
        }

        // debtorId
        if (o1.getDebtorId() < o2.getDebtorId()) {
            return -1;
        } else if (o1.getDebtorId() > o2.getDebtorId()) {
            return 1;
        }

        // creditorId
        if (o1.getCreditorId() < o2.getCreditorId()) {
            return -1;
        } else if (o1.getCreditorId() > o2.getCreditorId()) {
            return 1;
        }

        // amount
        if (o1.getAmount() < o2.getAmount()) {
            return -1;
        } else if (o1.getAmount() > o2.getAmount()) {
            return 1;
        }

        // Equal amounts. Check description
        return o1.getDescription().compareTo(o2.getDescription());
    }

}
