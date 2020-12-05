package ch.bolkhuis.kasboek;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class RecentLedgerFile {
    private final File file;
    private final String name;

    /**
     * Creates a new RecentLedgerFile with the File location and a name.
     *
     * @param file the Ledger file location
     * @param name the name of the Ledger
     */
    public RecentLedgerFile(File file, @NotNull String name) {
        if (name == null) { throw new NullPointerException(); }

        this.file = file;
        this.name = name;
    }

    public File getFile() { return this.file; }
    public String getName() { return this.name; }
}
