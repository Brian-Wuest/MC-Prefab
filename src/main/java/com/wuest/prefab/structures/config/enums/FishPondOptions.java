package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class FishPondOptions extends BaseOption{

    public static FishPondOptions Default = new FishPondOptions(
            "item.prefab.item_fish_pond",
            "assets/prefab/structures/fishpond.zip",
            "textures/gui/fish_pond_top_down.png",
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
