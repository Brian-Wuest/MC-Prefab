package com.wuest.prefab.structures.config.enums;

public class ChickenCoopOptions extends BaseOption {
    public static ChickenCoopOptions Default = new ChickenCoopOptions(
            "item.prefab.item_chicken_coop",
            "assets/prefab/structures/chickencoop.zip",
            "textures/gui/chicken_coop_topdown.png",
            false,
            false);

    protected ChickenCoopOptions(String translationString,
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
