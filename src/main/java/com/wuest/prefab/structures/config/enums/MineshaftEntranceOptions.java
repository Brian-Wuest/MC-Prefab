package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class MineshaftEntranceOptions extends BaseOption {
    public static MineshaftEntranceOptions Default = new MineshaftEntranceOptions(
            "item.prefab.mineshaft.entrance",
            "assets/prefab/structures/mineshaft_entrance.zip",
            "textures/gui/mineshaft_entrance.png",
            true,
            false);

    protected MineshaftEntranceOptions(String translationString,
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
