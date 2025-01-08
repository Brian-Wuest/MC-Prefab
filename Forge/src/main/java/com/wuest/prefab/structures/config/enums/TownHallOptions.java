package com.wuest.prefab.structures.config.enums;

public class TownHallOptions extends BaseOption {
    public static TownHallOptions Default = new TownHallOptions(
            "item.prefab.town_hall",
            "assets/prefab/structures/town_hall.gz",
            "textures/gui/town_hall.png",
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
