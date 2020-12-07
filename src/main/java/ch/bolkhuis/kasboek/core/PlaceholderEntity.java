/**
 * Copyright (C) 2020 Aron Hoogeveen
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
package ch.bolkhuis.kasboek.core;

import org.jetbrains.annotations.NotNull;

public class PlaceholderEntity extends AccountingEntity {
    /**
     * Constructs a new PlaceholderEntity with {@code id} and {@code name}.
     * <br />
     * For the constraints of {@code id} and {@code name} see {@link AccountingEntity#isCorrectId(int)} and
     * {@link AccountingEntity#isCorrectName(String)} respectively.
     *
     * @param id          a unique (negative) identifier
     * @param name        the name of this AccountingEntry
     * @see AccountingEntity#isCorrectId(int)
     * @see AccountingEntity#isCorrectName(String)
     */
    public PlaceholderEntity(@NotNull String name) {
        super(999, name, AccountType.NON_EXISTENT, 0);
    }
}
