package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class ChickenCoopOptions extends BaseOption {
    public static ChickenCoopOptions Default = new ChickenCoopOptions(
            "item.prefab.item_chicken_coop",
            "assets/prefab/structures/chickencoop.zip",
            "textures/gui/chicken_coop_topdown.png",
            Direction.SOUTH,
            7,
            12,
            5,
            1,
            9,
            0,
            false,
            false);

    protected ChickenCoopOptions(String translationString,
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
