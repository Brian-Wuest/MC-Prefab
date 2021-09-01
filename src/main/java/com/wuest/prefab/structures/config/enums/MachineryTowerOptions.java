package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.EnumFacing;

public class MachineryTowerOptions extends BaseOption {
    public static MachineryTowerOptions Default = new MachineryTowerOptions(
            "item.prefab.machinery.tower",
            "assets/prefab/structures/machinery_tower.zip",
            "textures/gui/machinery_tower_topdown.png",
            false,
            false);

    protected MachineryTowerOptions(String translationString,
                                    String assetLocation,
                                    String pictureLocation,
                                    boolean hasBedColor,
                                    boolean hasGlassColor) {
        super(translationString, assetLocation, pictureLocation, hasBedColor, hasGlassColor);
    }
}
