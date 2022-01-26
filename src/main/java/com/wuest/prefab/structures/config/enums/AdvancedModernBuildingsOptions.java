package com.wuest.prefab.structures.config.enums;

public class AdvancedModernBuildingsOptions extends BaseOption {

    public static AdvancedModernBuildingsOptions TreeHouse = new AdvancedModernBuildingsOptions(
            "prefab.gui.item_modern_tree_house",
            "assets/prefab/structures/modern_tree_house.zip",
            "textures/gui/modern_tree_house_topdown.png",
            true,
            true);

    protected AdvancedModernBuildingsOptions(String translationString,
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
