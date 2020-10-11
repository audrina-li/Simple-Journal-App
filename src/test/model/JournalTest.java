package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JournalTest {
    private Journal journal;
    private ArrayList<Event> meaningfulEvent;

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
    void testAddEventOne() {
        Event e = new Event(15,5,"study");
        journal.addEvent(e);
        assertTrue(journal.contain(e));
    }

    @Test
    void testAddEventMultiple() {
        Event e1 = new Event(15,5,"study");
        Event e2 = new Event(18,30,"have dinner");
        journal.addEvent(e1);
        journal.addEvent(e2);
        assertTrue(journal.contain(e1));
        assertTrue(journal.contain(e2));
    }

    @Test
    void testDisplayMeaningfulEventEmpty() {
        assertEquals(null, journal.displaySelectedEvents());
    }

    @Test
    void testDisplaySelectedEventNonEmpty() {
        Event e1 = new Event(15,5,"study");
        Event e2 = new Event(18,30,"have dinner");
        e1.highlightEvent();
        e2.highlightEvent();
        journal.addEvent(e1);
        journal.addEvent(e2);
        assertEquals(e1, journal.displaySelectedEvents().get(0));
        assertEquals(e2, journal.displaySelectedEvents().get(1));
    }


}