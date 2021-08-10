package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class DefenseBunkerOptions extends BaseOption {
    public static DefenseBunkerOptions Default = new DefenseBunkerOptions(
            "item.prefab.defense.bunker",
            "assets/prefab/structures/defense_bunker.zip",
            "textures/gui/defense_bunker_topdown.png",
            Direction.SOUTH,
            17,
            32,
            32,
            1,
            15,
            0,
            false,
            false);

    protected DefenseBunkerOptions(String translationString,
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
