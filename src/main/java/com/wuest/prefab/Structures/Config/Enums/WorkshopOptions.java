package com.wuest.prefab.Structures.Config.Enums;

public class WorkshopOptions extends BaseOption {
    public static WorkshopOptions Default = new WorkshopOptions("item.prefab.workshop", "assets/prefab/structures/workshop.zip", "textures/gui/workshop_topdown.png", 174, 131);

    protected WorkshopOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
        super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
    }
}