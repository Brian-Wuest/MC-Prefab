package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class WarehouseOptions extends BaseOption {
    public static WarehouseOptions Default = new WarehouseOptions(
            "item.prefab.item_warehouse",
            "assets/prefab/structures/warehouse.zip",
            "textures/gui/warehouse_top_down.png",
            Direction.SOUTH,
            16,
            16,
            16,
            1,
            7,
            -5,
            false,
            true);

    protected WarehouseOptions(String translationString,
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
