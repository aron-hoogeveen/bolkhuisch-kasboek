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

public class AccountingEntryNameCheck {

    /**
     * Checks the name if it adheres to the contract specified by this class. TODO write out to specification
     *
     * @param name the name to check
     * @return {@code true} if the name adheres to the contract, {@code false} otherwise
     */
    public static boolean checkName(String name) {
        return (name != null) && (!name.isBlank()) && (name.length() <= 256) && (!name.contains("\n"))
                && (!name.contains("\r")) && (name.matches("[a-zA-Z]+[a-zA-Z ]*[a-zA-Z]+"));
    }

}
