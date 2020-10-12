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
    void testMakeChangesOne() {
        event.makeChanges(9, 0,"have breakfast");
        assertEquals(9, event.getHour());
        assertEquals(0,event.getMinute());
        assertEquals("have breakfast", event.getDescription());

        event.makeChanges(8, 30,"work out");
        assertEquals(8, event.getHour());
        assertEquals(30,event.getMinute());
        assertEquals("work out", event.getDescription());
    }

    @Test
    void testMakeChangesBoth() {
        event.makeChanges(10,15 ,"shopping");
        assertEquals(10, event.getHour());
        assertEquals(15,event.getMinute());
        assertEquals("shopping", event.getDescription());
    }

    @Test
    void testHighlightEvent() {
        event.highlightEvent();
        assertEquals(8, event.getHour());
        assertEquals(30,event.getMinute());
        assertEquals("* have breakfast *", event.getDescription());
        assertTrue(event.isHighlightOfTheDay());
    }
}