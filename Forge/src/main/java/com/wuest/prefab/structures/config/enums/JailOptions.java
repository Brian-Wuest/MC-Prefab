package com.wuest.prefab.structures.config.enums;

public class JailOptions extends BaseOption {
    public static JailOptions Default = new JailOptions(
            "item.prefab.jail",
            "assets/prefab/structures/jail.gz",
            "textures/gui/jail.png",
            false,
            false);

    protected JailOptions(String translationString,
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
