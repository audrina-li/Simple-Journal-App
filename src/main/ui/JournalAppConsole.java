package ui;

import model.Event;
import model.Journal;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Represent the console-based journal application
// References: https://github.students.cs.ubc.ca/CPSC210/TellerApp.git
//             https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class JournalAppConsole {
    private Scanner input = new Scanner(System.in).useDelimiter("\\n");
    private Map<Integer, Journal> journals = new HashMap<>();
    private String jsonStore;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private int journalNum = new File("data").list().length - 4;
    private int currentJournal;

    // EFFECTS: runs the journal application
    public JournalAppConsole() {
        runJournal();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runJournal() {
        boolean keepGoing = true;
        String command = null;

        while (keepGoing) {
            displayMenu();
            command = input.next();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("1")) {
            createJournal();
        } else if (command.equals("2")) {
            deleteJournal();
        } else if (command.equals("3")) {
            viewJournal();
        } else if (command.equals("4")) {
            saveJournal();
        } else if (command.equals("5")) {
            loadJournal();
        } else {
            System.out.println("Not a valid selection.");
        }
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\t1 -> create a journal");
        System.out.println("\t2 -> delete a journal");
        System.out.println("\t3 -> view a journal");
        System.out.println("\t4 -> save journal to file");
        System.out.println("\t5 -> load journal from file");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: create a new journal
    private void createJournal() {
        try {
            System.out.println("\nEnter date (YYYY-MM-DD):");
            String date = input.next();
            int year = Integer.valueOf(date.substring(0, 4));
            int month = Integer.valueOf(date.substring(5, 7));
            int day = Integer.valueOf(date.substring(8, 10));

            System.out.println("\nEnter title:");
            String title = input.next();

            journals.put(journalNum, new Journal(year, month, day, title));
            System.out.println("\nSuccessfully created a new journal."
                    + "\nReference number:" + Integer.toString(journalNum));
            currentJournal = journalNum;
            journalNum = journalNum + 1;

            System.out.println("\nWould you like to add an event to the journal? (Y for yes, N for no)");
            String response = input.next();

            if (response.equals("Y")) {
                addEvents();
            }
        } catch (Exception e) {
            System.out.println("\nNot a valid input.\nPlease try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: add events to the currentJournal
    private void addEvents() {
        Boolean add = true;

        try {
            while (add) {
                addEvent();

                System.out.println("\nWould you like to add another event to the journal? (Y for yes, N for no)");

                if (input.next().equals("N")) {
                    add = false;
                }
            }
        } catch (Exception e) {
            System.out.println("\nNot a valid input.\nPlease try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: add an event to the currentJournal
    private void addEvent() {
        Journal myJournal = journals.get(currentJournal);

        try {
            System.out.println("\nEnter time of event (HH:MM):");
            String time = input.next();
            int hour = Integer.parseInt(time.substring(0, 2));
            int minute = Integer.parseInt(time.substring(3, 5));

            System.out.println("\nEnter description of event:");
            String description = input.next();

            Event event = new Event(hour, minute, description);

            System.out.println("\nMark it as the highlight of the day?\n (Y for yes, N for no)");
            if (input.next().equals("Y")) {
                event.highlightEvent();
            }

            myJournal.addEvent(event);
            System.out.println("\nSuccessfully added a new event.");
        } catch (NumberFormatException e) {
            System.out.println("\nNot a valid input.\nPlease try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: delete an existing journal
    private void deleteJournal() {
        System.out.println("\nSelect a journal by reference number:");
        int key = input.nextInt();

        if (journals.containsKey(key)) {
            journals.remove(key);
            System.out.println("Journal deleted.");
        } else {
            System.out.println("Could not find the journal.");
        }
    }

    // EFFECTS: view events in a journal
    private void viewJournal() {
        System.out.println("\nSelect a journal by entering its reference number:");
        int key = input.nextInt();

        if (journals.containsKey(key)) {
            Journal journal = journals.get(key);
            ArrayList<Event> events = journal.displayEvents();

            if (events.isEmpty()) {
                System.out.println("\nThis is an empty journal.");
            } else {
                for (Event e : events) {
                    String min = Integer.toString(e.getMinute());

                    if (min.length() == 1) {
                        System.out.println(e.getHour() + ":" + "0" + min + " " + e.getDescription());
                    } else {
                        System.out.println(e.getHour() + ":" + e.getMinute() + " " + e.getDescription());
                    }
                }
            }
        } else {
            System.out.println("Could not find the journal.");
        }
    }

    // EFFECTS: saves the journal to file
    private void saveJournal() {
        System.out.println("\nSelect a journal by entering its reference number:");
        int num = input.nextInt();

        if (journals.containsKey(num)) {
            Journal journal = journals.get(num);
            jsonStore = "./data/journal" + Integer.toString(num) + ".json";
            jsonWriter = new JsonWriter(jsonStore);

            try {
                jsonWriter.open();
                jsonWriter.write(journal);
                jsonWriter.close();
                System.out.println("\nSaved the journal to " + jsonStore);
            } catch (FileNotFoundException e) {
                System.out.println("\nUnable to write to file: " + jsonStore);
            }
        } else {
            System.out.println("Could not find the journal.");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads journal from file
    private void loadJournal() {
        System.out.println("\nSelect a journal by entering its reference number:");
        int num = input.nextInt();
        jsonStore = "./data/journal" + Integer.toString(num) + ".json";
        jsonReader = new JsonReader(jsonStore);

        try {
            Journal journal = jsonReader.read();
            journals.put(num, journal);
            System.out.println("\nLoaded the journal from " + jsonStore);
        } catch (IOException e) {
            System.out.println("\nUnable to read from file: " + jsonStore);
        }
    }
}
