package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.EnumFacing;

public class NetherGateOptions extends BaseOption {
    public static NetherGateOptions AncientSkull = new NetherGateOptions(
            "item.prefab.item_nether_gate_skull",
            "assets/prefab/structures/nethergate.zip",
            "textures/gui/nether_gate_top_down.png",
            164,
            108,
            EnumFacing.SOUTH,
            13,
            26,
            15,
            1,
            7,
            -2);

    protected NetherGateOptions(String translationString,
                                String assetLocation,
                                String pictureLocation,
                                int imageWidth,
                                int imageHeight,
                                EnumFacing direction,
                                int height,
                                int width,
                                int length,
                                int offsetParallelToPlayer,
                                int offsetToLeftOfPlayer,
                                int heightOffset) {
        super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight, direction, height, width, length, offsetParallelToPlayer, offsetToLeftOfPlayer, heightOffset);
    }
}
