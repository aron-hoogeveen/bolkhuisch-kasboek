package ch.bolkhuis.kasboek.core2;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringUtils {

    /**
     * Checks the provided String for various properties.<br />
     * <br />
     * More specifically this method checks if this String:<br />
     *   - is non-null;<br />
     *   - does not start or end with whitespaces<br />
     *   - has a length of at least 5<br />
     *
     * @param str the String to check
     * @return
     */
    public static boolean checkString(@Nullable String str) {
        if (str == null) return false;

        if (hasLeadingOrTrailingWhitespaces(str)) return false;

        return (str.length() >= 5);
    }

    /**
     * Returns whether or not the provided String starts or ends with whitespace characters.
     *
     * @param str the String to check
     * @return
     */
    public static boolean hasLeadingOrTrailingWhitespaces(@NotNull String str) {
        // TODO this can be optimized, but is it really worth it?
        String tmp = str.strip();
        return !str.equals(tmp);
    }

    /**
     * Checks the name if it adheres to the contract specified by this method. TODO write out to specification
     *
     * @param name the name to check
     * @return {@code true} if the name adheres to the contract, {@code false} otherwise
     */
    public static boolean checkName(String name) {
        return (name != null) && (!name.isBlank()) && (name.length() <= 256) && (!name.contains("\n"))
                && (!name.contains("\r")) && (name.matches("[a-zA-Z]+[a-zA-Z ]*[a-zA-Z]+"));
    }

}
