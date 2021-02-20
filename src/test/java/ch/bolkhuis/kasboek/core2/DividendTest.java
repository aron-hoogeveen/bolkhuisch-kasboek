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

class DividendTest {

    @Test
    public void nullName() {
        assertThrows(NullPointerException.class, () -> new Dividend(0, null, 0));
    }

    @Test
    public void testNameWithWhitespaces() {
        assertThrows(IllegalArgumentException.class, () -> new Dividend(0, " whitespace", 0));
        assertThrows(IllegalArgumentException.class, () -> new Dividend(0, "whitespace ", 0));
        assertDoesNotThrow(() -> new Dividend(0, "Aron Hoogeveen", 0));
    }

    @Test
    public void testNameWithLeadingNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Dividend(0, "0hello", 0));
    }

    @Test
    public void testNameWithIllegalSymbols() {
        assertThrows(IllegalArgumentException.class, () -> new Dividend(0, "Hellow My@", 0));
    }

    @Test
    public void debitAndCreditChangesCorrect() {
        Dividend dividend = new Dividend(0, "Some Dividend", 0);
        Dividend afterCredit = dividend.credit(20);
        Dividend afterDebit = dividend.debit(20);

        assertEquals(0, dividend.getBalance());
        assertEquals(-20, afterCredit.getBalance());
        assertEquals(20, afterDebit.getBalance());
    }

    @Test
    public void testCompare() {
        Dividend a0 = new Dividend(10000, "aaaaaaaaaaa", 99);
        Dividend a1 = new Dividend(999, "Aron", 0);
        Dividend a2 = new Dividend(0, "Connor", 0);

        assertEquals(0, a1.compareTo(a1));
        assertEquals(-1, a1.compareTo(a2));
        assertEquals(1, a2.compareTo(a1));
        assertEquals(-1, a0.compareTo(a1));
    }

    @Test
    public void testEquality() {
        Dividend dividend = new Dividend(0, "dividend", 0);
        Dividend other1 = new Dividend(0, "dividend", 0);
        Dividend other2 = new Dividend(0, "dividend", 15);
        Dividend other3 = new Dividend(0, "different", 0);
        Dividend other4 = new Dividend(14, "dividend", 0);

        assertEquals(dividend, dividend);
        assertEquals(dividend, other1);
        assertEquals(dividend, other2);
        assertNotEquals(dividend, other3);
        assertNotEquals(dividend, other4);
        assertNotEquals(dividend, null);
    }
    
}