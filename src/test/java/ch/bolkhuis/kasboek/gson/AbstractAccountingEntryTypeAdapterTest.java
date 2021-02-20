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

package ch.bolkhuis.kasboek.gson;

import ch.bolkhuis.kasboek.core2.*;
import org.junit.jupiter.api.Test;

import static ch.bolkhuis.kasboek.gson.CustomizedGson.gson;
import static org.junit.jupiter.api.Assertions.*;

class AbstractAccountingEntryTypeAdapterTest {

    @Test
    public void testAsset() {
        Asset a1 = new Asset(0, "some name", 12);
        Asset a2 = (Asset)gson.fromJson(gson.toJson(a1), AbstractAccountingEntry.class);

        assertEquals(a1, a2);
    }

    @Test
    public void testDividend() {
        Dividend d1 = new Dividend(12, "yesjdfjd", 13);
        Dividend d2 = (Dividend)gson.fromJson(gson.toJson(d1), AbstractAccountingEntry.class);

        assertEquals(d1, d2);
    }

    @Test
    public void testEquity() {
        Equity d1 = new Equity(12, "yesjdfjd", 13);
        Equity d2 = (Equity)gson.fromJson(gson.toJson(d1), AbstractAccountingEntry.class);

        assertEquals(d1, d2);
    }

    @Test
    public void testExpense() {
        Expense d1 = new Expense(12, "yesjdfjd", 13);
        Expense d2 = (Expense)gson.fromJson(gson.toJson(d1), AbstractAccountingEntry.class);

        assertEquals(d1, d2);
    }

    @Test
    public void testLiability() {
        Liability d1 = new Liability(12, "yesjdfjd", 13);
        Liability d2 = (Liability)gson.fromJson(gson.toJson(d1), AbstractAccountingEntry.class);

        assertEquals(d1, d2);
    }

    @Test
    public void testResident() {
        Resident d1 = new Resident(12, "yesjdfjd", 13, 78);
        Resident d2 = (Resident)gson.fromJson(gson.toJson(d1), AbstractAccountingEntry.class);

        assertEquals(d1, d2);
    }

    @Test
    public void testRevenue() {
        Revenue d1 = new Revenue(12, "yesjdfjd", 13);
        Revenue d2 = (Revenue)gson.fromJson(gson.toJson(d1), AbstractAccountingEntry.class);

        assertEquals(d1, d2);
    }

}