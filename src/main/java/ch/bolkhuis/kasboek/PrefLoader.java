package ch.bolkhuis.kasboek;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.prefs.Preferences;

/**
 * Utility class for improved user experience with using the Preferences API
 *
 * TODO add JavaDoc for all methods
 */
public class PrefLoader {

    /**
     * Returns the saved String or a default value defined in {@code prefName}.
     *
     * @param pref Preferences Node
     * @param prefName PrefEntry containing the key and default value
     * @return the Preferences string or the default value
     * @throws ClassCastException if {@code prefName}'s {@link PrefEntry#getDefault} cannot be case to String
     * @throws NullPointerException if either {@code pref} or {@code prefName} is null
     */
    public static String get(@NotNull Preferences pref, @NotNull PrefEntry prefName) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        return pref.get(prefName.getName(), (String)prefName.getDefault());
    }

    public static void put(@NotNull Preferences pref, @NotNull PrefEntry prefName, String value) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        pref.put(prefName.getName(), value);
    }

    public static boolean getBoolean(@NotNull Preferences pref, @NotNull PrefEntry prefName) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        return pref.getBoolean(prefName.getName(), (boolean)prefName.getDefault());
    }

    public static void putBoolean(@NotNull Preferences pref, @NotNull PrefEntry prefName, boolean value) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        pref.putBoolean(prefName.getName(), value);
    }

    public static byte[] getByteArray(@NotNull Preferences pref, @NotNull PrefEntry prefName) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        return pref.getByteArray(prefName.getName(), (byte[])prefName.getDefault());
    }

    public static void putByteArray(@NotNull Preferences pref, @NotNull PrefEntry prefName, byte[] value) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        pref.putByteArray(prefName.getName(), value);
    }

    public static double getDouble(@NotNull Preferences pref, @NotNull PrefEntry prefName) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        return pref.getDouble(prefName.getName(), (double)prefName.getDefault());
    }

    public static void putDouble(@NotNull Preferences pref, @NotNull PrefEntry prefName, double value) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        pref.putDouble(prefName.getName(), value);
    }

    public static float getFloat(@NotNull Preferences pref, @NotNull PrefEntry prefName) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        return pref.getFloat(prefName.getName(), (float)prefName.getDefault());
    }

    public static void putFloat(@NotNull Preferences pref, @NotNull PrefEntry prefName, float value) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        pref.putFloat(prefName.getName(), value);
    }

    public static int getInt(@NotNull Preferences pref, @NotNull PrefEntry prefName) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        return pref.getInt(prefName.getName(), (int)prefName.getDefault());
    }

    public static void putInt(@NotNull Preferences pref, @NotNull PrefEntry prefName, int value) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        pref.putInt(prefName.getName(), value);
    }

    public static long getLong(@NotNull Preferences pref, @NotNull PrefEntry prefName) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        return pref.getLong(prefName.getName(), (long)prefName.getDefault());
    }

    public static void putLong(@NotNull Preferences pref, @NotNull PrefEntry prefName, long value) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        pref.putLong(prefName.getName(), value);
    }

    public static void remove(@NotNull Preferences pref, @NotNull PrefEntry prefName) {
        Objects.requireNonNull(pref);
        Objects.requireNonNull(prefName);

        pref.remove(prefName.getName());
    }

}
