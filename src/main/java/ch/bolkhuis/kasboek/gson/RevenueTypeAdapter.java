package ch.bolkhuis.kasboek.gson;

import ch.bolkhuis.kasboek.core2.AbstractAccountingEntry;
import ch.bolkhuis.kasboek.core2.Asset;
import ch.bolkhuis.kasboek.core2.Revenue;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import static ch.bolkhuis.kasboek.gson.CustomizedGson.gson;

public class RevenueTypeAdapter extends TypeAdapter<Revenue> {
    @Override
    public void write(JsonWriter jsonWriter, Revenue revenue) throws IOException {
        TypeAdapter<AbstractAccountingEntry> adapter = gson.getAdapter(AbstractAccountingEntry.class);
        adapter.write(jsonWriter, revenue);
    }

    @Override
    public Revenue read(JsonReader jsonReader) throws IOException {
        TypeAdapter<AbstractAccountingEntry> adapter = gson.getAdapter(AbstractAccountingEntry.class);
        return (Revenue)adapter.read(jsonReader);

    }
}
