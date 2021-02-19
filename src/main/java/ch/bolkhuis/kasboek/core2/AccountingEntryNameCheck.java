package ch.bolkhuis.kasboek.core2;

public class AccountingEntryNameCheck {

    /**
     * Checks the name if it adheres to the contract specified by this class. TODO write out to specification
     *
     * @param name the name to check
     * @return {@code true} if the name adheres to the contract, {@code false} otherwise
     */
    public static boolean checkName(String name) {
        return (name != null) && (!name.isBlank()) && (name.length() <= 256) && (!name.contains("\n"))
                && (!name.contains("\r")) && (name.matches("[a-zA-Z]+[a-zA-Z ]*[a-zA-Z]+"));
    }

}
