package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class NetherGateOptions extends BaseOption {
    public static NetherGateOptions AncientSkull = new NetherGateOptions(
            "item.prefab.item_nether_gate_skull",
            "assets/prefab/structures/nethergate.zip",
            "textures/gui/nether_gate_top_down.png",
            Direction.SOUTH,
            13,
            26,
            15,
            1,
            7,
            -2,
            false,
            false);

    public static NetherGateOptions CorruptedTree = new NetherGateOptions(
            "item.prefab.item_nether_gate_tree",
            "assets/prefab/structures/nethergate_tree.zip",
            "textures/gui/nether_gate_tree_top_down.png",
            Direction.SOUTH,
            20,
            16,
            16,
            1,
            9,
            -7,
            false,
            false);

    protected NetherGateOptions(String translationString,
                                String assetLocation,
                                String pictureLocation,
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
