package persistence;

import model.Event;
import model.Journal;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Journal journal = new Journal(2020,10,20,"my journal");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testWriterEmptyJournal() {
        try {
            Journal journal = new Journal(2020,10,20,"my journal");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyJournal.json");
            writer.open();
            writer.write(journal);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyJournal.json");
            journal = reader.read();
            assertEquals(2020, journal.getYear());
            assertEquals(10, journal.getMonth());
            assertEquals(20, journal.getDay());
            assertEquals("my journal", journal.getTitle());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralJournal() {
        try {
            Journal journal = new Journal(2020,10,20,"my journal");
            journal.addEvent(new Event(8,00,"have breakfast"));
            journal.addEvent(new Event(11,15,"shopping"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralJournal.json");
            writer.open();
            writer.write(journal);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralJournal.json");
            journal = reader.read();
            assertEquals(2020, journal.getYear());
            assertEquals(10, journal.getMonth());
            assertEquals(20, journal.getDay());
            assertEquals("my journal", journal.getTitle());
            List<Event> events = journal.displayEvents();
            assertEquals(2, events.size());
            checkEvent(8,00, "have breakfast",events.get(0));
            checkEvent(11,15,"shopping",events.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
