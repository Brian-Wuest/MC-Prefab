package com.wuest.prefab.structures.config.enums;

public class ProduceFarmOptions extends BaseOption {
    public static ProduceFarmOptions Default = new ProduceFarmOptions(
            "item.prefab.item_produce_farm",
            "assets/prefab/structures/producefarm.zip",
            "textures/gui/produce_farm_top_down.png",
            false,
            true);

    protected ProduceFarmOptions(String translationString,
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
