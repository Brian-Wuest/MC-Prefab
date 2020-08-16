package com.wuest.prefab.Structures.Config.Enums;

public class MagicTempleOptions extends BaseOption {
	public static MagicTempleOptions Default = new MagicTempleOptions("item.prefab.magic_temple", "assets/prefab/structures/magic_temple.zip", "textures/gui/magic_temple_topdown.png", 156, 146);

	protected MagicTempleOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
