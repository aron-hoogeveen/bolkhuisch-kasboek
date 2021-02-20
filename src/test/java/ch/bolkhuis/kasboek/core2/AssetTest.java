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

class AssetTest {

    @Test
    public void nullName() {
        assertThrows(NullPointerException.class, () -> new Asset(0, null, 0));
    }

    @Test
    public void testNameWithWhitespaces() {
        assertThrows(IllegalArgumentException.class, () -> new Asset(0, " whitespace", 0));
        assertThrows(IllegalArgumentException.class, () -> new Asset(0, "whitespace ", 0));
        assertDoesNotThrow(() -> new Asset(0, "Aron Hoogeveen", 0));
    }

    @Test
    public void testNameWithLeadingNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Asset(0, "0hello", 0));
    }

    @Test
    public void testNameWithIllegalSymbols() {
        assertThrows(IllegalArgumentException.class, () -> new Asset(0, "Hellow My@", 0));
    }

    @Test
    public void debitAndCreditChangesCorrect() {
        Asset asset = new Asset(0, "Some Asset", 0);
        Asset afterCredit = asset.credit(20);
        Asset afterDebit = asset.debit(20);

        assertEquals(0, asset.getBalance());
        assertEquals(-20, afterCredit.getBalance());
        assertEquals(20, afterDebit.getBalance());
    }

    @Test
    public void testCompare() {
        Asset a0 = new Asset(10000, "aaaaaaaaaaa", 99);
        Asset a1 = new Asset(999, "Aron", 0);
        Asset a2 = new Asset(0, "Connor", 0);

        assertEquals(0, a1.compareTo(a1));
        assertEquals(-1, a1.compareTo(a2));
        assertEquals(1, a2.compareTo(a1));
        assertEquals(-1, a0.compareTo(a1));
    }

    @Test
    public void testEquality() {
        Asset Asset = new Asset(0, "Asset", 0);
        Asset other1 = new Asset(0, "Asset", 0);
        Asset other2 = new Asset(0, "Asset", 15);
        Asset other3 = new Asset(0, "different", 0);
        Asset other4 = new Asset(14, "Asset", 0);

        assertEquals(Asset, Asset);
        assertEquals(Asset, other1);
        assertEquals(Asset, other2);
        assertNotEquals(Asset, other3);
        assertNotEquals(Asset, other4);
        assertNotEquals(Asset, null);
    }

}