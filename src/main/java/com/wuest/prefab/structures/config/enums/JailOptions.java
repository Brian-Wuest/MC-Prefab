package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class JailOptions extends BaseOption {
    public static JailOptions Default = new JailOptions(
            "item.prefab.jail",
            "assets/prefab/structures/jail.zip",
            "textures/gui/jail_topdown.png",
            false,
            false);

    protected JailOptions(String translationString,
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
