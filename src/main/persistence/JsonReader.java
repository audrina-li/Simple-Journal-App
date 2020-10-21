package persistence;

import model.Event;
import model.Journal;
import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads journal from JSON data stored in file
// Some of the code is cited from the JsonSerializationDemo
// URL: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads journal from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Journal read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseJournal(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses journal from JSON object and returns it
    private Journal parseJournal(JSONObject jsonObject) {
        int year = jsonObject.getInt("year");
        int month = jsonObject.getInt("month");
        int day = jsonObject.getInt("day");
        String title = jsonObject.getString("title");
        Journal journal = new Journal(year,month,day,title);
        addEvents(journal, jsonObject);
        return journal;
    }

    // MODIFIES: journal
    // EFFECTS: parses events from JSON object and adds them to journal
    private void addEvents(Journal journal, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("events");
        for (Object json : jsonArray) {
            JSONObject nextEvent = (JSONObject) json;
            addEvent(journal, nextEvent);
        }
    }

    // MODIFIES: journal
    // EFFECTS: parses event from JSON object and adds it to journal
    private void addEvent(Journal journal, JSONObject jsonObject) {
        int hour = jsonObject.getInt("hour");
        int minute = jsonObject.getInt("minute");
        String description = jsonObject.getString("description");
        Event event = new Event(hour, minute, description);
        journal.addEvent(event);
    }
}