package main.java.map;

import main.java.pattern.Observable;

import java.io.Serializable;


public class Placeable extends Observable implements Serializable {
    private static final long serialVersionUID = 4762734196222645657L;
    private int x;
    private int y;
    // in tiles
    private int width;
    private int height;
    // in tiles
    private int offset = 1;

    public Placeable(int x, int y, int width, int height, int offset) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.offset = offset;
    }

    public Placeable(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // offset is already set to one
    }

    /**
     * this constructor is only here so the House decorator can have an own constructor, witch only accepts an house
     * instance.
     */
    public Placeable() {

    }

    /**
     * return x position of Placeble
     *
     * @return x of Placeble
     */
    public int getX() {
        return x;
    }

    /**
     * sets the x position of Placeble
     *
     * @param x x position
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * return y position of Placeble
     *
     * @return y of Placeble
     */
    public int getY() {
        return y;
    }

    /**
     * sets the y position of Placeble
     *
     * @param y y position
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Method to change the offset of an Placeble to zero.
     * <p>
     * It is intentionally not named "setOffset" because this method should only be called to change the offset
     * while the map is created. This change in off set is necessary to ensure that objects like houses are placed
     * with a offset of 1 and later have no offset so the player-collision is detected right.
     *
     * @see main.java.map.MapGenerator -> findObjectSpot(MapObject)
     */
    public void disableOffset() {
        this.offset = 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * checks whether this placeble intersects with anotehr one. It is important to note that the offset is only
     * computed from this object, not both.
     *
     * @param other the other placeble
     * @return true if intersects otherwise false
     */
    public boolean intersects(Placeable other) {
        return (this.x - this.offset < other.x + other.width &&
                this.x + this.width + this.offset > other.x &&
                this.y - this.offset < other.y + other.height &&
                this.y + this.height + this.offset > other.y);
    }

    /**
     * checks whether this placeble contains another one.
     *
     * @param other the other placeble
     * @return true if object is contained otherwise false
     */
    public boolean contains(Placeable other) {
        return (other.x + other.width) < (this.x + this.width)
                && (other.x) > (this.x)
                && (other.y) > (this.y)
                && (other.y + other.height) < (this.y + this.height);
    }
}
