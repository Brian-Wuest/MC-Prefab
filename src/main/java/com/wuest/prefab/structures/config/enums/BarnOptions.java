package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.EnumFacing;

public class BarnOptions extends BaseOption {
    public static BarnOptions Default = new BarnOptions(
            "item.prefab.barn",
            "assets/prefab/structures/barn.zip",
            "textures/gui/barn_topdown.png",
            false,
            false);

    protected BarnOptions(String translationString,
                          String assetLocation,
                          String pictureLocation,
                          boolean hasBedColor,
                          boolean hasGlassColor) {
        super(translationString, assetLocation, pictureLocation, hasBedColor, hasGlassColor);
    }
}