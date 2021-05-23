package com.wuest.prefab.Structures.Config.Enums;

import net.minecraft.util.Direction;

public class MachineryTowerOptions extends BaseOption {
    public static MachineryTowerOptions Default = new MachineryTowerOptions(
            "item.prefab.machinery.tower",
            "assets/prefab/structures/machinery_tower.zip",
            "textures/gui/machinery_tower_topdown.png",
            175,
            153,
            Direction.SOUTH,
            12,
            16,
            16,
            1,
            8,
            0);

    protected MachineryTowerOptions(String translationString,
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
                                    int heightOffset) {
        super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight, direction, height, width, length, offsetParallelToPlayer, offsetToLeftOfPlayer, heightOffset);
    }
}
