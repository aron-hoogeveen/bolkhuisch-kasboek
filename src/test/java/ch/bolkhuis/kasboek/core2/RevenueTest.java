package ch.bolkhuis.kasboek.core2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RevenueTest {

    @Test
    public void nullName() {
        assertThrows(NullPointerException.class, () -> new Revenue(0, null, 0));
    }

    @Test
    public void testNameWithWhitespaces() {
        assertThrows(IllegalArgumentException.class, () -> new Revenue(0, " whitespace", 0));
        assertThrows(IllegalArgumentException.class, () -> new Revenue(0, "whitespace ", 0));
        assertDoesNotThrow(() -> new Revenue(0, "Aron Hoogeveen", 0));
    }

    @Test
    public void testNameWithLeadingNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Revenue(0, "0hello", 0));
    }

    @Test
    public void testNameWithIllegalSymbols() {
        assertThrows(IllegalArgumentException.class, () -> new Revenue(0, "Hellow My@", 0));
    }

    @Test
    public void debitAndCreditChangesCorrect() {
        Revenue revenue = new Revenue(0, "Some Revenue", 0);
        Revenue afterCredit = revenue.credit(20);
        Revenue afterDebit = revenue.debit(20);

        assertEquals(0, revenue.getBalance());
        assertEquals(20, afterCredit.getBalance());
        assertEquals(-20, afterDebit.getBalance());
    }

    @Test
    public void testCompare() {
        Revenue a0 = new Revenue(10000, "aaaaaaaaaaa", 99);
        Revenue a1 = new Revenue(999, "Aron", 0);
        Revenue a2 = new Revenue(0, "Connor", 0);

        assertEquals(0, a1.compareTo(a1));
        assertEquals(-1, a1.compareTo(a2));
        assertEquals(1, a2.compareTo(a1));
        assertEquals(-1, a0.compareTo(a1));
    }

}