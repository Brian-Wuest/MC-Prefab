package com.wuest.prefab.Structures.Config.Enums;

public class BarnOptions extends BaseOption {
	public static BarnOptions Default = new BarnOptions("item.prefab.barn", "assets/prefab/structures/barn.zip", "textures/gui/barn_topdown.png", 164, 160);

	protected BarnOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}