package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class HorseStableOptions extends BaseOption {
    public static HorseStableOptions Default = new HorseStableOptions(
            "item.prefab.item_horse_stable",
            "assets/prefab/structures/horsestable.zip",
            "textures/gui/horse_stable_top_down.png",
            164,
            160,
            Direction.SOUTH,
            11,
            8,
            10,
            1,
            4,
            -4);

    protected HorseStableOptions(String translationString,
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
