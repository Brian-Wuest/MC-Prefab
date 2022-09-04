package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class EnderGatewayOptions extends BaseOption {
    public static EnderGatewayOptions Default = new EnderGatewayOptions(
            "item.prefab.ender_gateway",
            "assets/prefab/structures/ender_gateway.zip",
            "textures/gui/ender_gateway.png",
            false,
            false);

    protected EnderGatewayOptions(String translationString,
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
