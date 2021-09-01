package com.wuest.prefab.structures.config.enums;

public class FishPondOptions extends BaseOption{

    public static FishPondOptions Default = new FishPondOptions(
            "item.prefab.item_fish_pond",
            "assets/prefab/structures/fishpond.zip",
            "textures/gui/fish_pond_top_down.png",
            false,
            false);

    protected FishPondOptions(String translationString,
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
