package com.wuest.prefab.Structures.Config.Enums;

public class WatchTowerOptions extends BaseOption {
	public static WatchTowerOptions Default = new WatchTowerOptions("item.prefab.watch_tower", "assets/prefab/structures/watch_tower.zip", "textures/gui/watch_tower_topdown.png", 133, 176);

	protected WatchTowerOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
