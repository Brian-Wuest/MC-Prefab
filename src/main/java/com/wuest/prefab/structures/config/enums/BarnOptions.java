package com.wuest.prefab.Structures.Config.Enums;

import net.minecraft.util.Direction;

public class BarnOptions extends BaseOption {
    public static BarnOptions Default = new BarnOptions(
            "item.prefab.barn",
            "assets/prefab/structures/barn.zip",
            "textures/gui/barn_topdown.png",
            164,
            160,
            Direction.SOUTH,
            10,
            30,
            25,
            1,
            15,
            0);

    protected BarnOptions(String translationString,
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