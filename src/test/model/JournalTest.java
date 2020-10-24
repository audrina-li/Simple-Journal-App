package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JournalTest {
    private Journal journal;

    @BeforeEach
    void runBefore() {
        journal = new Journal(2020,10,10,"an ordinary day");
    }

    @Test
    void testConstructor() {
        assertEquals(2020, journal.getYear());
        assertEquals(10,journal.getMonth());
        assertEquals(10,journal.getDay());
        assertEquals("an ordinary day",journal.getTitle());
    }

    @Test
    void testAddEvent() {
        Event e1 = new Event(15,5,"study");
        Event e2 = new Event(18,30,"have dinner");
        journal.addEvent(e1);
        journal.addEvent(e2);
        assertTrue(journal.contain(e1));
        assertTrue(journal.contain(e2));
    }

    @Test
    void testRemoveEvent() {
        Event e1 = new Event(15,5,"study");
        Event e2 = new Event(18,30,"have dinner");
        journal.removeEvent(e1);
        journal.removeEvent(e2);
        assertFalse(journal.contain(e1));
        assertFalse(journal.contain(e2));
    }

    @Test
    void testDisplayEvent() {
        Event e1 = new Event(15,5,"study");
        Event e2 = new Event(18,30,"have dinner");
        journal.addEvent(e1);
        journal.addEvent(e2);
        assertEquals(e1, journal.displayEvents().get(0));
        assertEquals(e2, journal.displayEvents().get(1));
    }

    @Test
    void testToJsonWithoutEvent() {
        assertEquals(2020,journal.toJson().get("year"));
        assertEquals(10,journal.toJson().get("month"));
        assertEquals(10,journal.toJson().get("day"));
        assertEquals("an ordinary day",journal.toJson().get("title"));
        assertEquals(0,journal.toJson().getJSONArray("events").length());
    }

    @Test
    void testToJsonWithEvents() {
        Event e1 = new Event(15,5,"study");
        Event e2 = new Event(18,30,"have dinner");
        journal.addEvent(e1);
        journal.addEvent(e2);
        assertEquals(15, journal.eventsToJson().getJSONObject(0).get("hour"));
        assertEquals(5,journal.eventsToJson().getJSONObject(0).get("minute"));
        assertEquals("study",journal.eventsToJson().getJSONObject(0).get("description"));
        assertEquals(18, journal.eventsToJson().getJSONObject(1).get("hour"));
        assertEquals(30,journal.eventsToJson().getJSONObject(1).get("minute"));
        assertEquals("have dinner",journal.eventsToJson().getJSONObject(1).get("description"));
    }
}