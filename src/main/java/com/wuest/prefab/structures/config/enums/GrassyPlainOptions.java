package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.EnumFacing;

public class GrassyPlainOptions extends BaseOption {
    public static GrassyPlainOptions Default = new GrassyPlainOptions(
            "item.prefab.grassy_plain",
            "assets/prefab/structures/grassy_plain.zip",
            "textures/gui/grassy_plain_topdown.png",
            160,
            160,
            EnumFacing.SOUTH,
            4,
            15,
            15,
            1,
            8,
            -1);

    protected GrassyPlainOptions(String translationString,
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
