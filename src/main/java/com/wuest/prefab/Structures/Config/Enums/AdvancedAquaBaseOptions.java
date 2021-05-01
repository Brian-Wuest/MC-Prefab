package com.wuest.prefab.Structures.Config.Enums;

public class AdvancedAquaBaseOptions extends BaseOption {
	public static AdvancedAquaBaseOptions Default = new AdvancedAquaBaseOptions("item.prefab.advanced_aqua_base", "assets/prefab/structures/advanced_aqua_base.zip", "textures/gui/advanced_aqua_base_topdown.png", 119, 160);

	protected AdvancedAquaBaseOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
