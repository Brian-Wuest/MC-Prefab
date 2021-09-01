package com.wuest.prefab.structures.config.enums;

public class HorseStableOptions extends BaseOption {
    public static HorseStableOptions Default = new HorseStableOptions(
            "item.prefab.item_horse_stable",
            "assets/prefab/structures/horsestable.zip",
            "textures/gui/horse_stable_top_down.png",
            false,
            false);

    protected HorseStableOptions(String translationString,
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
