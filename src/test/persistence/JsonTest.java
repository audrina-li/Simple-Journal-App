package persistence;

import model.Event;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkEvent(int hour, int minute, String description, Event event) {
        assertEquals(hour, event.getHour());
        assertEquals(minute, event.getMinute());
        assertEquals(description, event.getDescription());
    }
}