package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class AdvancedAquaBaseOptions extends BaseOption {
    public static AdvancedAquaBaseOptions Default = new AdvancedAquaBaseOptions(
            "item.prefab.advanced_aqua_base",
            "assets/prefab/structures/advanced_aqua_base.zip",
            "textures/gui/advanced_aqua_base_topdown.png",
            false,
            false);

    protected AdvancedAquaBaseOptions(String translationString,
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
