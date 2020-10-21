package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a journal with date and title
public class Journal implements Writable {
    private ArrayList<Event> journal;
    private ArrayList<Event> events = new ArrayList<>();
    private int year;
    private int month;
    private int day;
    private String title;

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

    // EFFECTS: return true if the event exists in the journal,
    //          return false otherwise
    public boolean contain(Event e) {
        return journal.contains(e);
    }

    // MODIFIES: this
    // EFFECTS: adds the given event to the journal
    public void addEvent(Event e) {
        journal.add(e);
        events.add(e);
    }

    // EFFECTS: return all events in the journal
    public ArrayList<Event> displayEvents() {
        return events;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("year", year);
        json.put("month", month);
        json.put("day", day);
        json.put("title", title);
        json.put("events", eventsToJson());
        return json;
    }

    // EFFECTS: returns events in this journal as a JSON array
    public JSONArray eventsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Event e : journal) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }
}