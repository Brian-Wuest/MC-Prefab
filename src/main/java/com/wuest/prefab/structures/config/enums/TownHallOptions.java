package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class TownHallOptions extends BaseOption {
    public static TownHallOptions Default = new TownHallOptions(
            "item.prefab.town_hall",
            "assets/prefab/structures/town_hall.zip",
            "textures/gui/town_hall_topdown.png",
            173,
            89,
            Direction.SOUTH,
            12,
            27,
            27,
            1,
            20,
            -1,
            false,
            false);

    protected TownHallOptions(String translationString,
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
