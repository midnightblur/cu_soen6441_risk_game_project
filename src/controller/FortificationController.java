package controller;

import model.RiskGame;
import model.game_entities.Player;
import model.ui_models.FortificationModel;
import view.screens.GamePlayFrame;
import view.ui_components.FortificationPanel;

import static model.RiskGame.getInstance;
import static utilities.Config.GAME_STATES.FORTIFICATION_PHASE;
import static view.helpers.UIHelper.setDivider;

/**
 * The FortificationController class
 */
public class FortificationController {
    private FortificationPanel fortificationPanel;
    private GamePlayFrame gamePlayFrame;
    private RiskGame riskGame;
    private Player currentPlayer;
    private FortificationModel fortificationModel;
    
    
    /* Constructors */
    
    /**
     * The constructor
     *
     * @param gamePlayFrame the main frame
     */
    public FortificationController(GamePlayFrame gamePlayFrame) {
        this.gamePlayFrame = gamePlayFrame;
        fortificationPanel = new FortificationPanel();
        gamePlayFrame.getContentPane().setRightComponent(fortificationPanel);
        setDivider(gamePlayFrame.getContentPane());
        
        riskGame = getInstance();
        currentPlayer = riskGame.getCurrPlayer();
        riskGame.setGameState(FORTIFICATION_PHASE);
        
        fortificationModel = new FortificationModel();
        

        /* Register Observer to Observable */
        fortificationModel.addObserver(fortificationPanel);
        currentPlayer.addObserver(fortificationPanel);
        riskGame.addObserver(fortificationPanel);
        
        /* Register to be ActionListeners */
        fortificationPanel.addDoneButtonListener(e -> nextPlayer());
        fortificationPanel.addMoveArmiesButtonListener(e -> moveArmies());
        fortificationPanel.addSourceTerritoryDropdownListener(e -> fortificationModel.setTargetTerritoriesList(
                fortificationPanel.getSourceTerritoryDropdown().getSelectedItem().toString()));
    
        /* set control panel */
        populateFortificationPanel();
    }
    
    /* Private methods */
    
    
    /**
     * Move armies from selected source territory to selected target territory
     * If move is successful the action is disabled
     */
    private void moveArmies() {
        String source = fortificationPanel.getSourceTerritoryDropdown().getSelectedItem().toString();
        String target = fortificationPanel.getTargetTerritoryDropdown().getSelectedItem().toString();
        String quantity = fortificationPanel.getArmiesToMoveField().getText();
        if (!quantity.equals("") && (Integer.parseInt(quantity) > 0) && !target.equals("No neighbours owned. Please select another territory")) {
            fortificationPanel.getMoveArmiesButton().setEnabled(true);
            String message = riskGame.fortificationPhase(source, target, quantity);
            riskGame.getMapTableModel().updateMapTableModel(riskGame.getGameMap());
            // disable the button once armies are moved
            if (message.toLowerCase().contains("success")) {
                fortificationPanel.getMoveArmiesButton().setEnabled(false);
            }
            gamePlayFrame.displayMessage(message);
        } else {
            gamePlayFrame.displayMessage("Please validate your selection.");
        }
    }
    
    /**
     * Populate the fortification control panel with updated model data
     */
    private void populateFortificationPanel() {
        /* set the phase label */
        fortificationPanel.setGameState(riskGame.getGameState());
        
        /* set the player ID label */
        fortificationPanel.setPlayerID(currentPlayer.getPlayerID());
        
        /* set the source dropdown */
        fortificationPanel.getSourceTerritoryDropdown().setModel(fortificationModel.getSourceTerritoriesList());
        
        /* set the target dropdown */
        fortificationPanel.getTargetTerritoryDropdown().setModel(fortificationModel.getTargetTerritoriesList());
        fortificationModel.setTargetTerritoriesList(fortificationPanel.getSourceTerritoryDropdown().getSelectedItem().toString());
    }
    
    /**
     * Advance the game to next player
     */
    public void nextPlayer() {
        riskGame.setCurrPlayerToNextPlayer();
        new ReinforcementController(this.gamePlayFrame);
    }
    
}
