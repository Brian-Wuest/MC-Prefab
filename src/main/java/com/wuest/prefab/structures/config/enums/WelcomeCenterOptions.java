package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class WelcomeCenterOptions extends BaseOption {
	public static WelcomeCenterOptions Default = new WelcomeCenterOptions(
			"item.prefab.welcome_center",
			"assets/prefab/structures/welcome_center.zip",
			"textures/gui/welcome_center_topdown.png",
			Direction.SOUTH,
			25,
			19,
			48,
			1,
			5,
			-5,
			true,
			false);

	protected WelcomeCenterOptions(String translationString,
								   String assetLocation,
								   String pictureLocation,
								   Direction direction,
								   int height,
								   int width,
								   int length,
								   int offsetParallelToPlayer,
								   int offsetToLeftOfPlayer,
								   int heightOffset,
								   boolean hasBedColor,
								   boolean hasGlassColor) {
		super(
				translationString,
				assetLocation,
				pictureLocation,
				direction,
				height,
				width,
				length,
				offsetParallelToPlayer,
				offsetToLeftOfPlayer,
				heightOffset,
				hasBedColor,
				hasGlassColor);
	}
}
