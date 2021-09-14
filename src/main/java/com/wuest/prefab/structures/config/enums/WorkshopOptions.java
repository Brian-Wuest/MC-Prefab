package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class WorkshopOptions extends BaseOption {
    public static WorkshopOptions Default = new WorkshopOptions(
            "item.prefab.workshop",
            "assets/prefab/structures/workshop.zip",
            "textures/gui/workshop_topdown.png",
            true,
            true);

    protected WorkshopOptions(String translationString,
                              String assetLocation,
                              String pictureLocation,
                              boolean hasBedColor,
                              boolean hasGlassColor) {
        super(
                translationString,
                assetLocation,
                pictureLocation,
                hasBedColor,
                hasGlassColor);
    }
}