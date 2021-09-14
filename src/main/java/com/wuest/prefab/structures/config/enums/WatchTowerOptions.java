package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class WatchTowerOptions extends BaseOption {
    public static WatchTowerOptions Default = new WatchTowerOptions(
            "item.prefab.watch_tower",
            "assets/prefab/structures/watch_tower.zip",
            "textures/gui/watch_tower_topdown.png",
            true,
            false);

    protected WatchTowerOptions(String translationString,
                                String assetLocation,
                                String pictureLocation,
                                boolean hasBedColor,
                                boolean hasGlassColor) {
        super(
                translationString,
                assetLocation,
                pictureLocation,
                hasBedColor,
                hasGlassColor);
    }
}
