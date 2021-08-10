package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class AdvancedWarehouseOptions extends BaseOption{
    public static AdvancedWarehouseOptions Default = new AdvancedWarehouseOptions(
            "item.prefab.item_advanced_warehouse",
            "assets/prefab/structures/advanced_warehouse.zip",
            "textures/gui/advanced_warehouse_top_down.png",
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
