package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class SugarCaneFarmOptions extends BaseOption {
    public static SugarCaneFarmOptions Default = new SugarCaneFarmOptions(
            "item.prefab.sugar_cane_farm",
            "assets/prefab/structures/sugar_cane_farm.zip",
            "textures/gui/sugar_cane_farm_topdown.png",
            180,
            109,
            Direction.SOUTH,
            9,
            6,
            12,
            1,
            10,
            -1);

    protected SugarCaneFarmOptions(String translationString,
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
