package com.wuest.prefab.Structures.Config.Enums;

public class AdvancedHorseStableOptions extends BaseOption {
	public static AdvancedHorseStableOptions Default = new AdvancedHorseStableOptions("item.prefab.advanced.horse.stable", "assets/prefab/structures/advanced_horse_stable.zip", "textures/gui/advanced_horse_stable_topdown.png", 128, 158);

	protected AdvancedHorseStableOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}