package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class BarnOptions extends BaseOption {
    public static BarnOptions Default = new BarnOptions(
            "item.prefab.item_barn",
            "assets/prefab/structures/barn.zip",
            "textures/gui/barn_topdown.png",
            164,
            160,
            Direction.SOUTH,
            10,
            30,
            25,
            1,
            15,
            0,
            false,
            false);

    protected BarnOptions(String translationString,
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
                          int heightOffset,
                          boolean hasBedColor,
                          boolean hasGlassColor) {
        super(
                translationString,
                assetLocation,
                pictureLocation,
                imageWidth,
                imageHeight,
                direction,
                height,
                width,
                length,
                offsetParallelToPlayer,
                offsetToLeftOfPlayer,
                heightOffset,
                hasBedColor,
                hasGlassColor);
    }
}