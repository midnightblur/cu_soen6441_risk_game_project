package model;

/**
 * Each Player in a new game has a unique ID number (starting from 1) and the isBot status
 * which determines whether or not that player is a bot or a human controlled Player.
 */
public class Player {
    private static int nextID = 0;

    // Private data member of model.Player class
    private int playerID;
    private String playerName;

    public Player() {
        this.playerID = ++Player.nextID;
    }

    // Getters & Setters
    public int getID() {
        return this.playerID;
    }
    
}