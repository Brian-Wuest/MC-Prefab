package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class JailOptions extends BaseOption {
    public static JailOptions Default = new JailOptions(
            "item.prefab.jail",
            "assets/prefab/structures/jail.zip",
            "textures/gui/jail_topdown.png",
            Direction.SOUTH,
            14,
            28,
            33,
            1,
            25,
            -3,
            false,
            false);

    protected JailOptions(String translationString,
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
