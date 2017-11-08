/* 
 * Risk Game Team 2
 * GamePlayFrame.java
 * Version 1.0
 * Oct 18, 2017
 */
package game_play.view.screens;

import game_play.controller.MainMenuController;
import game_play.model.GamePlayModel;
import game_play.view.ui_components.*;
import shared_resources.game_entities.Player;
import shared_resources.helper.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * GamePlayFrame is responsible for displaying all UI components to play the game.
 *
 * It is composed of a left side that renders the map as a table, and
 * an interchangeable right side for various control panels.
 *
 * @author Team 2
 * @version 1.0
 */
public class GamePlayFrame extends JFrame implements Observer {
    // region Attributes declaration
    private static final String TITLE = "Game Play";
    private static final int WIDTH = 1366;
    private static final int HEIGHT = 800;
    private JSplitPane topArea;
    private JPanel contentPane;
    private WorldDominationPanel worldDominationPanel;
    private PhaseViewPanel phaseViewPanel;
    private GameMapTable gameMapTable;
    private JPanel controlArea;
    private GameSetupPanel gameSetupPanel;
    private StartupPanel startupPanel;
    private ReinforcementPanel reinforcementPanel;
    private FortificationPanel fortificationPanel;
    private AttackingPanel attackingPanel;
    private Vector<Player> playersList;
    private MainMenuController callerController;
    // endregion
    
    // region Constructors
    
    /**
     * Instantiates and show the GamePlayFrame.
     *
     * @param callerController the main menu controller
     */
    public GamePlayFrame(MainMenuController callerController) {
        /* Setup main container */
        this.callerController = callerController;
        playersList = new Vector<>();
        setupContentPaneLayout();
        
        /* Setup world domination view & table area */
        worldDominationPanel = new WorldDominationPanel();
        gameMapTable = new GameMapTable();
        JPanel leftComponent = new JPanel();
        leftComponent.setLayout(new BoxLayout(leftComponent, BoxLayout.Y_AXIS));
        leftComponent.add(worldDominationPanel);
        leftComponent.add(new JScrollPane(gameMapTable));
        topArea.setLeftComponent(leftComponent);
        
        /* Setup Phase View */
        phaseViewPanel = new PhaseViewPanel();
        
        /* Setup main content pane */
        contentPane = new JPanel(new BorderLayout());
        contentPane.add(topArea, BorderLayout.CENTER);
        contentPane.add(phaseViewPanel, BorderLayout.PAGE_END);
        setContentPane(contentPane);
        
        /* Setup control area */
        setupControlArea();
        
        /* Setup & Display frame */
        UIHelper.displayJFrame(this, TITLE, WIDTH, HEIGHT, true);
    }
    // endregion
    
    // region Getters & Setters
    
    /**
     * Setup the layout for the main screen.
     */
    private void setupContentPaneLayout() {
        topArea = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT) {
            private final int location = 1000;
            
            {
                setDividerLocation(location);
            }
            
            @Override
            public int getLastDividerLocation() {
                return location;
            }
            
            @Override
            public int getDividerLocation() {
                return location;
            }
        };
    }
    
    /**
     * Setup ui components in control area.
     */
    private void setupControlArea() {
        /* Use CardLayout to let different panels share the same space */
        controlArea = new JPanel(new CardLayout());
        
        gameSetupPanel = new GameSetupPanel();
        controlArea.add(gameSetupPanel, GameSetupPanel.class.getName());
        startupPanel = new StartupPanel();
        controlArea.add(startupPanel, StartupPanel.class.getName());
        reinforcementPanel = new ReinforcementPanel();
        controlArea.add(reinforcementPanel, ReinforcementPanel.class.getName());
        fortificationPanel = new FortificationPanel();
        controlArea.add(fortificationPanel, FortificationPanel.class.getName());
        attackingPanel = new AttackingPanel();
        controlArea.add(attackingPanel, AttackingPanel.class.getName());
        
        topArea.setRightComponent(controlArea);
    }
    
    /**
     * Gets the fortification panel.
     *
     * @return the fortification panel
     */
    public FortificationPanel getFortificationPanel() {
        return fortificationPanel;
    }
    
    /**
     * @see javax.swing.JFrame#getContentPane()
     */
    @Override
    public JSplitPane getContentPane() {
        return topArea;
    }
    
    /**
     * Gets the game map table.
     *
     * @return the game map table
     */
    public GameMapTable getGameMapTable() {
        return gameMapTable;
    }
    
    // endregion
    
    // region Public methods
    
    /**
     * Gets the game setup panel.
     *
     * @return the game setup panel
     */
    public GameSetupPanel getGameSetupPanel() {
        return gameSetupPanel;
    }
    
    /**
     * Gets the startup panel.
     *
     * @return the startup panel
     */
    public StartupPanel getStartupPanel() {
        return startupPanel;
    }
    
    /**
     * Gets the reinforcement panel.
     *
     * @return the reinforcement panel
     */
    public ReinforcementPanel getReinforcementPanel() {
        return reinforcementPanel;
    }
    
    /**
     * Gets the world domination panel
     *
     * @return the world domination panel
     */
    public WorldDominationPanel getWorldDominationPanel() {
        return worldDominationPanel;
    }
    
    /**
     * Gets the phase view panel
     *
     * @return the phase view panel
     */
    public PhaseViewPanel getPhaseViewPanel() {
        return phaseViewPanel;
    }
    
    // endregion
    
    // region Private methods
    
    /**
     * Close GamePlayFrame, open MainMenuFrame
     */
    private void backToMainMenu() {
        this.dispose();
        UIHelper.invokeFrame(callerController.getMainMenuFrame());
    }

    /**
     * Gets the attacking panel
     *
     * @return the attacking panel
     */
    public AttackingPanel getAttackingPanel() {
        return attackingPanel;
    }
    // endregion
    
    // region MVC & Observer pattern methods
    
    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof GamePlayModel) {
            GamePlayModel gamePlayModel = (GamePlayModel) o;
            
            gameMapTable.setModel(gamePlayModel.getMapTableModel().getModel());
            
            CardLayout cardLayout = (CardLayout) controlArea.getLayout();
            switch (gamePlayModel.getGameState()) {
                case SETUP:
                    cardLayout.show(controlArea, GameSetupPanel.class.getName());
                    break;
                case STARTUP:
                    cardLayout.show(controlArea, StartupPanel.class.getName());
                    break;
                case PLAY:
                    switch (gamePlayModel.getCurrentPlayer().getGameState()) {
                        case REINFORCEMENT:
                        case TRADE_CARDS:
                            cardLayout.show(controlArea, ReinforcementPanel.class.getName());
                            break;
                        case ATTACK_PREPARE:
                        case ATTACK_BATTLE:
                            cardLayout.show(controlArea, AttackingPanel.class.getName());
                            break;
                        case FORTIFICATION:
                            cardLayout.show(controlArea, FortificationPanel.class.getName());
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
    
            Vector<Player> oldPlayersList = new Vector<>();
            for (Player player : playersList) {
                if (player.getPlayerStatus() == GamePlayModel.PLAYER_STATUS.IN_GAME) {
                    oldPlayersList.add(player);
                }
            }
    
            playersList.clear();
            for (Player player : gamePlayModel.getPlayers()) {
                if (player.getPlayerStatus() == GamePlayModel.PLAYER_STATUS.IN_GAME) {
                    playersList.add(player);
                }
            }
            
            if (oldPlayersList.size() > playersList.size()) {
                if (playersList.size() != 1) {
                    for (Player player : oldPlayersList) {
                        if (!playersList.contains(player)) {
                            UIHelper.displayMessage(this, String.format("%s has been eliminated", player.getPlayerName()));
                        }
                    }
                } else {
                    Player player = playersList.firstElement();
                    UIHelper.displayMessage(this, String.format("%s is the winner", player.getPlayerName()));
                    backToMainMenu();
                }
            }
        }
    }
    // endregion
}
