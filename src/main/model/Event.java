package model;

import org.json.JSONObject;
import persistence.Writable;

//Represent an event with time and description
public class Event implements Writable {
    private int hour;
    private int minute;
    private String description;
    private boolean highlightOfTheDay;

    // EFFECTS: constructs an event with a time and description
    public Event(int hour, int minute, String description) {
        this.hour = hour;
        this.minute = minute;
        this.description = description;
        highlightOfTheDay = false;
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

    // REQUIRES: the event is not currently marked as the highlightOfTheDay
    // MODIFIES: this
    // EFFECTS: place a pair of star symbol around the description
    public void highlightEvent() {
        if (highlightOfTheDay == false) {
            description = "* " + description + " *";
            highlightOfTheDay = true;
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("hour", hour);
        json.put("minute", minute);
        json.put("description", description);
        return json;
    }
}
