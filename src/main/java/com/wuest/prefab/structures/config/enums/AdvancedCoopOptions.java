package com.wuest.prefab.structures.config.enums;

public class AdvancedCoopOptions extends BaseOption {
    public static AdvancedCoopOptions Default = new AdvancedCoopOptions(
            "item.prefab.advanced.chicken.coop",
            "assets/prefab/structures/advancedcoop.zip",
            "textures/gui/advanced_chicken_coop_topdown.png",
            false,
            false);

    protected AdvancedCoopOptions(String translationString,
                                  String assetLocation,
                                  String pictureLocation,
                                  boolean hasBedColor,
                                  boolean hasGlassColor) {
        super(translationString, assetLocation, pictureLocation, hasBedColor, hasGlassColor);
    }
}