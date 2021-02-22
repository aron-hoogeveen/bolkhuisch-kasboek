package ch.bolkhuis.kasboek.core2;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {

    @Test
    public void testConstructorNPE() {
        assertThrows(NullPointerException.class, () -> new Receipt(null, 0, "Hello something", new HashSet<>(), 0));
        assertThrows(NullPointerException.class, () -> new Receipt(LocalDate.now(), 0, null, new HashSet<>(), 0));

        assertDoesNotThrow(() -> new Receipt(LocalDate.now(), 0, "Hello something", null, 0));
        assertDoesNotThrow(() -> new Receipt(LocalDate.now(), 0, "Some yes", new HashSet<>(), 0));
    }

    @Test
    public void testConstructorIAE() { // IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> new Receipt(LocalDate.now(), 0, "1", new HashSet<>(), 0));

        assertDoesNotThrow(() -> new Receipt(LocalDate.now(), 0, "Some yes", new HashSet<>(), 0));
    }

    @Test
    public void transactionKeySetUsesNaturalOrdering() {
        fail();
        // I am fairly certain that this is the case, so I do not want to write the test at this moment. It is not that important
    }

    @Test
    public void addKeySetFunctionality() {
        Receipt receipt = new Receipt(LocalDate.now(), 0, "Very good", new HashSet<>(), 0);
    }

}