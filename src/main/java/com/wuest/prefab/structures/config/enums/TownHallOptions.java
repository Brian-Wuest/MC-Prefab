package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class TownHallOptions extends BaseOption {
    public static TownHallOptions Default = new TownHallOptions(
            "item.prefab.town_hall",
            "assets/prefab/structures/town_hall.zip",
            "textures/gui/town_hall_topdown.png",
            false,
            false);

    protected TownHallOptions(String translationString,
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
