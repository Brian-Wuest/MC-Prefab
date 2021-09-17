package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class AdvancedWarehouseOptions extends BaseOption{
    public static AdvancedWarehouseOptions Default = new AdvancedWarehouseOptions(
            "item.prefab.item_advanced_warehouse",
            "assets/prefab/structures/advanced_warehouse.zip",
            "textures/gui/advanced_warehouse_top_down.png",
            false,
            true);

    protected AdvancedWarehouseOptions(String translationString,
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
