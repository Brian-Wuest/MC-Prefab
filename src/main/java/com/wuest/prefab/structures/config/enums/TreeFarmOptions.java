package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class TreeFarmOptions extends BaseOption {
    public static TreeFarmOptions Default = new TreeFarmOptions(
            "item.prefab.item_tree_farm",
            "assets/prefab/structures/treefarm.zip",
            "textures/gui/tree_farm_top_down.png",
            Direction.SOUTH,
            7,
            38,
            38,
            1,
            18,
            0,
            false,
            false);

    protected TreeFarmOptions(String translationString,
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
