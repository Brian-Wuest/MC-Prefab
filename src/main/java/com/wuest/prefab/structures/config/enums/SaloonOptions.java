package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class SaloonOptions extends BaseOption {
    public static SaloonOptions Default = new SaloonOptions(
            "item.prefab.saloon",
            "assets/prefab/structures/saloon.zip",
            "textures/gui/saloon_topdown.png",
            170,
            130,
            Direction.SOUTH,
            14,
            28,
            16,
            1,
            10,
            -1,
            false,
            false);

    protected SaloonOptions(String translationString,
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
