package com.wuest.prefab.structures.config.enums;

public class WindMillOptions extends BaseOption {
    public static WindMillOptions Default = new WindMillOptions(
            "item.prefab.wind_mill",
            "assets/prefab/structures/wind_mill.gz",
            "textures/gui/wind_mill.png",
            false,
            false);

    protected WindMillOptions(String translationString,
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
