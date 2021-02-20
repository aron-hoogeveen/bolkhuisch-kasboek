package ch.bolkhuis.kasboek.core2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResidentTest {

    @Test
    public void nullName() {
        assertThrows(NullPointerException.class, () -> new Resident(0, null, 0, 0));
    }

    @Test
    public void testNameWithWhitespaces() {
        assertThrows(IllegalArgumentException.class, () -> new Resident(0, " whitespace", 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Resident(0, "whitespace ", 0, 0));
        assertDoesNotThrow(() -> new Resident(0, "Aron Hoogeveen", 0, 0));
    }

    @Test
    public void testNameWithLeadingNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Resident(0, "0hello", 0, 0));
    }

    @Test
    public void testNameWithIllegalSymbols() {
        assertThrows(IllegalArgumentException.class, () -> new Resident(0, "Hellow My@", 0, 0));
    }

    @Test
    public void debitAndCreditChangesCorrect() {
        Resident resident = new Resident(0, "Some Resident", 0, 0);
        Resident afterCredit = resident.credit(20);
        Resident afterDebit = resident.debit(20);

        assertEquals(0, resident.getBalance());
        assertEquals(20, afterCredit.getBalance());
        assertEquals(-20, afterDebit.getBalance());
    }

    @Test
    public void testCompare() {
        Resident a0 = new Resident(10000, "aaaaaaaaaaa", 99, 0);
        Resident a1 = new Resident(999, "Aron", 0, 0);
        Resident a2 = new Resident(0, "Connor", 0, 0);

        assertEquals(0, a1.compareTo(a1));
        assertEquals(-1, a1.compareTo(a2));
        assertEquals(1, a2.compareTo(a1));
        assertEquals(-1, a0.compareTo(a1));
    }

    @Test
    public void residentNotEqualToEquity() {
        Resident resident = new Resident(0, "Aron", 0, 0);
        Equity equity = (Equity)resident;
        Equity otherEquity = new Equity(0, "Aron", 0);

        assertEquals(resident, equity);
        assertEquals(equity, resident);
        assertNotEquals(resident, otherEquity);
        assertNotEquals(otherEquity, resident);
    }
    
}