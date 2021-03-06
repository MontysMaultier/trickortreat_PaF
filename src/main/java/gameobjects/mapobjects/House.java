package main.java.gameobjects.mapobjects;

import main.java.pattern.Observable;
import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.districts.District;
import main.java.map.Map;
import main.java.map.MapObject;
import main.java.map.Tile;

/**
 * the super class of every specialized house object
 */
public abstract class House extends MapObject implements Visitable {

    protected Tile[][] tileset;

    protected boolean isUnvisited = true;

    protected District district = null;

    protected Player insidePlayer = null;


    /**
     * House constructor.
     * <p>
     * fills the whole tileset with House-Tiles and creates a Door-Tile at a suitable place.
     *
     * @param x          x coordinate on game map
     * @param y          y coordinate on game map
     * @param tileWidth  width
     * @param tileHeight height
     */
    public House(int x, int y, int tileWidth, int tileHeight) {
        super(x, y, tileWidth, tileHeight);
        this.tileset = new Tile[tileWidth][tileHeight];
    }

    /**
     * this constructor is only here so the House decorator can have an own constructor, witch only accepts an house
     * instance.
     */
    public House() {
        super();
    }

    /**
     * gets an Tile of the House by its row and column index
     *
     * @param r row index
     * @param c column index
     * @return the tile that is indexed by r and c
     */
    public Tile getTileByTileIndex(int r, int c) {
        try {
            return tileset[r][c];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * district setter
     *
     * @param district district
     */
    public void setDistrict(District district) {
        this.district = district;
    }

    /**
     * method that should be called when a player visits a house. Special houses like the town hall or witch house
     * override this method.
     *
     * @param player the player entity that visits the current house instance
     * @see TownHall
     */
    public void visit(Player player) {
        if (isUnvisited) {
            player.notifyObservers(player);
        }
        this.isUnvisited = false;
        notifyObservers(observers);
        player.notifyObservers(player);

    }

    /**
     * after the visit of a house (which is observed by a Controller (network- or GameController)) and the mandatory
     * switch of tiles or an whole tileset (inside of houses), this method is called to ensure that the game map is
     * on the cutting edge.
     *
     * @see main.java.GameController#update(Observable, Object)
     * @see House#visit(Player)
     */
    public void updateMap() {

        Tile[][][] map = Map.getInstance().getMap();

        for (int y = 0; y < tileset[0].length; y++) {
            for (int x = 0; x < tileset.length; x++) {
                map[x + this.getX()][y + this.getY()][1] = tileset[x][y];
            }
        }
        Map.getInstance().setMap(map);
    }

    /**
     * returns if this house has not been visited yet or not.
     *
     * @return true/false
     */
    public boolean isUnvisited() {
        return isUnvisited;
    }

    /**
     * setter unvisited
     *
     * @param unvisited unvisited
     */
    public void setUnvisited(boolean unvisited) {
        isUnvisited = unvisited;
    }

    /**
     * method that is implemented in inheriting  classes.
     * This makes it possible for the different types of houses to change tiles and tile positions when they are visited-
     */
    public abstract void repaintAfterVisit();

    public void decorateHouse() {

    }

}