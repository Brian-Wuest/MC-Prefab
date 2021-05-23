package com.wuest.prefab.Structures.Config.Enums;

import net.minecraft.util.Direction;

public class SaloonOptions extends BaseOption {
    public static SaloonOptions Default = new SaloonOptions(
            "item.prefab.saloon",
            "assets/prefab/structures/saloon.zip",
            "textures/gui/saloon_topdown.png",
            130,
            170,
            Direction.SOUTH,
            14,
            28,
            16,
            1,
            10,
            -1);

    protected SaloonOptions(String translationString,
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
