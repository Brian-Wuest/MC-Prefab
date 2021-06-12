package com.wuest.prefab.Structures.Config.Enums;

import net.minecraft.util.Direction;

public class WorkshopOptions extends BaseOption {
    public static WorkshopOptions Default = new WorkshopOptions(
            "item.prefab.workshop",
            "assets/prefab/structures/workshop.zip",
            "textures/gui/workshop_topdown.png",
            174,
            131,
            Direction.SOUTH,
            12,
            20,
            21,
            1,
            10,
            0);

    protected WorkshopOptions(String translationString,
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