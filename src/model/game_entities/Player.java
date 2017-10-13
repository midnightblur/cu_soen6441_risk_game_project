package model.game_entities;

import java.awt.*;
import java.util.Observable;
import java.util.Random;
import java.util.Vector;

/**
 * Each Player in a new game has a unique ID number (starting from 1) and the isBot status
 * which determines whether or not that player is a bot or a human controlled Player.
 */
public class Player extends Observable {
    private static int nextID = 0;
    Random rand = new Random();
    // Will produce only bright / light colours:
    private Color color;
    
    // Private data member of model.game_entities.Player class
    private int playerID;
    private String playerName;
    private int unallocatedArmies;
    private Vector<Card> playersHand = new Vector<>();
    
    public Player() {
        this.playerID = ++Player.nextID;
        setColor();
    }
    
    /* Getters & Setters */
    public int getPlayerID() {
        return this.playerID;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public void setUnallocatedArmies(int unallocatedArmies) {
        this.unallocatedArmies = unallocatedArmies;
        broadcastPlayerChanges();
    }
    
    public int getUnallocatedArmies() {
        return this.unallocatedArmies;
    }
    
    public Vector<Card> getPlayersHand() {
        return this.playersHand;
    }
    
    // public methods
    
    /**
     * Reduces the number of unallocated armies for this player by the specified number.
     *
     * @param num The int value of the number of unallocated armies to reduce
     */
    public void reduceUnallocatedArmies(int num) {
        this.unallocatedArmies -= num;
        broadcastPlayerChanges();
    }
    
    /**
     * Increases the number of unallocated armies for this player by the specified number.
     *
     * @param num The int value of the number o unallocated armies to add
     */
    public void addUnallocatedArmies(int num) {
        this.unallocatedArmies += num;
        broadcastPlayerChanges();
    }
    
    /**
     * Adds a card to the player's hand.
     *
     * @param card An object of Card class to be added to the players hand
     */
    public void addCardToPlayersHand(Card card) {
        this.playersHand.add(card);
    }
    
    /**
     * Player color is randomly generated when a new player object is created
     */
    private void setColor() {
        float r, g, b;
        r = (float) (rand.nextFloat() / 2f + 0.5);
        g = (float) (rand.nextFloat() / 2f + 0.5);
        b = (float) (rand.nextFloat() / 2f + 0.5);
        this.color = new Color(r, g, b);
    }
    
    /**
     * Override equals method to check whether or not two Player objects are the same.
     *
     * @param other The other object to compare with the Player object.
     *
     * @return Boolean value that tells whether or not the two Player objects have the same attribute values
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        Player tempPlayer = (Player) other;
        if (this.playerID == tempPlayer.playerID
                && ((this.playerName == null && tempPlayer.playerName == null)
                || this.playerName.equals(tempPlayer.playerName))
                && this.unallocatedArmies == tempPlayer.unallocatedArmies) {
            return true;
        }
        return false;
    }
    
    /**
     * Method to update the GamePlayModel and notify the Observer.
     */
    private void broadcastPlayerChanges() {
        setChanged();
        notifyObservers();
    }
    
}
