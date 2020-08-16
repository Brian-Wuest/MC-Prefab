package com.wuest.prefab.Structures.Config.Enums;

public class WelcomeCenterOptions extends BaseOption {
	public static WelcomeCenterOptions Default = new WelcomeCenterOptions("item.prefab.welcome_center", "assets/prefab/structures/welcome_center.zip", "textures/gui/welcome_center_topdown.png", 168, 121);

	protected WelcomeCenterOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
