package ch.bolkhuis.kasboek.gson;

import ch.bolkhuis.kasboek.core.AccountType;
import ch.bolkhuis.kasboek.core.AccountingEntity;
import ch.bolkhuis.kasboek.core.InmateEntity;
import ch.bolkhuis.kasboek.core.Ledger;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;

public class GsonTester {
    private static Ledger ledger = new Ledger();

    public static void main(String[] args) {
        AccountingEntity inmate1 = new InmateEntity(
                ledger.getAndIncrementNextAccountingEntityId(),
                "Klaas",
                0,
                0
        );
        AccountingEntity inmate2 = new InmateEntity(
                ledger.getAndIncrementNextAccountingEntityId(),
                "Truus",
                0,
                0
        );
        AccountingEntity bank = new AccountingEntity(
                ledger.getAndIncrementNextAccountingEntityId(),
                "Bank",
                AccountType.ASSET,
                0
        );

        ledger.addAccountingEntity(inmate1);
        ledger.addAccountingEntity(inmate2);
        ledger.addAccountingEntity(bank);

        // try to write this to a file
        try {
            File file = new File("out/GsonTester_ledger.json");
            Ledger.toFile(file, ledger);

            // Try to construct it again
            ledger = Ledger.fromFile(file);
        } catch (IOException ioException) {
            System.out.println("A general IOException occurred");
            return;
        } catch (JsonSyntaxException jsonSyntaxException) {
            System.out.println("JSON Syntax malformation: " + jsonSyntaxException.getMessage());
            return;
        }

        System.out.println(Ledger.toJson(ledger));
    }
}
