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

class LiabilityTest {

    @Test
    public void nullName() {
        assertThrows(NullPointerException.class, () -> new Liability(0, null, 0));
    }

    @Test
    public void testNameWithWhitespaces() {
        assertThrows(IllegalArgumentException.class, () -> new Liability(0, " whitespace", 0));
        assertThrows(IllegalArgumentException.class, () -> new Liability(0, "whitespace ", 0));
        assertDoesNotThrow(() -> new Liability(0, "Aron Hoogeveen", 0));
    }

    @Test
    public void testNameWithLeadingNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Liability(0, "0hello", 0));
    }

    @Test
    public void testNameWithIllegalSymbols() {
        assertThrows(IllegalArgumentException.class, () -> new Liability(0, "Hellow My@", 0));
    }

    @Test
    public void debitAndCreditChangesCorrect() {
        Liability liability = new Liability(0, "Some Liability", 0);
        Liability afterCredit = liability.credit(20);
        Liability afterDebit = liability.debit(20);

        assertEquals(0, liability.getBalance());
        assertEquals(20, afterCredit.getBalance());
        assertEquals(-20, afterDebit.getBalance());
    }

    @Test
    public void testCompare() {
        Liability a0 = new Liability(10000, "aaaaaaaaaaa", 99);
        Liability a1 = new Liability(999, "Aron", 0);
        Liability a2 = new Liability(0, "Connor", 0);

        assertEquals(0, a1.compareTo(a1));
        assertEquals(-1, a1.compareTo(a2));
        assertEquals(1, a2.compareTo(a1));
        assertEquals(-1, a0.compareTo(a1));
    }

    @Test
    public void testEquality() {
        Liability liability = new Liability(0, "liability", 0);
        Liability other1 = new Liability(0, "liability", 0);
        Liability other2 = new Liability(0, "liability", 15);
        Liability other3 = new Liability(0, "different", 0);
        Liability other4 = new Liability(14, "liability", 0);

        assertEquals(liability, liability);
        assertEquals(liability, other1);
        assertEquals(liability, other2);
        assertNotEquals(liability, other3);
        assertNotEquals(liability, other4);
        assertNotEquals(liability, null);
    }

}