package model.ui_models;

import model.game_entities.Player;
import model.game_entities.Territory;

import javax.swing.*;
import java.util.Observable;
import java.util.Vector;

import static model.ui_models.GamePlayModel.getInstance;

public class FortificationModel extends Observable {
    private Vector<String> sourceTerritoriesList;
    private Vector<String> targetTerritoriesList;
    private GamePlayModel gamePlayModel;
    private Player currentPlayer;
    
    public FortificationModel() {
        gamePlayModel = getInstance();
        currentPlayer = gamePlayModel.getCurrPlayer();
        sourceTerritoriesList = new Vector<>();
        targetTerritoriesList = new Vector<>();
        
        /* collect player's territories */
        for (Territory t : gamePlayModel.getGameMap().getTerritoriesOfPlayer(currentPlayer).values()) {
            sourceTerritoriesList.add(t.getName());
        }
        broadcastGamePlayChanges();
    }
    
    /* Getters & setters */
    
    /**
     * Accessor for the source territories model
     *
     * @return the model
     */
    public ComboBoxModel getSourceTerritoriesList() {
        return new DropDownModel(sourceTerritoriesList);
    }
    
    /**
     * Sets target territory dropdown based on source territory dropdown
     * Only adjacent territories belonging to same player are shown
     *
     * @param selectedTerritory selected value in source territory dropdown
     */
    public void setTargetTerritoriesList(String selectedTerritory) {
        targetTerritoriesList.clear();
        Vector<String> neighbors = gamePlayModel.getGameMap().getATerritory(selectedTerritory).getNeighbors();
        for (String n : neighbors) {  // if neighbor is owned by current player, add it to the lost
            if (gamePlayModel.getGameMap().getATerritory(n).isOwnedBy(currentPlayer.getPlayerID())
                    && !n.equals(selectedTerritory)) {
                targetTerritoriesList.add(n);
            }
        }
        if (targetTerritoriesList.size() == 0) {
            targetTerritoriesList.add("No neighbors owned. Please select another territory");
        }
        broadcastGamePlayChanges();
    }
    
    /**
     * Accessor for the target territories model
     *
     * @return the model
     */
    public ComboBoxModel getTargetTerritoriesList() {
        return new DropDownModel(targetTerritoriesList);
    }
    
    /* Private methods */
    
    /**
     * Method to update the GamePlayModel and notify the Observer.
     */
    private void broadcastGamePlayChanges() {
        setChanged();
        notifyObservers();
    }
}
