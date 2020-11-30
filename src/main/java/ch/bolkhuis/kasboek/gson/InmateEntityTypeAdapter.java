package ch.bolkhuis.kasboek.gson;

import ch.bolkhuis.kasboek.core.AccountType;
import ch.bolkhuis.kasboek.core.InmateEntity;
import ch.bolkhuis.kasboek.gson.CustomizedGson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Objects;

/**
 * @version v0.2-pre-alpha
 * @author Aron Hoogeveen
 */
public class InmateEntityTypeAdapter extends TypeAdapter<InmateEntity> {
    private enum FieldNames {
        ID("id"),
        NAME("name"),
        PREVIOUS_BALANCE("previous_balance"),
        BALANCE("balance");

        private final String name;

        FieldNames(String name) {
            this.name = name;
        }

        /**
         * Returns the ordinal + 1 for use in ORing.
         *
         * @return value greater than zero
         */
        private int getValue() {
            return ordinal() + 1;
        }
    }

    @Override
    public void write(JsonWriter jsonWriter, InmateEntity inmateEntity) throws IOException {
        if (inmateEntity == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginObject();
        jsonWriter.name(FieldNames.ID.name).value(inmateEntity.getId());
        jsonWriter.name(FieldNames.NAME.name).value(inmateEntity.getName());
        jsonWriter.name(FieldNames.PREVIOUS_BALANCE.name).value(inmateEntity.getPreviousBalance());
        jsonWriter.name(FieldNames.BALANCE.name).value(inmateEntity.getBalance());
        jsonWriter.endObject();
    }

    @Override
    public InmateEntity read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        int id = 0;
        String name = null;
        AccountType accountType = null;
        double previousBalance = 0;
        double balance = 0;
        // fields is used for checking if all fields are available
        int fields = 0;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            if (jsonReader.peek() == JsonToken.NAME) {
                String fieldName = jsonReader.nextName();
                if (fieldName.equals(FieldNames.ID.name)) {
                    fields |= FieldNames.ID.getValue();
                    id = jsonReader.nextInt();
                }
                else if (fieldName.equals(FieldNames.NAME.name)) {
                    fields |= FieldNames.NAME.getValue();
                    name = jsonReader.nextString();
                }
                else if (fieldName.equals(FieldNames.PREVIOUS_BALANCE.name)) {
                    fields |= FieldNames.PREVIOUS_BALANCE.getValue();
                    previousBalance = jsonReader.nextDouble();
                }
                else if (fieldName.equals(FieldNames.BALANCE.name)) {
                    fields |= FieldNames.BALANCE.getValue();
                    balance = jsonReader.nextDouble();
                }
                else {
                    // unknown field
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
            return new InmateEntity(
                    id,
                    Objects.requireNonNull(name, "name should not be null at this point"),
                    previousBalance,
                    balance);
        }
        throw new IOException("Not all required fields are available");
    }
}
