package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.EnumFacing;

public class GreenHouseOptions extends BaseOption {
    public static GreenHouseOptions Default = new GreenHouseOptions(
            "item.prefab.green_house",
            "assets/prefab/structures/green_house.zip",
            "textures/gui/green_house_topdown.png",
            false,
            false);

    protected GreenHouseOptions(String translationString,
                                String assetLocation,
                                String pictureLocation,
                                boolean hasBedColor,
                                boolean hasGlassColor) {
        super(translationString, assetLocation, pictureLocation, hasBedColor, hasGlassColor);
    }
}
