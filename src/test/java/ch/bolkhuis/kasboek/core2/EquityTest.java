package ch.bolkhuis.kasboek.core2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquityTest {

    @Test
    public void nullName() {
        assertThrows(NullPointerException.class, () -> new Equity(0, null, 0));
    }

    @Test
    public void testNameWithWhitespaces() {
        assertThrows(IllegalArgumentException.class, () -> new Equity(0, " whitespace", 0));
        assertThrows(IllegalArgumentException.class, () -> new Equity(0, "whitespace ", 0));
        assertDoesNotThrow(() -> new Equity(0, "Aron Hoogeveen", 0));
    }

    @Test
    public void testNameWithLeadingNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Equity(0, "0hello", 0));
    }

    @Test
    public void testNameWithIllegalSymbols() {
        assertThrows(IllegalArgumentException.class, () -> new Equity(0, "Hellow My@", 0));
    }

    @Test
    public void debitAndCreditChangesCorrect() {
        Equity equity = new Equity(0, "Some Equity", 0);
        Equity afterCredit = equity.credit(20);
        Equity afterDebit = equity.debit(20);

        assertEquals(0, equity.getBalance());
        assertEquals(20, afterCredit.getBalance());
        assertEquals(-20, afterDebit.getBalance());
    }

    @Test
    public void testCompare() {
        Equity a0 = new Equity(10000, "aaaaaaaaaaa", 99);
        Equity a1 = new Equity(999, "Aron", 0);
        Equity a2 = new Equity(0, "Connor", 0);

        assertEquals(0, a1.compareTo(a1));
        assertEquals(-1, a1.compareTo(a2));
        assertEquals(1, a2.compareTo(a1));
        assertEquals(-1, a0.compareTo(a1));
    }

}