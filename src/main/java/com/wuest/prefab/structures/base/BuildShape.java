package com.wuest.prefab.structures.base;

import com.google.gson.annotations.Expose;
import net.minecraft.core.Direction;

/**
 * This class holds the general shape of the structure.
 *
 * @author WuestMan
 */
public class BuildShape {
    @Expose
    private int width;

    @Expose
    private int height;

    @Expose
    private int length;

    @Expose
    private Direction direction;

    public BuildShape() {
        this.Initialize();
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int value) {
        this.width = value;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int value) {
        this.height = value;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int value) {
        this.length = value;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction value) {
        this.direction = value;
    }

    public void Initialize() {
        this.width = 0;
        this.height = 0;
        this.length = 0;
        this.direction = Direction.NORTH;
    }

    /**
     * Clones this instance.
     *
     * @return A new instance of this class with the same property values.
     */
    public BuildShape Clone() {
        BuildShape clone = new BuildShape();
        clone.setDirection(this.direction);
        clone.setHeight(this.height);
        clone.setLength(this.length);
        clone.setWidth(this.width);

        return clone;
    }
}
