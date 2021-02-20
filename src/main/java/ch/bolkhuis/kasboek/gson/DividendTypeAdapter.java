package ch.bolkhuis.kasboek.gson;

import ch.bolkhuis.kasboek.core2.AbstractAccountingEntry;
import ch.bolkhuis.kasboek.core2.Asset;
import ch.bolkhuis.kasboek.core2.Dividend;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import static ch.bolkhuis.kasboek.gson.CustomizedGson.gson;

public class DividendTypeAdapter extends TypeAdapter<Dividend> {
    @Override
    public void write(JsonWriter jsonWriter, Dividend dividend) throws IOException {
        TypeAdapter<AbstractAccountingEntry> adapter = gson.getAdapter(AbstractAccountingEntry.class);
        adapter.write(jsonWriter, dividend);
    }

    @Override
    public Dividend read(JsonReader jsonReader) throws IOException {
        TypeAdapter<AbstractAccountingEntry> adapter = gson.getAdapter(AbstractAccountingEntry.class);
        return (Dividend)adapter.read(jsonReader);

    }
}
