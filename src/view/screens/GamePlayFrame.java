package view.screens;

import view.helpers.UIHelper;
import view.ui_components.MapTable;
import view.ui_components.ReinforcementControlPanel;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class GamePlayFrame extends JFrame implements Observer {
    private static final String TITLE = "Game Play";
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 800;
    
    private JSplitPane contentPane;
    private MapTable gameMapTable;
    private ReinforcementControlPanel reinforcementControlPanel;
    
    /* Constructors */
    public GamePlayFrame() {
        /* Setup main container */
        setupContentPaneLayout();
        setContentPane(contentPane);
        
        /* Setup table area */
        gameMapTable = new MapTable();
        contentPane.setLeftComponent(new JScrollPane(gameMapTable));
        
        /* Setup control panel area */
        reinforcementControlPanel = new ReinforcementControlPanel();
        contentPane.setRightComponent(reinforcementControlPanel);
        
        /* Setup & Display frame */
        UIHelper.displayJFrame(this, TITLE, WIDTH, HEIGHT, false);
    }
    
    /* Getters & Setters */
    @Override
    public JSplitPane getContentPane() {
        return contentPane;
    }
    
    public MapTable getGameMapTable() {
        return gameMapTable;
    }
    
    public ReinforcementControlPanel getReinforcementControlPanel() {
        return reinforcementControlPanel;
    }
    
    
    /* Public methods */
    
    /**
     * Displays error messages on UI
     *
     * @param errorMessage Error message string
     */
    public void displayErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage);
    }
    
    
    /* Private methods */
    
    private void setupContentPaneLayout() {
        contentPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT) {
            private final int location = 1100;
            
            {
                setDividerLocation(location);
            }
            
            @Override
            public int getDividerLocation() {
                return location;
            }
            
            @Override
            public int getLastDividerLocation() {
                return location;
            }
        };
        contentPane.setDividerLocation(1100);
        contentPane.setResizeWeight(.75d);
    }
    
    @Override
    public void update(Observable o, Object arg) {
//        if (o instanceof DropDownModel)
    }
}
