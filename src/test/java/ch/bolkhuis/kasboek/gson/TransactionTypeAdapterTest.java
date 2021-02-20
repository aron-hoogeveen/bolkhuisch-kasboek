package ch.bolkhuis.kasboek.gson;

import ch.bolkhuis.kasboek.core2.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static ch.bolkhuis.kasboek.gson.CustomizedGson.gson;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeAdapterTest {

    @Test
    public void testJsonReceiptNull() {
        Transaction transaction = new Transaction(LocalDate.now(), 0, 12, 15, 35.99, "Some transacton!", null);
        String jsonStr = gson.toJson(transaction);

        // Let's convert it back to a Transaction
        Transaction t2 = gson.fromJson(jsonStr, Transaction.class);
        assertEquals(transaction, t2);
    }

    @Test
    public void testJsonNonNullReceipt() {
        Transaction transaction = new Transaction(LocalDate.now(), 0, 12, 15, 15.99, "Yes!!!!!", 27);
        String jsonStr = gson.toJson(transaction);
        Transaction t2 = gson.fromJson(jsonStr, Transaction.class);
        assertEquals(transaction, t2);
    }

}