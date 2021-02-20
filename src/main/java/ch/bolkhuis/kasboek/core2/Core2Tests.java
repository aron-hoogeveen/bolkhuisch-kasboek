package ch.bolkhuis.kasboek.core2;

import java.time.LocalDate;

import static ch.bolkhuis.kasboek.gson.CustomizedGson.gson;

public class Core2Tests {

    public static void main(String[] args) {
        /*
         * Test what kind of result is returned by the GSON object of the core2/Transaction
         */
        Transaction transaction = new Transaction(LocalDate.now(), 0, 12, 15, 35.99, "Some transacton!", null);

        String jsonStr = gson.toJson(transaction);
        System.out.println("This is the json for core2/Transaction:\n----------");
        System.out.println(jsonStr);
        System.out.println("----------");

        // Let's convert it back to a Transaction
        Transaction t2 = gson.fromJson(jsonStr, Transaction.class);
        if (transaction.equals(t2)) {
            System.out.println("Gson conversion successful!");
        } else {
            System.err.println("ERROR. gson converted object is not the same after deserialization");
        }
    }

}
