/*
 * Risk Game Team 2
 * LoggingFrame.java
 * Version 1.0
 * Nov 6, 2017
 */
package game_play.view.screens;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * The window used to display the game progression
 *
 * @author Team 2
 * @version 2.0
 */
public class LoggingFrame extends JFrame {
    private static LoggingFrame instance = null;
    private static JTextArea logArea;
    
    // region Constructors
    
    /**
     * Private constructor for the Logging Window
     */
    private LoggingFrame() {
        JFrame frame = new JFrame();
        frame.setTitle("Game Logging");
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setMargin(new Insets(10, 10, 10, 10));
        logArea.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.add(new JScrollPane(logArea));
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500, 1000);
    }
    
    /**
     * Static instance method to determine if an object of LoggingWindow already exists.
     *
     * @return instance of the singleton LoggingWindow object
     */
    public static LoggingFrame getInstance() {
        if (instance == null) {
            instance = new LoggingFrame();
        }
        return instance;
    }
    // endregion
    
    // region Public Methods
    
    /**
     * Appends text to logging text area
     *
     * @param text the text to be appended on the logging area
     */
    public static void append(String text) {
        if (logArea.getText().length() > 10000) {
            dumpLog();
            logArea.setText("");
        }
        logArea.append("\n" + text);
        logArea.repaint();
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    private static void dumpLog() {
        Path logPath = Paths.get("Log.txt");
        Charset charset = Charset.forName("UTF-8");
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = Files.newBufferedWriter(logPath, charset, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            bufferedWriter.append(logArea.getText(), 0, logArea.getText().length());
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * Gets logArea
     *
     * @return Value of logArea.
     */
    public static JTextArea getLogArea() {
        return logArea;
    }
    
    /**
     * Sets new logArea.
     *
     * @param logArea New value of logArea.
     */
    public void setLogArea(JTextArea logArea) {
        this.logArea = logArea;
    }
    // endregion
}
