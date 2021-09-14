package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class GrassyPlainOptions extends BaseOption {
    public static GrassyPlainOptions Default = new GrassyPlainOptions(
            "item.prefab.grassy_plain",
            "assets/prefab/structures/grassy_plain.zip",
            "textures/gui/grassy_plain_topdown.png",
            false,
            false);

    protected GrassyPlainOptions(String translationString,
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
