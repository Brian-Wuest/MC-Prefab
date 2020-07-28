package com.wuest.prefab.Structures.Config.Enums;

public class MachineryTowerOptions extends BaseOption {
	public static MachineryTowerOptions Default = new MachineryTowerOptions("item.prefab.machinery.tower", "assets/prefab/structures/machinery_tower.zip", "textures/gui/machinery_tower_topdown.png", 153, 175);

	protected MachineryTowerOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
