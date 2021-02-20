package ch.bolkhuis.kasboek.core2;

public class AccountingEntryTest {

    public static void main(String[] args) {
        Resident resident = new Resident(0, "Aron", 0, 0);
        Equity equity = (Equity)resident;

        System.out.println("Class for resident: " + resident.getClass());
        System.out.println("Class for equity: " + equity.getClass());
    }

}
