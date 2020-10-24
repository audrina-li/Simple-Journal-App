package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    private Event event;

    @BeforeEach
    void runBefore() {
        event = new Event(8,30,"have breakfast");
    }

    @Test
    void testConstructor() {
        assertEquals(8, event.getHour());
        assertEquals(30, event.getMinute());
        assertEquals("have breakfast", event.getDescription());
        assertFalse(event.isHighlightOfTheDay());
    }

    @Test
    void testHighlightEventTrue() {
        event.highlightEvent();
        assertEquals(8, event.getHour());
        assertEquals(30,event.getMinute());
        assertEquals("* have breakfast *", event.getDescription());
        assertTrue(event.isHighlightOfTheDay());
        event.highlightEvent();
        assertEquals(8, event.getHour());
        assertEquals(30,event.getMinute());
        assertEquals("* have breakfast *", event.getDescription());
    }

    @Test
    void testHighlightEventFalse() {
        event.highlightEvent();
        assertEquals(8, event.getHour());
        assertEquals(30,event.getMinute());
        assertEquals("* have breakfast *", event.getDescription());
        assertTrue(event.isHighlightOfTheDay());
    }

    @Test
    void testToJson() {
        assertEquals(8, event.toJson().get("hour"));
        assertEquals(30, event.toJson().get("minute"));
        assertEquals("have breakfast", event.toJson().get("description"));
    }
}