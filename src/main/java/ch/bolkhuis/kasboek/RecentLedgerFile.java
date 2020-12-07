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
package ch.bolkhuis.kasboek;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class RecentLedgerFile {
    private final File file;
    private final String name;

    /**
     * Creates a new RecentLedgerFile with the File location and a name.
     *
     * @param file the Ledger file location
     * @param name the name of the Ledger
     */
    public RecentLedgerFile(File file, @NotNull String name) {
        if (name == null) { throw new NullPointerException(); }

        this.file = file;
        this.name = name;
    }

    public File getFile() { return this.file; }
    public String getName() { return this.name; }
}
