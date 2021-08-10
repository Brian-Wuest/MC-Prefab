package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class WatchTowerOptions extends BaseOption {
    public static WatchTowerOptions Default = new WatchTowerOptions(
            "item.prefab.watch_tower",
            "assets/prefab/structures/watch_tower.zip",
            "textures/gui/watch_tower_topdown.png",
            Direction.SOUTH,
            16,
            9,
            9,
            1,
            4,
            0,
            true,
            false);

    protected WatchTowerOptions(String translationString,
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
