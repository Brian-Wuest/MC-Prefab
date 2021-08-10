package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class MagicTempleOptions extends BaseOption {
    public static MagicTempleOptions Default = new MagicTempleOptions(
            "item.prefab.magic_temple",
            "assets/prefab/structures/magic_temple.zip",
            "textures/gui/magic_temple_topdown.png",
            Direction.SOUTH,
            13,
            12,
            13,
            1,
            6,
            0,
            false,
            false);

    protected MagicTempleOptions(String translationString,
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
