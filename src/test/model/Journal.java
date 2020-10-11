package model;

import java.util.ArrayList;

public class Journal {
    private ArrayList<Event> journal;
    private int year;
    private int month;
    private int day;
    private String title;
    private ArrayList<Event> selectedEvents = new ArrayList<>();

    // REQUIRES: 1 <= month <= 12, 1 <= day <= 31
    // EFFECTS: create an empty Journal with a date and a title
    public Journal(int year, int month, int day, String title) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        journal = new ArrayList<>();
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getTitle() {
        return title;
    }

    // MODIFIES: this
    // EFFECTS: adds the given event to the journal
    public void addEvent(Event e) {
        journal.add(e);
    }

    // EFFECTS: return true if the event exists in the journal,
    //          return false otherwise
    public boolean contain(Event e) {
        return journal.contains(e);
    }

    // EFFECTS: return events that are marked as the highlightOfTheDay,
    //          return null if no such events are found
    public ArrayList<Event> displaySelectedEvents() {
        for (Event e : journal) {
            if (e.isHighlightOfTheDay() == true) {
                selectedEvents.add(e);
            }
        }
        if (selectedEvents.isEmpty() == false) {
            return selectedEvents;
        } else {
            return null;
        }
    }
}