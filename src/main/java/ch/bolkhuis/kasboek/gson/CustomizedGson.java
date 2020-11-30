package ch.bolkhuis.kasboek.gson;

import ch.bolkhuis.kasboek.core.*;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

/**
 * @version v0.2-pre-alpha
 * @author Aron Hoogeveen
 */
public class CustomizedGson {
    private final static GsonBuilder builder = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
            .registerTypeAdapter(Transaction.class, new TransactionTypeAdapter())
            .registerTypeAdapter(AccountingEntity.class, new AccountingEntityTypeAdapter())
            .registerTypeAdapter(InmateEntity.class, new InmateEntityTypeAdapter())
            .registerTypeAdapter(HuischLedger.class, new HuischLedgerTypeAdapter())
            .registerTypeAdapter(Ledger.class, new LedgerTypeAdapter())
            .registerTypeAdapter(Receipt.class, new ReceiptTypeAdapter())
            .setPrettyPrinting(); // FIXME REMOVE THIS BEFORE PUBLISHING

    public final static Gson gson = builder.create();
}
