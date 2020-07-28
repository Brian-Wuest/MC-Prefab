package com.wuest.prefab.Structures.Config.Enums;

public class DefenseBunkerOptions extends BaseOption {
	public static DefenseBunkerOptions Default = new DefenseBunkerOptions("item.prefab.defense.bunker", "assets/prefab/structures/defense_bunker.zip", "textures/gui/defense_bunker_topdown.png", 153, 175);

	protected DefenseBunkerOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
