package com.wuest.prefab.structures.config.enums;

public class AquaBaseImprovedOptions extends BaseOption {
    public static AquaBaseImprovedOptions Default = new AquaBaseImprovedOptions(
            "item.prefab.aqua_base_improved",
            "assets/prefab/structures/aqua_base_improved.gz",
            "textures/gui/aqua_base_improved.png",
            false,
            false);

    protected AquaBaseImprovedOptions(String translationString,
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
