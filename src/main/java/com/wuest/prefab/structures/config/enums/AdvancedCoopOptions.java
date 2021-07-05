package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class AdvancedCoopOptions extends BaseOption {
    public static AdvancedCoopOptions Default = new AdvancedCoopOptions(
            "item.prefab.advanced.chicken.coop",
            "assets/prefab/structures/advancedcoop.zip",
            "textures/gui/advanced_chicken_coop_topdown.png",
            156,
            121,
            Direction.SOUTH,
            10,
            11,
            11,
            1,
            5,
            0,
            false,
            false);

    protected AdvancedCoopOptions(String translationString,
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