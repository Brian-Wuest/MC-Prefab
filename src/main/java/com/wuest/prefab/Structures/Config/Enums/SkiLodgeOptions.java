package com.wuest.prefab.Structures.Config.Enums;

public class SkiLodgeOptions extends BaseOption {
	public static SkiLodgeOptions Default = new SkiLodgeOptions("item.prefab.ski_lodge", "assets/prefab/structures/ski_lodge.zip", "textures/gui/ski_lodge_topdown.png", 137, 180);

	protected SkiLodgeOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
