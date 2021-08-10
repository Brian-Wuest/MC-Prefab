package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class BarnOptions extends BaseOption {
    public static BarnOptions Default = new BarnOptions(
            "item.prefab.item_barn",
            "assets/prefab/structures/barn.zip",
            "textures/gui/barn_topdown.png",
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