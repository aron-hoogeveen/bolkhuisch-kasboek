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

import java.time.LocalDate;

import static ch.bolkhuis.kasboek.gson.CustomizedGson.gson;

public class Core2Tests {

    public static void main(String[] args) {
        /*
         * Test what kind of result is returned by the GSON object of the core2/Transaction
         */
        Transaction transaction = new Transaction(LocalDate.now(), 0, 12, 15, 35.99, "Some transacton!", null);

        String jsonStr = gson.toJson(transaction);
        System.out.println("This is the json for core2/Transaction:\n----------");
        System.out.println(jsonStr);
        System.out.println("----------");

        // Let's convert it back to a Transaction
        Transaction t2 = gson.fromJson(jsonStr, Transaction.class);
        if (transaction.equals(t2)) {
            System.out.println("Gson conversion successful!");
        } else {
            System.err.println("ERROR. gson converted object is not the same after deserialization");
        }
    }

}
