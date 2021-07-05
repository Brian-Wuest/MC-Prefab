package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class FishPondOptions extends BaseOption{

    public static FishPondOptions Default = new FishPondOptions(
            "item.prefab.item_fish_pond",
            "assets/prefab/structures/fishpond.zip",
            "textures/gui/fish_pond_top_down.png",
            164,
            160,
            Direction.SOUTH,
            25,
            32,
            32,
            1,
            16,
            -3,
            false,
            false);

    protected FishPondOptions(String translationString,
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
