package model;

import model.game_entities.Continent;
import model.game_entities.GameMap;
import model.game_entities.Territory;
import utilities.BidiArrayComparator;

import javax.swing.table.DefaultTableModel;
import java.util.Arrays;
import java.util.Observable;
import java.util.Vector;

/**
 * Model to hold the map data in order to displayJFrame it within a JTable
 */
public class MapTableModel extends Observable {
    private DefaultTableModel model = new DefaultTableModel();
    private String[][] rows;
    private Vector<String> columns = new Vector<>();
    private GameMap map;
    
    /* Constructors */
    public MapTableModel() {
        model = new DefaultTableModel();
    }
    
    /**
     * Updating the table model and notifying the subscribers
     * This method is also used by the constructor
     *
     * @param map the map object that provides the data
     *
     * @return a table model to be used to generate the view
     */
    public DefaultTableModel updateMapTableModel(GameMap map) {
        this.map = map;
        /* clears the model data and reinitialize it with new values */
        model.setRowCount(0);
        columns.clear();
        columns.add("Continent");
        columns.add("Territory");
        columns.add("Neighbors");
        if (RiskGame.getInstance().getGameState().getValue() > 3) {
            columns.add("Owner");
            columns.add("Armies");
        }
    
        rows = new String[this.map.getTerritoriesCount() + this.map.getContinentsCount()][columns.size()];
        int i = 0;
        /* add continents */
        for (Continent continent : this.map.getContinents().values()) {
            rows[i][0] = continent.getName();
            if (RiskGame.getInstance().getGameState().getValue() > 3) {
                rows[i][3] = continent.getContinentOwner();
                rows[i][4] = Integer.toString(continent.getContinentArmies());
            }
            i++;
        }
    
        for (Territory territory : this.map.getTerritories().values()) {
            rows[i][0] = territory.getContinent().getName();
            rows[i][1] = territory.getName();
            rows[i][2] = territory.getNeighbors().toString().replace("[", "").replace("]", "");
            
            /* add countries and their information */
            if (RiskGame.getInstance().getGameState().getValue() > 3) {
                rows[i][3] = "Player " + Integer.toString(territory.getOwner().getPlayerID());
                rows[i][4] = Integer.toString(territory.getArmies());
            }
            
            i++;
        }
        this.model.setColumnIdentifiers(columns);
        Arrays.sort(rows, new BidiArrayComparator(0));        // perform sort on Continents column
        groupRows();                                                  // 'group' the rows
        
        /* specify that model state changed and notify observers */
        setChanged();
        notifyObservers();
        
        return model;
    }
    
    /* Getters & setters */
    
    public DefaultTableModel getModel() {
        return model;
    }
    
    public GameMap getMap() {
        return map;
    }
    
    /* Public methods */
    
    /**
     * If rows have same continent as previous row, clear the continent name
     */
    private void groupRows() {
        String prevGroup = "";
        for (String[] row : rows) {
            if (row[0].equals(prevGroup)) {
                row[0] = "  ";
            } else {
                // add a row and shift down the rest
                prevGroup = row[0];
            }
            this.model.addRow(row);
        }
    }
    
}