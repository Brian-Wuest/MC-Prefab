package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class MachineryTowerOptions extends BaseOption {
    public static MachineryTowerOptions Default = new MachineryTowerOptions(
            "item.prefab.machinery.tower",
            "assets/prefab/structures/machinery_tower.zip",
            "textures/gui/machinery_tower_topdown.png",
            Direction.SOUTH,
            12,
            16,
            16,
            1,
            8,
            0,
            false,
            false);

    protected MachineryTowerOptions(String translationString,
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
