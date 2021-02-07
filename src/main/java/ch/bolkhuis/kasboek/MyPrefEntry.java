package ch.bolkhuis.kasboek;

import org.jetbrains.annotations.NotNull;

public class MyPrefEntry implements PrefEntry {
    public static MyPrefEntry BASE_DIR = new MyPrefEntry(
            "base_dir",
            System.getProperty("user.home") + "/.kasboek/"
    );

    private final String name;
    private final Object def;

    public MyPrefEntry(@NotNull String name, Object def) {
        this.name = name;
        this.def = def;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getDefault() {
        return def;
    }

    @Override
    public String toString() {
        return name;
    }
}
