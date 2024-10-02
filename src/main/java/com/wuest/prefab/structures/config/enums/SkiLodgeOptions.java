package com.wuest.prefab.structures.config.enums;

public class SkiLodgeOptions extends BaseOption {
    public static SkiLodgeOptions Default = new SkiLodgeOptions(
            "item.prefab.ski_lodge",
            "assets/prefab/structures/ski_lodge.gz",
            "textures/gui/ski_lodge.png",
            false,
            false);

    protected SkiLodgeOptions(String translationString,
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
