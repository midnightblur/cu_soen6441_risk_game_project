package Models;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GameMap {
    // Private data member of Models.GameMap class
    private String mapPath;
    private String author;
    private String image;
    private boolean wrapping;
    private String scroll;
    private boolean warning;

    private Map<String, Territory> territories;
    private Vector<Adjacency> adjacencies;
    private Vector<Continent> continents;
    private int minX, maxX, minY, maxY;

    // Public methods
    public GameMap(String mapPath) {
        this.mapPath = mapPath;
        this.territories = new HashMap<>();
        this.adjacencies = new Vector<>();
        this.continents = new Vector<>();
        minX = minY = Integer.MAX_VALUE;
        maxX = maxY = Integer.MIN_VALUE;
    }

    public Map<String, Territory> getTerritories() {
        return territories;
    }

    public Vector<Adjacency> getAdjacencies() {
        return adjacencies;
    }

    public Vector<Continent> getContinents() {
        return continents;
    }

    public String getMapPath() {
        return mapPath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isWrapping() {
        return wrapping;
    }

    public void setWrapping(boolean wrapping) {
        this.wrapping = wrapping;
    }

    public String getScroll() {
        return scroll;
    }

    public void setScroll(String scroll) {
        this.scroll = scroll;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }

    public void addEdge(Adjacency adjacency) {
        if (!adjacencies.contains(adjacency))
            adjacencies.add(adjacency);
    }

    public void removeEdge(Adjacency adjacency) {
        if (adjacencies.contains(adjacency))
            adjacencies.remove(adjacency);
    }

    public void addTerritory(Territory territory) {
        if (!territories.containsKey(territory.getName())) {
            territories.put(territory.getName(), territory);

            if (minX > territory.getX())
                minX = territory.getX();
            if (minY > territory.getY())
                minY = territory.getY();
            if (maxX < territory.getX())
                maxX = territory.getX();
            if (maxY < territory.getY())
                maxY = territory.getY();
        }

    }

    public void removeTerritory(Territory territory) {
        if (territories.containsKey(territory.getName()))
            territories.remove(territory);
    }

    public void addContinent(Continent continent) {
        if (!continents.contains(continent))
            continents.add(continent);
    }
}
