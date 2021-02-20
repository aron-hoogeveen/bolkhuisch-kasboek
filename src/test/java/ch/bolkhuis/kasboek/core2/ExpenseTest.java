package ch.bolkhuis.kasboek.core2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {

    @Test
    public void nullName() {
        assertThrows(NullPointerException.class, () -> new Expense(0, null, 0));
    }

    @Test
    public void testNameWithWhitespaces() {
        assertThrows(IllegalArgumentException.class, () -> new Expense(0, " whitespace", 0));
        assertThrows(IllegalArgumentException.class, () -> new Expense(0, "whitespace ", 0));
        assertDoesNotThrow(() -> new Expense(0, "Aron Hoogeveen", 0));
    }

    @Test
    public void testNameWithLeadingNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Expense(0, "0hello", 0));
    }

    @Test
    public void testNameWithIllegalSymbols() {
        assertThrows(IllegalArgumentException.class, () -> new Expense(0, "Hellow My@", 0));
    }

    @Test
    public void debitAndCreditChangesCorrect() {
        Expense expense = new Expense(0, "Some Expense", 0);
        Expense afterCredit = expense.credit(20);
        Expense afterDebit = expense.debit(20);

        assertEquals(0, expense.getBalance());
        assertEquals(-20, afterCredit.getBalance());
        assertEquals(20, afterDebit.getBalance());
    }

    @Test
    public void testCompare() {
        Expense a0 = new Expense(10000, "aaaaaaaaaaa", 99);
        Expense a1 = new Expense(999, "Aron", 0);
        Expense a2 = new Expense(0, "Connor", 0);

        assertEquals(0, a1.compareTo(a1));
        assertEquals(-1, a1.compareTo(a2));
        assertEquals(1, a2.compareTo(a1));
        assertEquals(-1, a0.compareTo(a1));
    }

    @Test
    public void testEquality() {
        Expense expense = new Expense(0, "expense", 0);
        Expense other1 = new Expense(0, "expense", 0);
        Expense other2 = new Expense(0, "expense", 15);
        Expense other3 = new Expense(0, "different", 0);
        Expense other4 = new Expense(14, "expense", 0);

        assertEquals(expense, expense);
        assertEquals(expense, other1);
        assertEquals(expense, other2);
        assertNotEquals(expense, other3);
        assertNotEquals(expense, other4);
        assertNotEquals(expense, null);
    }

}