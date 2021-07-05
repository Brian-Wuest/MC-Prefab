package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class AdvancedHorseStableOptions extends BaseOption {
    public static AdvancedHorseStableOptions Default = new AdvancedHorseStableOptions(
            "item.prefab.advanced.horse.stable",
            "assets/prefab/structures/advanced_horse_stable.zip",
            "textures/gui/advanced_horse_stable_topdown.png",
            128,
            158,
            Direction.SOUTH,
            8,
            17,
            34,
            1,
            8,
            0,
            false,
            false);

    protected AdvancedHorseStableOptions(String translationString,
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