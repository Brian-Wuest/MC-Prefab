package com.wuest.prefab.Structures.Config.Enums;

public class WindMillOptions extends BaseOption {
	public static WindMillOptions Default = new WindMillOptions("item.prefab.wind_mill", "assets/prefab/structures/wind_mill.zip", "textures/gui/wind_mill_topdown.png", 102, 176);

	protected WindMillOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
