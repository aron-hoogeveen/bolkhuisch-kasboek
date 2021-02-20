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
