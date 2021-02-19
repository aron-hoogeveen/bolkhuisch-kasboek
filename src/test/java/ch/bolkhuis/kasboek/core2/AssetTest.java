package ch.bolkhuis.kasboek.core2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssetTest {

    @Test
    public void nullName() {
        assertThrows(NullPointerException.class, () -> new Asset(0, null, 0));
    }

    @Test
    public void testNameWithWhitespaces() {
        assertThrows(IllegalArgumentException.class, () -> new Asset(0, " whitespace", 0));
        assertThrows(IllegalArgumentException.class, () -> new Asset(0, "whitespace ", 0));
        assertDoesNotThrow(() -> new Asset(0, "Aron Hoogeveen", 0));
    }

    @Test
    public void testNameWithLeadingNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Asset(0, "0hello", 0));
    }

    @Test
    public void testNameWithIllegalSymbols() {
        assertThrows(IllegalArgumentException.class, () -> new Asset(0, "Hellow My@", 0));
    }

}