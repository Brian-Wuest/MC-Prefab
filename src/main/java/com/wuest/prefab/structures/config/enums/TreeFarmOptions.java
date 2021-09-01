package com.wuest.prefab.structures.config.enums;

public class TreeFarmOptions extends BaseOption {
    public static TreeFarmOptions Default = new TreeFarmOptions(
            "item.prefab.item_tree_farm",
            "assets/prefab/structures/treefarm.zip",
            "textures/gui/tree_farm_top_down.png",
            false,
            false);

    protected TreeFarmOptions(String translationString,
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
