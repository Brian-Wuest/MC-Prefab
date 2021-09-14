package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class NetherGateOptions extends BaseOption {
    public static NetherGateOptions AncientSkull = new NetherGateOptions(
            "item.prefab.item_nether_gate_skull",
            "assets/prefab/structures/nethergate.zip",
            "textures/gui/nether_gate_top_down.png",
            false,
            false);

    public static NetherGateOptions CorruptedTree = new NetherGateOptions(
            "item.prefab.item_nether_gate_tree",
            "assets/prefab/structures/nethergate_tree.zip",
            "textures/gui/nether_gate_tree_top_down.png",
            false,
            false);

    protected NetherGateOptions(String translationString,
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
