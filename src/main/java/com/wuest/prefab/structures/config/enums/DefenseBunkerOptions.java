package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class DefenseBunkerOptions extends BaseOption {
    public static DefenseBunkerOptions Default = new DefenseBunkerOptions(
            "item.prefab.defense.bunker",
            "assets/prefab/structures/defense_bunker.zip",
            "textures/gui/defense_bunker.png",
            false,
            false);

    protected DefenseBunkerOptions(String translationString,
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
