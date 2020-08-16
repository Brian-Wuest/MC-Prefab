package com.wuest.prefab.Structures.Config.Enums;

public class SaloonOptions extends BaseOption {
	public static SaloonOptions Default = new SaloonOptions("item.prefab.saloon", "assets/prefab/structures/saloon.zip", "textures/gui/saloon_topdown.png", 130, 170);

	protected SaloonOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
