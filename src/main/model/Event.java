package model;

public class Event {
    private int hour;
    private int minute;
    private String description;
    private boolean highlightOfTheDay = false;

    // REQUIRES: 0 <= hour <= 24, 0 <= minute <= 60
    // EFFECTS: an event with a time and a description
    public Event(int hour, int minute, String description) {
        this.hour = hour;
        this.minute = minute;
        this.description = description;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHighlightOfTheDay() {
        return highlightOfTheDay;
    }

    // MODIFIES: this
    // EFFECTS: make changes to an event with modified time and/or description
    public void makeChanges(int hour, int minute, String description) {
        this.hour = hour;
        this.minute = minute;
        this.description = description;
    }

    // MODIFIES: this
    // EFFECTS: place a star symbol before and after the description of the event
    public void highlightEvent() {
        description = "* " + description + " *";
        highlightOfTheDay = true;
    }
}
