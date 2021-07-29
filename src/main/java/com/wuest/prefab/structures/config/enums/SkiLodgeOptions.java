package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class SkiLodgeOptions extends BaseOption {
    public static SkiLodgeOptions Default = new SkiLodgeOptions(
            "item.prefab.ski_lodge",
            "assets/prefab/structures/ski_lodge.zip",
            "textures/gui/ski_lodge_topdown.png",
            Direction.SOUTH,
            25,
            46,
            35,
            1,
            20,
            -1,
            false,
            false);

    protected SkiLodgeOptions(String translationString,
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
