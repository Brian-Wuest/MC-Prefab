package com.wuest.prefab.structures.config.enums;

public class WarehouseImprovedOptions extends BaseOption{
    public static WarehouseImprovedOptions Default = new WarehouseImprovedOptions(
            "item.prefab.item_warehouse_improved",
            "assets/prefab/structures/warehouse_improved.gz",
            "textures/gui/warehouse_improved.png",
            false,
            true);

    protected WarehouseImprovedOptions(String translationString,
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
