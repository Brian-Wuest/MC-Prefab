package com.wuest.prefab.Structures.Config.Enums;

import net.minecraft.util.Direction;

public class AquaBaseOptions extends BaseOption {
    public static AquaBaseOptions Default = new AquaBaseOptions(
            "item.prefab.aqua_base",
            "assets/prefab/structures/aqua_base.zip",
            "textures/gui/aqua_base_topdown.png",
            119,
            160,
            Direction.SOUTH,
            27,
            25,
            38,
            1,
            12,
            0);

    protected AquaBaseOptions(String translationString,
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
