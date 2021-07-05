package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class ProduceFarmOptions extends BaseOption {
    public static ProduceFarmOptions Default = new ProduceFarmOptions(
            "item.prefab.item_produce_farm",
            "assets/prefab/structures/producefarm.zip",
            "textures/gui/produce_farm_top_down.png",
            164,
            160,
            Direction.SOUTH,
            9,
            32,
            32,
            1,
            28,
            -1,
            false,
            true);

    protected ProduceFarmOptions(String translationString,
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
