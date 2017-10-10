package model;

import model.game_entities.GameMap;
import model.helpers.GameMapHelper;
import model.ui_models.MapTableModel;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static utilities.Config.MAPS_FOLDER;

public class MapEditorModelTest {
    private static String fileName = "World.map";
    private static GameMap inputMap;
    private static GameMap outputMap;
    private static MapTableModel mapTableModel;
    
    /**
     * Prepare the test:
     * <ul>
     * <li>Load a specific map file into a input map object
     * <li>Make a tableModel from the map
     * <li>Save to file the tableModel
     * <li>Load the file that was just saved into a output map object
     * <li>
     * </ul>
     *
     * @throws Exception
     */
    @BeforeClass
    public static void roundTripMapFile() throws Exception {
        inputMap = GameMapHelper.loadGameMap(fileName);
        mapTableModel = new MapTableModel();
        mapTableModel.updateMapTableModel(inputMap);
        GameMapHelper.writeToFile(inputMap, MAPS_FOLDER + "/TEST.map");
        outputMap = GameMapHelper.loadGameMap("TEST.map");
    }
    
    /**
     * Remove the newly created map test file
     *
     * @throws IOException
     */
    @AfterClass
    public static void removeTestFile() throws IOException {
        Files.deleteIfExists(Paths.get(MAPS_FOLDER + "/TEST.map"));
    }
    
    /**
     * Assert if the input map has the same continents as the output map
     *
     * @throws Exception
     */
    @Test
    public void sameContinents() throws Exception {
        Assert.assertArrayEquals(inputMap.getContinentsNames().toArray(), outputMap.getContinentsNames().toArray());
    }
    
    /**
     * Assert if the input map has the same territories as the output map
     *
     * @throws Exception
     */
    @Test
    public void sameTerritories() throws Exception {
        Assert.assertArrayEquals(inputMap.getTerritories().values().toArray(), outputMap.getTerritories().values().toArray());
    }
    
}