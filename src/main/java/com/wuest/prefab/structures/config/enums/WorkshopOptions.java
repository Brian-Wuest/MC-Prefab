package com.wuest.prefab.structures.config.enums;

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
            0,
            true,
            true);

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