package main.java.map;

public class Placeable {
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
        this.offset = 1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOffset() {
        return offset;
    }

    /**
     * checks whether this placeble intersects with anotehr one. It is important to note that the offset is only
     * computed from this object, not both.
     *
     * @param other
     * @return
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
     * @param other
     * @return
     */
    public boolean contains(Placeable other) {
        if ((other.x + other.width) < (this.x + this.width)
                && (other.x) > (this.x)
                && (other.y) > (this.y)
                && (other.y + other.height) < (this.y + this.height)
        ) {
            return true;
        } else {
            return false;
        }
    }
}