package ui;

import model.Event;
import model.Journal;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Represents the journal application
// Reference: https://github.students.cs.ubc.ca/CPSC210/SimpleDrawingPlayer-Starter.git
//            https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
//            https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
//            https://stackoverflow.com/questions/18777893/jframe-background-image
//            http://suavesnippets.blogspot.com/2011/06/add-sound-on-jbutton-click-in-java.html
public class JournalAppGUI extends JFrame {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 500;

    private Map<String, Journal> journals = new HashMap<>();
    private String currentJournal;

    private String jsonStore;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    public JournalAppGUI() {
        super("Journal App");
        initializeGraphics();
    }

    // MODIFIES: this
    // EFFECTS:  display application's main window frame
    private void initializeGraphics() {
        setLayout(new BorderLayout());
        setLayout(new FlowLayout());
        setBackgroundImage();
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addMenu();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: set background image
    private void setBackgroundImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/main/ui/image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(scaledImage);
        setContentPane(new JLabel(imageIcon));
    }

    // MODIFIES: this
    // EFFECTS: adds menu bar
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        addMenuItem(fileMenu, new NewFileAction(),
                KeyStroke.getKeyStroke("control N"));
        addMenuItem(fileMenu, new OpenFileAction(),
                KeyStroke.getKeyStroke("control O"));
        addMenuItem(fileMenu, new SaveFileAction(),
                KeyStroke.getKeyStroke("control S"));
        addMenuItem(fileMenu, new LoadFileAction(),
                KeyStroke.getKeyStroke("control L"));
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }

    // MODIFIES: this
    // EFFECTS: adds an item with given handler to the given menu
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    // Represents the action to be taken when the user wants to create a journal
    private class NewFileAction extends AbstractAction {

        NewFileAction() {
            super("New");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                currentJournal = JOptionPane.showInputDialog("Enter Title:");
                String date = JOptionPane.showInputDialog("Enter Date (YYYY-MM-DD):");
                int year = Integer.valueOf(date.substring(0, 4));
                int month = Integer.valueOf(date.substring(5, 7));
                int day = Integer.valueOf(date.substring(8, 10));

                Journal journal = new Journal(year, month, day, currentJournal);
                journals.put(currentJournal, journal);
                playSound("src/main/ui/sound.wav");
                JOptionPane.showMessageDialog(null,
                        "Journal Created");

                int response = JOptionPane.showConfirmDialog(null,
                        "Would you like to add an event to the journal?");
                if (response == 0) {
                    addEvents();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Not a valid input.\nPlease try again.",
                        "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: add events to the currentJournal
    private void addEvents() {
        Boolean add = true;

        try {
            while (add) {
                addEvent();

                int response = JOptionPane.showConfirmDialog(null,
                        "Would you like to add another event to the journal?");

                if (response == 1) {
                    add = false;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Not a valid input.\nPlease try again.",
                    "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: add an event to the currentJournal
    private void addEvent() {
        Journal myJournal = journals.get(currentJournal);

        try {
            String time = JOptionPane.showInputDialog("Enter Time (HH:MM):");
            int hour = Integer.parseInt(time.substring(0, 2));
            int minute = Integer.parseInt(time.substring(3, 5));
            String description = JOptionPane.showInputDialog("Enter Description:");

            Event event = new Event(hour, minute, description);
            myJournal.addEvent(event);
            JOptionPane.showMessageDialog(null,
                    "Event Added");

        } catch (NumberFormatException e) {
            System.out.println("\nNot a valid input.\nPlease try again.");
        }
    }

    // Represents the action to be taken when the user wants to view a journal
    private class OpenFileAction extends AbstractAction {

        OpenFileAction() {
            super("Open");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String title = JOptionPane.showInputDialog("Select A Journal By Title:");

            if (journals.containsKey(title)) {
                Journal journal = journals.get(title);
                ArrayList<Event> events = journal.displayEvents();

                String message = "";

                if (events.isEmpty()) {
                    JOptionPane.showMessageDialog(null,"This is an empty journal.");
                } else {
                    for (Event e : events) {
                        String min = Integer.toString(e.getMinute());

                        if (min.length() == 1) {
                            message = message + "\n" + (e.getHour() + ":" + "0" + min + " " + e.getDescription());
                        } else {
                            message = message + "\n" + (e.getHour() + ":" + e.getMinute() + " " + e.getDescription());
                        }
                    }
                    JOptionPane.showMessageDialog(null, title + message);
                }
            } else {
                JOptionPane.showMessageDialog(null,"Could not find the journal.");
            }
        }
    }

    // Represents the action to be taken when the user wants to save a journal
    private class SaveFileAction extends AbstractAction {

        SaveFileAction() {
            super("Save");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String title = JOptionPane.showInputDialog("Select A Journal By Title:");

            if (journals.containsKey(title)) {
                Journal journal = journals.get(title);
                jsonStore = "./data/" + title + ".json";
                jsonWriter = new JsonWriter(jsonStore);

                try {
                    jsonWriter.open();
                    jsonWriter.write(journal);
                    jsonWriter.close();
                    JOptionPane.showMessageDialog(null, "Saved the journal to " + jsonStore);
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(null,
                            "Unable to write to file: " + jsonStore,
                            "System Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Could not find the journal.",
                        "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Represents the action to be taken when the user wants to load a journal
    private class LoadFileAction extends AbstractAction {

        LoadFileAction() {
            super("Load");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String title = JOptionPane.showInputDialog("Select A Journal By Title:");
            jsonStore = "./data/" + title + ".json";
            jsonReader = new JsonReader(jsonStore);

            try {
                Journal journal = jsonReader.read();
                journals.put(title, journal);
                JOptionPane.showMessageDialog(null, "Loaded the journal from " + jsonStore);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "\nUnable to read from file: " + jsonStore,
                        "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: play sound
    public void playSound(String soundName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    // EFFECTS: starts the application
    public static void main(String[] args) {
        new JournalAppGUI();
    }
}