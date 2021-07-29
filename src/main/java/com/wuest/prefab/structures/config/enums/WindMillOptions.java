package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class WindMillOptions extends BaseOption {
    public static WindMillOptions Default = new WindMillOptions(
            "item.prefab.wind_mill",
            "assets/prefab/structures/wind_mill.zip",
            "textures/gui/wind_mill_topdown.png",
            Direction.SOUTH,
            31,
            17,
            13,
            1,
            8,
            -1,
            false,
            false);

    protected WindMillOptions(String translationString,
                              String assetLocation,
                              String pictureLocation,
                              Direction direction,
                              int height,
                              int width,
                              int length,
                              int offsetParallelToPlayer,
                              int offsetToLeftOfPlayer,
                              int heightOffset,
                              boolean hasBedColor,
                              boolean hasGlassColor) {
        super(
                translationString,
                assetLocation,
                pictureLocation,
                direction,
                height,
                width,
                length,
                offsetParallelToPlayer,
                offsetToLeftOfPlayer,
                heightOffset,
                hasBedColor,
                hasGlassColor);
    }
}
