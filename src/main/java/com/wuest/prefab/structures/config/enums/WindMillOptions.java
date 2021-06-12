package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class WindMillOptions extends BaseOption {
    public static WindMillOptions Default = new WindMillOptions(
            "item.prefab.wind_mill",
            "assets/prefab/structures/wind_mill.zip",
            "textures/gui/wind_mill_topdown.png",
            102,
            176,
            Direction.SOUTH,
            31,
            17,
            13,
            1,
            8,
            -1);

    protected WindMillOptions(String translationString,
                              String assetLocation,
                              String pictureLocation,
                              int imageWidth,
                              int imageHeight,
                              Direction direction,
                              int height,
                              int width,
                              int length,
                              int offsetParallelToPlayer,
                              int offsetToLeftOfPlayer,
                              int heightOffset) {
        super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight, direction, height, width, length, offsetParallelToPlayer, offsetToLeftOfPlayer, heightOffset);
    }
}
