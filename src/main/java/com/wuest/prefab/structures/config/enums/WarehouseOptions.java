package com.wuest.prefab.structures.config.enums;

public class WarehouseOptions extends BaseOption {
    public static WarehouseOptions Default = new WarehouseOptions(
            "item.prefab.item_warehouse",
            "assets/prefab/structures/warehouse.gz",
            "textures/gui/warehouse.png",
            false,
            true);

    protected WarehouseOptions(String translationString,
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
