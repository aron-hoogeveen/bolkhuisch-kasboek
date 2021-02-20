package ch.bolkhuis.kasboek.core2;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    public void testConstructorParameters() {
        assertDoesNotThrow(() -> new Transaction(LocalDate.now(), 0, 0, 0, 1, "Some String", null));
        assertThrows(NullPointerException.class, () -> new Transaction(null, 0, 0, 0, 1, "Some String", null));
        assertThrows(NullPointerException.class, () -> new Transaction(LocalDate.now(), 0, 0, 0, 1, null, null));
        assertThrows(IllegalArgumentException.class, () -> new Transaction(LocalDate.now(), 0, 0, 0, 0, "Something", null));
        assertThrows(IllegalArgumentException.class, () -> new Transaction(LocalDate.now(), 0, 0, 0, 1, "     1234", null));
        assertDoesNotThrow(() -> new Transaction(LocalDate.now(), 0, 0, 0, 1, "     12345", null));
        assertThrows(IllegalArgumentException.class, () -> new Transaction(LocalDate.now(), 0, 0, 0, 1, "\t\n\r1234", null));
    }

    @Test
    public void testEquality() {
        LocalDate date = LocalDate.now();
        int id = 0;
        int debtorId = 0;
        int creditorId = 0;
        double amount = 36;
        String description = "Some String";
        Integer receiptId = 42;

        Transaction t1 = new Transaction(date, id, debtorId, creditorId, amount, description, receiptId);
        Transaction t2 = new Transaction(date, id, debtorId, creditorId, amount, description, receiptId);

        assertEquals(t1, t1); // self
        assertEquals(t1, t2);
        assertEquals(t2, t1);

        assertNotNull(t2.getReceiptId());
        t2 = t2.clearReceiptId();

        assertNotEquals(t1, t2);
        assertNull(t2.getReceiptId());
    }

    @Test
    public void testGetters() {
        LocalDate date = LocalDate.now();
        int id = 35;
        int debtorId = 2;
        int creditorId = 33;
        double amount = 36;
        String description = "Some String";
        Integer receiptId = 42;

        Transaction t1 = new Transaction(date, id, debtorId, creditorId, amount, description, receiptId);

        assertEquals(date, t1.getDate());
        assertEquals(id, t1.getId());
        assertEquals(debtorId, t1.getDebtorId());
        assertEquals(creditorId, t1.getCreditorId());
        assertEquals(amount, t1.getAmount(), 1e-3);
        assertEquals(description, t1.getDescription());
        assertEquals(receiptId, t1.getReceiptId());

        String str = "New Description";
        assertThrows(NullPointerException.class, () -> t1.setDescription(null));
        assertThrows(IllegalArgumentException.class, () -> t1.setDescription("    1234"));
        t1.setDescription(str);
        assertEquals(str, t1.getDescription());
    }

}