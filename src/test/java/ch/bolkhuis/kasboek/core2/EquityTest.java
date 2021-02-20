/*
 * Copyright (C) 2021 Aron Hoogeveen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

    @Test
    public void testEquality() {
        Equity equity = new Equity(0, "equity", 0);
        Equity other1 = new Equity(0, "equity", 0);
        Equity other2 = new Equity(0, "equity", 15);
        Equity other3 = new Equity(0, "different", 0);
        Equity other4 = new Equity(14, "equity", 0);

        assertEquals(equity, equity);
        assertEquals(equity, other1);
        assertEquals(equity, other2);
        assertNotEquals(equity, other3);
        assertNotEquals(equity, other4);
        assertNotEquals(equity, null);
    }

}