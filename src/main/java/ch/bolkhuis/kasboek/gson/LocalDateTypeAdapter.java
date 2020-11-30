package ch.bolkhuis.kasboek.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @version v0.2-pre-alpha
 * @author Aron Hoogeveen
 */
public class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {
    private enum FieldNames {
        DATE("date");

        private final String name;

        FieldNames(String name) {
            this.name = name;
        }

        public int getValue() {
            return ordinal() + 1;
        }
    }

    @Override
    public void write(JsonWriter jsonWriter, LocalDate localDateTime) throws IOException {
        if (localDateTime == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginObject();
        jsonWriter.name(FieldNames.DATE.name);
        jsonWriter.value(localDateTime.toString());
        jsonWriter.endObject();
    }

    @Override
    public LocalDate read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == null) {
            jsonReader.nextNull();
            return null;
        }
        LocalDateTime dateTime;
        String date = null;
        int fields = 0;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            if (jsonReader.peek() == JsonToken.NAME) {
                String fieldName = jsonReader.nextName();
                if (fieldName.equals(FieldNames.DATE.name)) {
                    fields |= FieldNames.DATE.getValue();
                    date = jsonReader.nextString();
                }
                else {
                    // unrecognized NAME
                    jsonReader.skipValue();
                }
            }
            else if (jsonReader.peek() == JsonToken.END_OBJECT) {
                break;
            }
            else {
                throw new IOException("Did not expect token " + jsonReader.peek());
            }
        }
        jsonReader.endObject();

        int fieldCheck = 0;
        for (FieldNames fieldName : FieldNames.values()) {
            fieldCheck |= fieldName.getValue();
        }

        if (fields == fieldCheck) {
            return LocalDate.parse(date);
        }
        throw new IOException("Not all required fields are available");
    }
}
