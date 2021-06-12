package com.wuest.prefab.Structures.Config.Enums;

import net.minecraft.util.Direction;

public class NetherGateOptions extends BaseOption {
    public static NetherGateOptions AncientSkull = new NetherGateOptions(
            "item.prefab.item_nether_gate_skull",
            "assets/prefab/structures/nethergate.zip",
            "textures/gui/nether_gate_top_down.png",
            164,
            108,
            Direction.SOUTH,
            13,
            26,
            15,
            1,
            7,
            -2);

    public static NetherGateOptions CorruptedTree = new NetherGateOptions(
            "item.prefab.item_nether_gate_tree",
            "assets/prefab/structures/nethergate_tree.zip",
            "textures/gui/nether_gate_tree_top_down.png",
            164,
            126,
            Direction.SOUTH,
            20,
            16,
            16,
            1,
            9,
            -7);

    protected NetherGateOptions(String translationString,
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
