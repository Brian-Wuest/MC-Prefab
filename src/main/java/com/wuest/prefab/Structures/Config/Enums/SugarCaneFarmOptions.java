package com.wuest.prefab.Structures.Config.Enums;

public class SugarCaneFarmOptions extends BaseOption {
    public static SugarCaneFarmOptions Default = new SugarCaneFarmOptions("item.prefab.sugar_cane_farm", "assets/prefab/structures/sugar_cane_farm.zip", "textures/gui/sugar_cane_farm_topdown.png", 180, 109);

    protected SugarCaneFarmOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
        super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
    }
}
