package com.wuest.prefab.Structures.Config.Enums;

public class JailOptions extends BaseOption {
	public static JailOptions Default = new JailOptions("item.prefab.jail", "assets/prefab/structures/jail.zip", "textures/gui/jail_topdown.png", 175, 131);

	protected JailOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
