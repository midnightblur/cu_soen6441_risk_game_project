package controller;

import model.RiskGame;
import model.game_entities.Player;
import model.ui_models.MapTableModel;
import model.ui_models.PlayerTerritoriesModel;
import utilities.Config;
import view.screens.GamePlayFrame;

import java.awt.event.WindowEvent;

/**
 * Controller to read and set map filepath to the model, and dispatchToController
 * the view to displayJFrame the map.
 */
public class GamePlayController {
    private GamePlayFrame gamePlayFrame;
    private RiskGame gamePlayModel;
    private MainGameController callerController;
    private MapTableModel tableModel;
    private PlayerTerritoriesModel playerTerritoriesModel;
    Player currentPlayer;
    
    /* Constructors */
    public GamePlayController(MainGameController mainGameController) {
        callerController = mainGameController;
        gamePlayFrame = new GamePlayFrame();
        gamePlayModel = RiskGame.getInstance();
        tableModel = gamePlayModel.getMapTableModel();
        
        // TODO: the following method calls should be moved to be part of StartupListener class which implements the ActionListener class
        /*
        initStartUp should only take 1 param like 'new StartupListener()'.
        Config.DEFAULT_MAP should be retrieved from the view's filepath value,
        default number of players should be retrieved from the view's number of players value
         */
        gamePlayModel.startupPhase(Config.DEFAULT_MAP, Config.DEFAULT_NUM_OF_PLAYERS);
        
        
        // TODO: get the correct player
        currentPlayer = gamePlayModel.getPlayers().elementAt(1);

        /* update the table model from a loaded map */
        try {
            tableModel.updateMapTableModel(gamePlayModel.getGameMap());
        } catch (Exception e) {
            e.printStackTrace(System.err);
            gamePlayFrame.displayErrorMessage(e.toString());
        }
        
        /* set the model for the main table */
        gamePlayFrame.getGameMapTable().setModel(tableModel.getModel());
        
        /* set the phase label */
        gamePlayFrame.getReinforcementControlPanel().setGameState(gamePlayModel.getGameState());
        
        /* set the player ID label */
        gamePlayFrame.getReinforcementControlPanel().setPlayerID(currentPlayer.getPlayerID());
        
        /* set the model for the player table */
        playerTerritoriesModel = new PlayerTerritoriesModel(currentPlayer);
        gamePlayFrame.getReinforcementControlPanel().getPlayerTerritoryTable().setModel(playerTerritoriesModel.getModel());
        gamePlayFrame.getReinforcementControlPanel().setTotalArmiesToPlace(currentPlayer.getUnallocatedArmies());

        /* Register Observer to Observable */
        gamePlayModel.addObserver(gamePlayFrame);
        playerTerritoriesModel.addObserver(gamePlayFrame.getReinforcementControlPanel());
        
        /* Register to be ActionListeners */
        this.gamePlayFrame.getReinforcementControlPanel().addBackButtonListener(e -> backToMainMenu());
        this.gamePlayFrame.getReinforcementControlPanel().addPlaceArmiesButtonListener(e -> gamePlayModel.placeArmies(currentPlayer));
        this.gamePlayFrame.getReinforcementControlPanel().addDoneButtonListener(e -> gamePlayModel.playPhases());
        
    }
    
    
    /* Private methods */
    
    /**
     * Close the current MapEditor screen and navigate back to the MainMenu screen
     */
    private void backToMainMenu() {
        callerController.invokeFrame();
        gamePlayFrame.dispatchEvent(new WindowEvent(gamePlayFrame, WindowEvent.WINDOW_CLOSING));
    }
    
    
}
