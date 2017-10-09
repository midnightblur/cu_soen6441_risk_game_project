package model.ui_models;

import model.game_entities.GameMap;
import model.helpers.GameMapHandler;

import java.util.Observable;
import java.util.Vector;

public class MapEditorModel extends Observable {
    private static final String CREATE_NEW_CONTINENT_ITEM = "<Create New Continent>";
    private static final String CREATE_NEW_TERRITORY_ITEM = "<Create New Territory>";
    private GameMap gameMap;
    private MapTableModel mapTableModel;
    private DropDownModel continentsDropdownModel;
    private DropDownModel territoriesDropdownModel;
    
    public MapEditorModel() {
        mapTableModel = new MapTableModel();
    }
    
    public void loadNewGameMap(String mapName) throws Exception {
        gameMap = GameMapHandler.loadGameMap(mapName);
        updateModels();
        broadcastGameMapChanges();
    }
    
    /* Getters & Setters */
    public GameMap getGameMap() {
        return gameMap;
    }
    
    public MapTableModel getMapTableModel() {
        return mapTableModel;
    }
    
    public DropDownModel getContinentsDropdownModel() {
        return continentsDropdownModel;
    }
    
    /**
     * Update the MapTableModel and notify the Observer
     */
    private void broadcastGameMapChanges() {
        setChanged();
        notifyObservers();
    }
    
    private void updateListOfContinents() {
        Vector<String> continentsList = new Vector<>();
        continentsList.add(CREATE_NEW_CONTINENT_ITEM);
        continentsList.addAll(gameMap.getContinentsNames());
        continentsDropdownModel = new DropDownModel(continentsList);
    }
    
    private void updateListOfTerritories() {
        Vector<String> territoriesList = new Vector<>();
        territoriesList.add(CREATE_NEW_TERRITORY_ITEM);
        territoriesList.addAll(gameMap.getTerritoriesNames());
        continentsDropdownModel = new DropDownModel(territoriesList);
    }
    
    private void updateModels() {
        mapTableModel.updateMapTableModel(gameMap);
        updateListOfContinents();
        updateListOfTerritories();
    }
}
