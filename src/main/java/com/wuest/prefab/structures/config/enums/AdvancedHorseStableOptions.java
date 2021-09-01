package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.EnumFacing;

public class AdvancedHorseStableOptions extends BaseOption {
    public static AdvancedHorseStableOptions Default = new AdvancedHorseStableOptions(
            "item.prefab.advanced.horse.stable",
            "assets/prefab/structures/advanced_horse_stable.zip",
            "textures/gui/advanced_horse_stable_topdown.png",
            false,
            false);

    protected AdvancedHorseStableOptions(String translationString,
                                         String assetLocation,
                                         String pictureLocation,
                                         boolean hasBedColor,
                                         boolean hasGlassColor) {
        super(translationString, assetLocation, pictureLocation, hasBedColor, hasGlassColor);
    }
}