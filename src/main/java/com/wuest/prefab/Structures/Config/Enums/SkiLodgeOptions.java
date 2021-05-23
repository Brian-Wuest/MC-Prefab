package com.wuest.prefab.Structures.Config.Enums;

import net.minecraft.util.Direction;

public class SkiLodgeOptions extends BaseOption {
    public static SkiLodgeOptions Default = new SkiLodgeOptions(
            "item.prefab.ski_lodge",
            "assets/prefab/structures/ski_lodge.zip",
            "textures/gui/ski_lodge_topdown.png",
            180,
            137,
            Direction.SOUTH,
            25,
            46,
            35,
            1,
            20,
            -1);

    protected SkiLodgeOptions(String translationString,
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
