package view.ui_components;

import model.game_entities.Player;
import model.ui_models.PlayerTerritoriesModel;
import utilities.Config.GAME_STATES;
import view.helpers.IntegerEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class ReinforcementPanel extends JPanel implements Observer {
    
    private static final String PLACE_ARMIES_BUTTON_LABEL = "Place armies";
    private static final String TOTAL_ARMIES_TO_PLACE_LABEL = "Armies to be placed: ";
    private static final String DONE_BUTTON_LABEL = "Done";
    private static final String TRADE_CARDS_BUTTON_LABEL = "Trade Cards";
    private static final String ARMIES_TO_PLACE_LABEL = "Use table to place armies:";
    
    private JButton tradeCardsButton;
    private JButton doneButton;
    private JButton placeArmiesButton;
    private JLabel gameState;
    private JLabel playerID;
    private JLabel totalArmiesToPlace;
    private JLabel howManyArmiesToPlace;
    private JTable playerTerritoryTable;
    
    /* Constructors */
    public ReinforcementPanel() {
        gameState = new JLabel();
        gameState.setFont(new Font("Sans Serif", Font.ITALIC, 20));
        playerID = new JLabel();
        playerID.setFont(new Font("Sans Serif", Font.BOLD, 20));
        tradeCardsButton = new JButton(TRADE_CARDS_BUTTON_LABEL);
        totalArmiesToPlace = new JLabel();
        totalArmiesToPlace.setFont(new Font("Sans Serif", Font.BOLD, 16));
        howManyArmiesToPlace = new JLabel(ARMIES_TO_PLACE_LABEL);
        playerTerritoryTable = new JTable() {
            @Override   // set the data type for each column
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Integer.class;
                }
                return getValueAt(0, column).getClass();
            }
            
            @Override   // only allow editing in second column
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        
        playerTerritoryTable.setDefaultEditor(Integer.class, new IntegerEditor());
        doneButton = new JButton(DONE_BUTTON_LABEL);
        placeArmiesButton = new JButton(PLACE_ARMIES_BUTTON_LABEL);
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        
        /* Control panel */
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        
        controlPanel.add(gameState);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        controlPanel.add(playerID);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        controlPanel.add(tradeCardsButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        controlPanel.add(totalArmiesToPlace);
        controlPanel.add(howManyArmiesToPlace);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(new JScrollPane(playerTerritoryTable));
        controlPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JPanel panel_1 = new JPanel();
        panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.PAGE_AXIS));
        panel_1.add(placeArmiesButton);
        panel_1.add(Box.createRigidArea(new Dimension(0, 20)));
        panel_1.add(doneButton);
        
        controlPanel.add(panel_1);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        
        add(controlPanel);
        
    }
    
    /* Getters & Setters */
    public JTable getPlayerTerritoryTable() {
        return playerTerritoryTable;
    }
    
    public void setGameState(GAME_STATES gameState) {
        this.gameState.setText(gameState.toString());
    }
    
    public void setTotalArmiesToPlace(int totalArmiesToPlace) {
        this.totalArmiesToPlace.setText(TOTAL_ARMIES_TO_PLACE_LABEL + Integer.toString(totalArmiesToPlace));
    }
    
    public void setPlayerID(int playerID) {
        this.playerID.setText("Player " + playerID);
    }
    
    
    /* MVC & Observer pattern methods */
    public void addPlaceArmiesButtonListener(ActionListener listenerForPlaceArmiesButton) {
        placeArmiesButton.addActionListener(listenerForPlaceArmiesButton);
    }
    
    public void addDoneButtonListener(ActionListener listenerForDoneButton) {
        doneButton.addActionListener(listenerForDoneButton);
    }
    
    public void addTradeCardsButtonListener(ActionListener listenerForTradeCardsButton) {
        tradeCardsButton.addActionListener(listenerForTradeCardsButton);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof PlayerTerritoriesModel) {
            playerTerritoryTable.setModel(((PlayerTerritoriesModel) o).getModel());
        }
        if (o instanceof Player) {
            setPlayerID(((Player) o).getPlayerID());
            setTotalArmiesToPlace(((Player) o).getUnallocatedArmies());
        }
    }
    
    /* Public methods */
    
    /* Private methods */
    
}
