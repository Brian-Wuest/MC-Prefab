package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class GreenHouseOptions extends BaseOption {
    public static GreenHouseOptions Default = new GreenHouseOptions(
            "item.prefab.green_house",
            "assets/prefab/structures/green_house.zip",
            "textures/gui/green_house_topdown.png",
            173,
            104,
            Direction.SOUTH,
            10,
            16,
            32,
            1,
            8,
            0);

    protected GreenHouseOptions(String translationString,
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
