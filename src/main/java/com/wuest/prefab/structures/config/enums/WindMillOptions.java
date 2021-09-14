package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class WindMillOptions extends BaseOption {
    public static WindMillOptions Default = new WindMillOptions(
            "item.prefab.wind_mill",
            "assets/prefab/structures/wind_mill.zip",
            "textures/gui/wind_mill_topdown.png",
            false,
            false);

    protected WindMillOptions(String translationString,
                              String assetLocation,
                              String pictureLocation,
                              boolean hasBedColor,
                              boolean hasGlassColor) {
        super(
                translationString,
                assetLocation,
                pictureLocation,
                hasBedColor,
                hasGlassColor);
    }
}
