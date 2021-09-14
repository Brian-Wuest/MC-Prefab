package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class SkiLodgeOptions extends BaseOption {
    public static SkiLodgeOptions Default = new SkiLodgeOptions(
            "item.prefab.ski_lodge",
            "assets/prefab/structures/ski_lodge.zip",
            "textures/gui/ski_lodge_topdown.png",
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
