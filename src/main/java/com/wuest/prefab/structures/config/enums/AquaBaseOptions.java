package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.EnumFacing;

public class AquaBaseOptions extends BaseOption {
    public static AquaBaseOptions Default = new AquaBaseOptions(
            "item.prefab.aqua_base",
            "assets/prefab/structures/aqua_base.zip",
            "textures/gui/aqua_base_topdown.png",
            119,
            160,
            EnumFacing.SOUTH,
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
                              EnumFacing direction,
                              int height,
                              int width,
                              int length,
                              int offsetParallelToPlayer,
                              int offsetToLeftOfPlayer,
                              int heightOffset) {
        super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight, direction, height, width, length, offsetParallelToPlayer, offsetToLeftOfPlayer, heightOffset);
    }
}
