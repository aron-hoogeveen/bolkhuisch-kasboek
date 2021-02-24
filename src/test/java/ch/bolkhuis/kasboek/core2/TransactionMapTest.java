package ch.bolkhuis.kasboek.core2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

class TransactionMapTest {

    @Test
    public void putRemoveAndSizeTransactions() {
        TransactionMap map = new TransactionMap();
        assertEquals(0, map.size());

        assertThrows(NullPointerException.class, () -> map.putTransaction(null));
        assertThrows(NullPointerException.class, () -> map.removeTransaction(null));

        Transaction t1 = new Transaction(LocalDate.of(2020, 1, 24), 0, 0, 1, 42, "My Birthday", null);
        Transaction t2 = new Transaction(LocalDate.of(2020, 1, 24), 1, 1, 0, 42, "Geld terug, boef!", null);
        Transaction t3 = new Transaction(LocalDate.of(2020, 1, 25), 0, 34, 2, 99.99, "Veel geld.", null);

        assertNull(map.putTransaction(t1));
        assertEquals(1, map.size());
        assertNull(map.putTransaction(t2));
        assertNull(map.putTransaction(t3));
        assertTrue(map.containsTransactionKey(t1));
        assertTrue(map.containsTransactionKey(t2));
        assertTrue(map.containsTransactionKey(t3));
        Transaction old = map.removeTransaction(t1);
        assertEquals(2, map.size());
        assertEquals(t1, old);
        map.removeTransaction(t3);
        assertFalse(map.containsTransactionKey(t1));
        assertTrue(map.containsTransactionKey(t2));
        assertFalse(map.containsTransactionKey(t3));
    }

    @Test
    public void getSubMap() {
        Transaction t1 = new Transaction(LocalDate.of(2021, 1, 1), 0, 0, 1, 24, "Very good", null);
        Transaction t2 = new Transaction(LocalDate.of(2021, 2, 1), 0, 0, 1, 24, "very good", null);
        Transaction t3 = new Transaction(LocalDate.of(2021, 2, 25), 0, 0, 1, 24, "Something Something", null);
        Transaction t4 = new Transaction(LocalDate.of(2021, 2, 1), 1, 0, 1, 24, "Also good", null);
        Transaction t5 = new Transaction(LocalDate.of(2021, 2, 28), 0, 0, 1, 24, "YesYesYes", null);

        TransactionMap map = new TransactionMap();
        assertThrows(NullPointerException.class, () -> map.subListTransactions(null, LocalDate.of(2021, 1, 1)));
        assertThrows(NullPointerException.class, () -> map.subListTransactions(LocalDate.of(2021, 1, 1), null));
        map.putTransaction(t1);
        map.putTransaction(t2);
        map.putTransaction(t3);
        map.putTransaction(t4);
        map.putTransaction(t5);

        List<Transaction> subList = map.subListTransactions(LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 26));
        assertTrue(subList.containsAll(List.of(
                t2,
                t3,
                t4
        )));
        assertFalse(subList.contains(t1));
        assertFalse(subList.contains(t5));

        // subList should be in the correct order
        assertEquals(t2, subList.get(0));
        assertEquals(t4, subList.get(1));
        assertEquals(t3, subList.get(2));
    }

    @Test
    public void testGetHighestId() {
        LocalDate date = LocalDate.of(2021, 1, 1);
        Transaction t1 = new Transaction(date, 41, 0, 1, 12, "Yesyess", null);
        TransactionMap map = new TransactionMap();
        assertThrows(NullPointerException.class, () -> map.highestTransactionId(null));

        map.putTransaction(t1);
        assertEquals(t1.getId(), map.highestTransactionId(date));
        assertEquals(-1, map.highestTransactionId(LocalDate.of(2000, 1, 1)));
    }

    @Test
    public void testContainsAndGet() {
        TransactionMap map = new TransactionMap();
        assertThrows(NullPointerException.class, () -> map.containsTransactionKey(null));
        assertThrows(NullPointerException.class, () -> map.containsTransaction(null));
        assertThrows(NullPointerException.class, () -> map.getTransaction(null));
        Transaction t1 = new Transaction(LocalDate.of(2021, 2, 24), 0, 0, 1, 23, "Descri", null);
        Transaction t2 = new Transaction(LocalDate.of(2021, 2, 24), 0, 5, 2, 33, "A different", null);
        map.putTransaction(t1);

        assertTrue(map.containsTransactionKey(t1));
        assertTrue(map.containsTransaction(t1));
        assertTrue(map.containsTransactionKey(t2));
        assertFalse(map.containsTransaction(t2));

        assertEquals(t1, map.getTransaction(t1));
        Transaction old = map.putTransaction(t2);
        assertEquals(t1, old);
        assertTrue(map.containsTransactionKey(t1));
        assertFalse(map.containsTransaction(t1));
        assertTrue(map.containsTransactionKey(t2));
        assertTrue(map.containsTransaction(t2));
    }

}