package controller;

import model.DropDownModel;
import model.GameMapHandler;
import model.MapTableModel;
import model.RiskGame;
import util.Config;
import view.MapEditor;

import static model.GameMapHandler.loadGameMap;


/**
 * Controller class holding methods used in Map Editor module of the game
 */

public class MapEditorController {
    
    private MapEditor theView;
    private MapTableModel theMapTableModel;
    private DropDownModel mapDropDownModel;
    
    public MapEditorController() {
        
        //create the View object
        theView = new MapEditor();
        
        //create the Model object
        try {
            //theMapTableModel = new MapTableModel(loadGameMap(Config.DEFAULT_MAP));
            theMapTableModel = new MapTableModel(RiskGame.getInstance().getGameMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // set the table model for the view
        theView.getMyTable().setModel(theMapTableModel.getModel());
        theView.resizeColumns(theView.getMyTable());
        
        // Subscribe the view as observer to model changes
        theMapTableModel.addObserver(theView);
        
        // register this instance of controller as listener to the view
        theView.addActionListener(e -> {
            try {   // update the model when button is clicked
                theMapTableModel.updateMapTableModel(loadGameMap(theView.getMap()));
            } catch (Exception e1) {
                theView.displayErrorMessage(e1.toString());
            }
        });
        
        /* Get the available maps and populate the dropdown */
        mapDropDownModel = new DropDownModel(GameMapHandler.getMapsInFolder(Config.MAPS_FOLDER));
        theView.setDropdownModel(mapDropDownModel);
        mapDropDownModel.addListDataListener(theView.mapsDropdown);
    }
    
}
