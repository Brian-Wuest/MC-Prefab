package com.wuest.prefab.Structures.Config.Enums;

public class MineshaftEntranceOptions extends BaseOption {
	public static MineshaftEntranceOptions Default = new MineshaftEntranceOptions("item.prefab.mineshaft.entrance", "assets/prefab/structures/mineshaft_entrance.zip", "textures/gui/mineshaft_entrance_topdown.png", 135, 159);

	protected MineshaftEntranceOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}

}
