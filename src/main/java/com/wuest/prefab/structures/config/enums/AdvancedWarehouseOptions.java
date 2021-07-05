package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class AdvancedWarehouseOptions extends BaseOption{
    public static AdvancedWarehouseOptions Default = new AdvancedWarehouseOptions(
            "item.prefab.item_advanced_warehouse",
            "assets/prefab/structures/advanced_warehouse.zip",
            "textures/gui/advanced_warehouse_top_down.png",
            164,
            160,
            Direction.SOUTH,
            16,
            16,
            16,
            1,
            7,
            -5,
            false,
            true);

    protected AdvancedWarehouseOptions(String translationString,
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
