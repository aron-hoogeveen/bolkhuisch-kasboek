package ch.bolkhuis.kasboek.gson;

import ch.bolkhuis.kasboek.core2.AbstractAccountingEntry;
import ch.bolkhuis.kasboek.core2.Asset;
import ch.bolkhuis.kasboek.core2.Equity;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import static ch.bolkhuis.kasboek.gson.CustomizedGson.gson;

public class EquityTypeAdapter extends TypeAdapter<Equity> {
    @Override
    public void write(JsonWriter jsonWriter, Equity equity) throws IOException {
        TypeAdapter<AbstractAccountingEntry> adapter = gson.getAdapter(AbstractAccountingEntry.class);
        adapter.write(jsonWriter, equity);
    }

    @Override
    public Equity read(JsonReader jsonReader) throws IOException {
        TypeAdapter<AbstractAccountingEntry> adapter = gson.getAdapter(AbstractAccountingEntry.class);
        return (Equity)adapter.read(jsonReader);

    }
}
