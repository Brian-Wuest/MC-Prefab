package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.EnumFacing;

public class AquaBaseOptions extends BaseOption {
    public static AquaBaseOptions Default = new AquaBaseOptions(
            "item.prefab.aqua_base",
            "assets/prefab/structures/aqua_base.zip",
            "textures/gui/aqua_base_topdown.png",
            false,
            false);

    protected AquaBaseOptions(String translationString,
                              String assetLocation,
                              String pictureLocation,
                              boolean hasBedColor,
                              boolean hasGlassColor) {
        super(translationString, assetLocation, pictureLocation, hasBedColor, hasGlassColor);
    }
}
