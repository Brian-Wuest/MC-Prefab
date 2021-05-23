package com.wuest.prefab.Structures.Config.Enums;

import net.minecraft.util.Direction;

public class AdvancedAquaBaseOptions extends BaseOption {
    public static AdvancedAquaBaseOptions Default = new AdvancedAquaBaseOptions(
            "item.prefab.advanced_aqua_base",
            "assets/prefab/structures/advanced_aqua_base.zip",
            "textures/gui/advanced_aqua_base_topdown.png",
            117,
            180,
            Direction.SOUTH,
            27,
            25,
            38,
            1,
            12,
            0);

    protected AdvancedAquaBaseOptions(String translationString,
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
                                      int heightOffset) {
        super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight, direction, height, width, length, offsetParallelToPlayer, offsetToLeftOfPlayer, heightOffset);
    }
}
