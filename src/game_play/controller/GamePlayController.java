/* 
 * Risk Game Team 2
 * GamePlayController.java
 * Version 1.0
 * Oct 18, 2017
 */
package game_play.controller;

import game_play.model.DropDownModel;
import game_play.model.GamePlayModel;
import game_play.view.screens.GamePlayFrame;
import game_play.view.ui_components.ReinforcementPanel;
import shared_resources.game_entities.GameMap;
import shared_resources.game_entities.Territory;
import shared_resources.helper.UIHelper;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static shared_resources.utilities.Config.GAME_STATES.*;

/**
 * This class is used as a controller to read and set map filepath
 * to the game_entities, and dispatchToController
 * the view to displayJFrame the map.
 *
 * @author Team 2
 * @version 1.0
 */
public class GamePlayController {
    
    // region Attributes declaration
    /** The caller controller. */
    private MainMenuController callerController;
    
    /** The game play frame. */
    private GamePlayFrame gamePlayFrame;
    
    /** The game play game_entities. */
    private GamePlayModel gamePlayModel;
    // endregion
    
    // region Constructors
    
    /**
     * Constructor for the mainGameController
     * responsible for launching the game phases.
     *
     * @param callerController the caller controller
     * @param gameMap          The already loaded map for the game
     */
    public GamePlayController(MainMenuController callerController, GameMap gameMap) {
        this.callerController = callerController;
        gamePlayModel = GamePlayModel.getInstance();
        gamePlayFrame = new GamePlayFrame();
        
        registerObserversToObservable();
        
        registerToBeListener();
        
        gamePlayModel.setGameMap(gameMap);
        gamePlayModel.setGameState(SETUP);
        
        gamePlayFrame.getGameMapTable().setModel(gamePlayModel.getMapTableModel().getModel());
    }
    // endregion
    
    // region Private methods
    
    /**
     * Register observers to observable.
     */
    private void registerObserversToObservable() {
        gamePlayModel.addObserver(gamePlayFrame);
        gamePlayModel.addObserver(gamePlayFrame.getGameMapTable());
        gamePlayModel.addObserver(gamePlayFrame.getGameSetupPanel());
        gamePlayModel.addObserver(gamePlayFrame.getStartupPanel());
        gamePlayModel.addObserver(gamePlayFrame.getReinforcementPanel());
        gamePlayModel.addObserver(gamePlayFrame.getReinforcementPanel().getTradeCardsPanel());
        gamePlayModel.addObserver(gamePlayFrame.getFortificationPanel());
    }
    
    /**
     * Register to be listener.
     */
    private void registerToBeListener() {
        /* Play button to start the game */
        gamePlayFrame.getGameSetupPanel().addPlayButtonListener(e -> startTheGame());
        
        /* For Startup Panel */
        gamePlayFrame.getStartupPanel().addPlaceArmyButtonListener(e -> placeArmy());
        
        /* For Reinforcement Panel */
        gamePlayFrame.getReinforcementPanel().addTradeCardsButtonListener(e -> goToTradeCardsPanel());
        gamePlayFrame.getReinforcementPanel().addPlaceArmiesButtonListener(e -> distributeArmies());
        gamePlayFrame.getReinforcementPanel().addGoToFortificationButtonListener(e -> goToFortificationPhase());
        gamePlayFrame.getReinforcementPanel().getTradeCardsPanel().addTradeCardsButtonListener(e -> tradeSelectedCards());
        gamePlayFrame.getReinforcementPanel().getTradeCardsPanel().addBackToReinforcementListener(e -> backToReinforcementPanel());
        
        /* For Fortification Panel */
        gamePlayFrame.getFortificationPanel().addMoveArmiesButtonListener(e -> moveArmies());
        gamePlayFrame.getFortificationPanel().addSourceTerritoryDropdownListener(e -> updateTargetTerritoriesDropdown(
                String.valueOf(gamePlayFrame.getFortificationPanel().getSourceTerritoryDropdown().getSelectedItem())
        ));
        gamePlayFrame.getFortificationPanel().addNextPlayerButtonListener(e -> changeToNextPlayer());
    }
    
    // region For Setup Phase
    
    /**
     * Start the game.
     */
    private void startTheGame() {
        /* initialize the game */
        try {
            int enteredPlayers = Integer.parseInt(gamePlayFrame.getGameSetupPanel().getPlayerCount().getText());
            if ((enteredPlayers > 1) && (enteredPlayers <= gamePlayModel.getGameMap().getMaxPlayers())) {
                gamePlayModel.initializeNewGame(enteredPlayers);
            } else if (enteredPlayers == 1) {
                gamePlayModel.initializeNewGame(enteredPlayers);
                UIHelper.displayMessage(gamePlayFrame, "Player 1 Wins!");
                UIHelper.closeFrame(gamePlayFrame);
                UIHelper.invokeFrame(callerController.getMainMenuFrame());
            } else {
                UIHelper.displayMessage(gamePlayFrame, "You must enter an amount of players between 1 and " + gamePlayModel.getGameMap().getMaxPlayers());
            }
        } catch (ClassCastException | NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Invalid entry. Please re-enter a number.",
                    "Entry Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    // endregion
    
    // region For Startup Phase
    
    /**
     * Place an army to the selected territory.
     */
    private void placeArmy() {
        String selectedTerritoryName = String.valueOf(gamePlayFrame.getStartupPanel().getTerritoryDropdown().getSelectedItem());
        gamePlayModel.placeArmyStartup(selectedTerritoryName);
    }
    // endregion
    
    // region For Reinforcement Phase
    
    /**
     * Shows the controller responsible for trading cards.
     */
    private void goToTradeCardsPanel() {
        CardLayout cardLayout = (CardLayout) gamePlayFrame.getReinforcementPanel().getCardsPanel().getLayout();
        cardLayout.show(gamePlayFrame.getReinforcementPanel().getCardsPanel(), ReinforcementPanel.getTradeCardsPanelName());
    }
    
    /**
     * Looping through view table, get the quantity of armies for each territory
     * then place them using the placeArmiesReinforcement in the game_entities.
     */
    private void distributeArmies() {
        TableModel armiesData = gamePlayFrame.getReinforcementPanel().getPlayerTerritoryTable().getModel();
        String territoryName;
        int armies;
        int runningSum = 0;
        Map<Territory, Integer> armiesToPlace = new HashMap<>();
        for (int r = 0; r < armiesData.getRowCount(); r++) {
            armies = Integer.parseInt(armiesData.getValueAt(r, 1).toString());
            if (armies > 0) {   // only add entries that have more than 0 armies to be placed
                runningSum += armies;
                territoryName = armiesData.getValueAt(r, 0).toString();
                armiesToPlace.put(gamePlayModel.getGameMap().getATerritory(territoryName), armies);
            }
        }
        if (runningSum > 0 && runningSum <= gamePlayModel.getCurrentPlayer().getUnallocatedArmies()) {
            gamePlayModel.placeArmiesReinforcement(armiesToPlace);
            UIHelper.displayMessage(gamePlayFrame, "The armies were placed successfully");
            // reset the armies to zero
            for (int r = 0; r < armiesData.getRowCount(); r++) {
                armiesData.setValueAt(0, r, 1);
            }
        } else {
            UIHelper.displayMessage(gamePlayFrame, "The total armies to allocate must be lesser or equal to the indicated total armies to place");
        }
    }
    
    /**
     * Go to fortification phase.
     */
    private void goToFortificationPhase() {
        if (gamePlayModel.getCurrentPlayer().getUnallocatedArmies() != 0 || gamePlayModel.getCurrentPlayer().getPlayersHand().size() >= 5) {
            UIHelper.displayMessage(gamePlayFrame, "You have to allocate all of your armies or trade in your cards");
        } else {
            gamePlayModel.setGameState(FORTIFICATION);
        }
    }
    
    /**
     * Collect the selected cards from UI and trade them by calling the tradeInCards() from the game_entities.
     */
    private void tradeSelectedCards() {
        int previousUnallocatedArmies = gamePlayModel.getCurrentPlayer().getUnallocatedArmies();
        Vector<String> selectedCards = new Vector<>();
        for (Component component : gamePlayFrame.getReinforcementPanel().getTradeCardsPanel().getCardList().getComponents()) {
            JCheckBox checkBox = (JCheckBox) component;
            if (checkBox.isSelected()) {
                selectedCards.add(checkBox.getText());
            }
        }
        String message = gamePlayModel.tradeInCards(selectedCards);
        int gainedArmies = gamePlayModel.getCurrentPlayer().getUnallocatedArmies() - previousUnallocatedArmies;
        gamePlayFrame.getReinforcementPanel().getTradeCardsPanel().setGainedArmiesLabel(gainedArmies);
        // confirmation message
        UIHelper.displayMessage(gamePlayFrame, message);
    }
    
    /**
     * Back to reinforcement panel.
     */
    private void backToReinforcementPanel() {
        CardLayout cardLayout = (CardLayout) gamePlayFrame.getReinforcementPanel().getCardsPanel().getLayout();
        cardLayout.show(gamePlayFrame.getReinforcementPanel().getCardsPanel(), ReinforcementPanel.getControlWrapperPanelName());
    }
    // endregion
    
    // region For Fortification Phase
    
    /**
     * Move armies from selected source territory to selected target territory
     * If move is successful the action is disabled.
     */
    private void moveArmies() {
        String sourceTerritory = String.valueOf(gamePlayFrame.getFortificationPanel().getSourceTerritoryDropdown().getSelectedItem());
        String targetTerritory = String.valueOf(gamePlayFrame.getFortificationPanel().getTargetTerritoryDropdown().getSelectedItem());
        String inputtedArmies = gamePlayFrame.getFortificationPanel().getArmiesToMoveField().getText();
        int quantity;
        try {
            quantity = Integer.parseInt(inputtedArmies);
            
            if ((quantity > 0) && targetTerritory.compareTo("No neighbors owned. Please select another territory") != 0) {
                String message = gamePlayModel.moveArmiesFortification(sourceTerritory, targetTerritory, quantity);
                UIHelper.displayMessage(gamePlayFrame, message);
            } else {
                UIHelper.displayMessage(gamePlayFrame, "Please validate your selection.");
            }
        } catch (ClassCastException | NumberFormatException nfe) {
            UIHelper.displayMessage(gamePlayFrame, "Invalid entry. Please re-enter a number.");
        }
    }
    
    /**
     * Update target territories dropdown.
     *
     * @param selectedTerritory the selected territory
     */
    private void updateTargetTerritoriesDropdown(String selectedTerritory) {
        Vector<String> targetTerritoriesList = new Vector<>();
        Vector<String> neighbors = gamePlayModel.getGameMap().getATerritory(selectedTerritory).getNeighbors();
        for (String neighborName : neighbors) {  // if neighborName is owned by current player, add it to the lost
            if (gamePlayModel.getGameMap().getATerritory(neighborName).isOwnedBy(gamePlayModel.getCurrentPlayer().getPlayerID())
                    && neighborName.compareTo(selectedTerritory) != 0) {
                targetTerritoriesList.add(neighborName);
            }
        }
        if (targetTerritoriesList.size() == 0) {
            targetTerritoriesList.add("No neighbors owned. Please select another territory");
        }
        DropDownModel targetTerritoriesModel = new DropDownModel(targetTerritoriesList);
        gamePlayFrame.getFortificationPanel().getTargetTerritoryDropdown().setModel(targetTerritoriesModel);
    }
    
    /**
     * Change to next player.
     */
    private void changeToNextPlayer() {
        gamePlayModel.setGameState(REINFORCEMENT);
        gamePlayModel.setCurrentPlayer(gamePlayModel.getNextPlayer());
    }
    // endregion
    
    // endregion
}
