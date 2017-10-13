package view.screens;

import view.helpers.UIHelper;
import view.ui_components.EditMapPanel;
import view.ui_components.MapTable;

import javax.swing.*;

public class MapEditorFrame extends JFrame {
    private static final String TITLE = "Map Editor";
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 800;
    
    private JSplitPane contentPane;
    private MapTable mapTable;
    private EditMapPanel editMapPanel;
    
    /* Constructors */
    public MapEditorFrame() {
        /* Setup main container */
        setupContentPaneLayout();
        setContentPane(contentPane);
        
        /* Setup table area */
        mapTable = new MapTable();
        contentPane.setLeftComponent(new JScrollPane(mapTable));
        
        /* Setup control panel area */
        editMapPanel = new EditMapPanel();
        contentPane.setRightComponent(editMapPanel);
        
        /* Setup & Display frame */
        UIHelper.displayJFrame(this, TITLE, WIDTH, HEIGHT, false);
    }
    
    /* Getters & Setters */
    @Override
    public JSplitPane getContentPane() {
        return contentPane;
    }
    
    public MapTable getMapTable() {
        return mapTable;
    }
    
    public EditMapPanel getEditMapPanel() {
        return editMapPanel;
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
}
