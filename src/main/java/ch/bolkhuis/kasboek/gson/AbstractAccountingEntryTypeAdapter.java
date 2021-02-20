package ch.bolkhuis.kasboek.gson;

import ch.bolkhuis.kasboek.core2.*;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * The AbstractAccountingEntryTypeAdapter is used to (de)serialize all of the following sub-classes:
 *   - Asset
 *   - Dividend
 *   - Equity
 *   - Expense
 *   - Liability
 *   - Resident
 *   - Revenue
 * It accomplishes this by saving the canonical name of the class in the serialized json.
 */
public class AbstractAccountingEntryTypeAdapter extends TypeAdapter<AbstractAccountingEntry> {

    private enum FieldNames {
        TYPE("type"),
        ID("id"),
        NAME("name"),
        BALANCE("balance"),
        PREVIOUS_BALANCE("previous_balance");

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
    public void write(JsonWriter jsonWriter, AbstractAccountingEntry entry) throws IOException {
        if (entry == null) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.beginObject();
        jsonWriter.name(FieldNames.TYPE.name).value(entry.getClass().getCanonicalName());
        jsonWriter.name(FieldNames.ID.name).value(entry.getId());
        jsonWriter.name(FieldNames.NAME.name).value(entry.getName());
        jsonWriter.name(FieldNames.BALANCE.name).value(entry.getBalance());
        if (entry.getClass() == Resident.class) {
            jsonWriter.name(FieldNames.PREVIOUS_BALANCE.name).value(((Resident)entry).getPreviousBalance());
        }
        jsonWriter.endObject();
    }

    @Override
    public AbstractAccountingEntry read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        String canonicalName = "";
        int id = 0;
        String name = null;
        double balance = 0.0;
        double previousBalance = 0.0;

        // used for keeping track of all encountered fields
        int fields = 0;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            if (jsonReader.peek() == JsonToken.NAME) {
                String fieldName = jsonReader.nextName();
                if (fieldName.equals(FieldNames.TYPE.name)) {
                    fields |= FieldNames.ID.getValue();
                    canonicalName = jsonReader.nextString();
                } else if (fieldName.equals(FieldNames.ID.name)) {
                    fields |= FieldNames.ID.getValue();
                    id = jsonReader.nextInt();
                }
                else if (fieldName.equals(FieldNames.NAME.name)) {
                    fields |= FieldNames.NAME.getValue();
                    name = jsonReader.nextString();
                } else if (fieldName.equals(FieldNames.BALANCE.name)) {
                    fields |= FieldNames.BALANCE.getValue();
                    balance = jsonReader.nextDouble();
                } else if (fieldName.equals(FieldNames.PREVIOUS_BALANCE.name)) {
                    fields |= FieldNames.PREVIOUS_BALANCE.getValue();
                    previousBalance = jsonReader.nextDouble();
                } else {
                    // unrecognized field
                    jsonReader.skipValue();
                }
            } else if (jsonReader.peek() == JsonToken.END_OBJECT) {
                break;
            } else {
                throw new IOException("Unexpected token: " + jsonReader.peek());
            }
        }
        jsonReader.endObject();

        // little hack for including the previousBalance field even if it was not encountered. This could mean that if
        // not correct implemented, the previousBalance value could get lost
        fields |= FieldNames.PREVIOUS_BALANCE.getValue();

        int fieldCheck = 0;
        for (FieldNames fieldName : FieldNames.values()) {
            fieldCheck |= fieldName.getValue();
        }

        // try catch could also be replaced by inserting null checks, but nah
        try {
            if (fieldCheck == fields) {
                // return the correct AbstractAccountingEntry
                if (canonicalName.equals(Asset.class.getCanonicalName())) {
                    return new Asset(id, name, balance);
                }
                if (canonicalName.equals(Dividend.class.getCanonicalName())) {
                    return new Dividend(id, name, balance);
                }
                if (canonicalName.equals(Equity.class.getCanonicalName())) {
                    return new Equity(id, name, balance);
                }
                if (canonicalName.equals(Expense.class.getCanonicalName())) {
                    return new Expense(id, name, balance);
                }
                if (canonicalName.equals(Liability.class.getCanonicalName())) {
                    return new Liability(id, name, balance);
                }
                if (canonicalName.equals(Resident.class.getCanonicalName())) {
                    return new Resident(id, name, balance, previousBalance);
                }
                if (canonicalName.equals(Revenue.class.getCanonicalName())) {
                    return new Revenue(id, name, balance);
                }
            }
        } catch (NullPointerException e) {
            throw new IOException("Some fields are null that must be non-null");
        }
        throw new IOException("Not all required fields are available");
    }
}
