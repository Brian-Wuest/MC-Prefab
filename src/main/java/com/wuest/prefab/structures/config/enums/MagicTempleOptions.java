package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class MagicTempleOptions extends BaseOption {
    public static MagicTempleOptions Default = new MagicTempleOptions(
            "item.prefab.magic_temple",
            "assets/prefab/structures/magic_temple.zip",
            "textures/gui/magic_temple_topdown.png",
            156,
            146,
            Direction.SOUTH,
            13,
            12,
            13,
            1,
            6,
            0);

    protected MagicTempleOptions(String translationString,
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
