package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class WelcomeCenterOptions extends BaseOption {
	public static WelcomeCenterOptions Default = new WelcomeCenterOptions(
			"item.prefab.welcome_center",
			"assets/prefab/structures/welcome_center.zip",
			"textures/gui/welcome_center_topdown.png",
			true,
			false);

	protected WelcomeCenterOptions(String translationString,
								   String assetLocation,
								   String pictureLocation,
								   boolean hasBedColor,
								   boolean hasGlassColor) {
		super(
				translationString,
				assetLocation,
				pictureLocation,
				hasBedColor,
				hasGlassColor);
	}
}
