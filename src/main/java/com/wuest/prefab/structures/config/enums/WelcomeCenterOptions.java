package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class WelcomeCenterOptions extends BaseOption {
	public static WelcomeCenterOptions Default = new WelcomeCenterOptions(
			"item.prefab.welcome_center",
			"assets/prefab/structures/welcome_center.zip",
			"textures/gui/welcome_center_topdown.png",
			168,
			121,
			Direction.SOUTH,
			24,
			29,
			48,
			1,
			3,
			-5,
			true,
			false);

	protected WelcomeCenterOptions(String translationString,
								   String assetLocation,
								   String pictureLocation,
								   int imageWidth,
								   int imageHeight,
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
				imageWidth,
				imageHeight,
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
