package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.EnumFacing;

public class DefenseBunkerOptions extends BaseOption {
    public static DefenseBunkerOptions Default = new DefenseBunkerOptions(
            "item.prefab.defense.bunker",
            "assets/prefab/structures/defense_bunker.zip",
            "textures/gui/defense_bunker_topdown.png",
            156,
            153,
            EnumFacing.SOUTH,
            17,
            32,
            32,
            1,
            15,
            0);

    protected DefenseBunkerOptions(String translationString,
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
