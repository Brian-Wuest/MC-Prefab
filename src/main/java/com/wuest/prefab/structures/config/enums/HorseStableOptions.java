package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class HorseStableOptions extends BaseOption {
    public static HorseStableOptions Default = new HorseStableOptions(
            "item.prefab.item_horse_stable",
            "assets/prefab/structures/horsestable.zip",
            "textures/gui/horse_stable_top_down.png",
            Direction.SOUTH,
            11,
            8,
            10,
            1,
            4,
            -4,
            false,
            false);

    protected HorseStableOptions(String translationString,
                                 String assetLocation,
                                 String pictureLocation,
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
