package persistence;

import model.Event;
import model.Journal;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Journal journal = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testReaderEmptyJournal() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyJournal.json");
        try {
            Journal journal = reader.read();
            assertEquals(2020, journal.getYear());
            assertEquals(10, journal.getMonth());
            assertEquals(15, journal.getDay());
            assertEquals("my journal", journal.getTitle());
            assertTrue(journal.displayEvents().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralJournal() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralJournal.json");
        try {
            Journal journal = reader.read();
            assertEquals(2020, journal.getYear());
            assertEquals(10, journal.getMonth());
            assertEquals(15, journal.getDay());
            assertEquals("my journal", journal.getTitle());
            List<Event> events = journal.displayEvents();
            assertEquals(2, events.size());
            checkEvent(15, 15, "work out", events.get(0));
            checkEvent(18, 30, "have dinner", events.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
