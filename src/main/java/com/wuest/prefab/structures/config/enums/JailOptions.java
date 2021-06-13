package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.EnumFacing;

public class JailOptions extends BaseOption {
    public static JailOptions Default = new JailOptions(
            "item.prefab.jail",
            "assets/prefab/structures/jail.zip",
            "textures/gui/jail_topdown.png",
            175,
            131,
            EnumFacing.SOUTH,
            14,
            28,
            33,
            1,
            25,
            -3);

    protected JailOptions(String translationString,
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
