package com.wuest.prefab.blocks;

import com.google.common.base.Strings;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.IStringSerializable;

public enum FullDyeColor implements IStringSerializable {
    WHITE(0, "white", EnumDyeColor.WHITE),
    ORANGE(1, "orange", EnumDyeColor.ORANGE),
    MAGENTA(2, "magenta", EnumDyeColor.MAGENTA),
    LIGHT_BLUE(3, "lightBlue", EnumDyeColor.LIGHT_BLUE),
    YELLOW(4, "yellow", EnumDyeColor.YELLOW),
    LIME(5, "lime", EnumDyeColor.LIME),
    PINK(6, "pink", EnumDyeColor.PINK),
    GRAY(7, "gray", EnumDyeColor.GRAY),
    LIGHT_GRAY(8, "silver", EnumDyeColor.SILVER),
    CYAN(9, "cyan", EnumDyeColor.CYAN),
    PURPLE(10, "purple", EnumDyeColor.PURPLE),
    BLUE(11, "blue", EnumDyeColor.BLUE),
    BROWN(12, "brown", EnumDyeColor.BROWN),
    GREEN(13, "green", EnumDyeColor.GREEN),
    RED(14, "red", EnumDyeColor.RED),
    BLACK(15, "black", EnumDyeColor.BLACK),
    CLEAR(16, "clear", null);

    private final int id;
    private final String name;
    private final EnumDyeColor linkedColor;

    FullDyeColor(int id, String name, EnumDyeColor linkedColor) {
        this.id = id;
        this.name = name;
        this.linkedColor = linkedColor;
    }

    public static FullDyeColor ById(int id) {
        FullDyeColor returnValue = FullDyeColor.WHITE;

        for (FullDyeColor value : FullDyeColor.values()) {
            if (value.id == id) {
                returnValue = value;
                break;
            }
        }

        return returnValue;
    }

    public static FullDyeColor ByName(String name) {
        FullDyeColor returnValue = FullDyeColor.CLEAR;

        if (!Strings.isNullOrEmpty(name)) {
            for (FullDyeColor value : FullDyeColor.values()) {
                if (value.name.equalsIgnoreCase(name)) {
                    returnValue = value;
                    break;
                }
            }
        }

        return returnValue;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public EnumDyeColor getLinkedColor() {
        return this.linkedColor;
    }

    public String toString() {
        return this.name;
    }

    public String getSerializedName() {
        return this.name;
    }
}
