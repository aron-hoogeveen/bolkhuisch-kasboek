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

import ch.bolkhuis.kasboek.core2.AbstractAccountingEntry;
import ch.bolkhuis.kasboek.core2.Asset;
import ch.bolkhuis.kasboek.core2.Resident;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import static ch.bolkhuis.kasboek.gson.CustomizedGson.gson;

public class ResidentTypeAdapter extends TypeAdapter<Resident> {
    @Override
    public void write(JsonWriter jsonWriter, Resident resident) throws IOException {
        TypeAdapter<AbstractAccountingEntry> adapter = gson.getAdapter(AbstractAccountingEntry.class);
        adapter.write(jsonWriter, resident);
    }

    @Override
    public Resident read(JsonReader jsonReader) throws IOException {
        TypeAdapter<AbstractAccountingEntry> adapter = gson.getAdapter(AbstractAccountingEntry.class);
        return (Resident)adapter.read(jsonReader);

    }
}
