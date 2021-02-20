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

import ch.bolkhuis.kasboek.core2.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static ch.bolkhuis.kasboek.gson.CustomizedGson.gson;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeAdapterTest {

    @Test
    public void testJsonReceiptNull() {
        Transaction transaction = new Transaction(LocalDate.now(), 0, 12, 15, 35.99, "Some transacton!", null);
        String jsonStr = gson.toJson(transaction);

        // Let's convert it back to a Transaction
        Transaction t2 = gson.fromJson(jsonStr, Transaction.class);
        assertEquals(transaction, t2);
    }

    @Test
    public void testJsonNonNullReceipt() {
        Transaction transaction = new Transaction(LocalDate.now(), 0, 12, 15, 15.99, "Yes!!!!!", 27);
        String jsonStr = gson.toJson(transaction);
        Transaction t2 = gson.fromJson(jsonStr, Transaction.class);
        assertEquals(transaction, t2);
    }

}