package com.wuest.prefab.Structures.Config.Enums;

public class GrassyPlainOptions extends BaseOption {
	public static GrassyPlainOptions Default = new GrassyPlainOptions("item.prefab.grassy_plain", "assets/prefab/structures/grassy_plain.zip", "textures/gui/grassy_plain_topdown.png", 160, 160);

	protected GrassyPlainOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
