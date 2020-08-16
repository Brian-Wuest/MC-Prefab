package com.wuest.prefab.Structures.Config.Enums;

public class GreenHouseOptions extends BaseOption {
	public static GreenHouseOptions Default = new GreenHouseOptions("item.prefab.green_house", "assets/prefab/structures/green_house.zip", "textures/gui/green_house_topdown.png", 173, 104);

	protected GreenHouseOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
