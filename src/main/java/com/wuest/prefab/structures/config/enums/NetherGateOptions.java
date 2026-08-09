package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class NetherGateOptions extends BaseOption {
    public static NetherGateOptions AncientSkull = new NetherGateOptions(
            "item.prefab.item_nether_gate_skull",
            "assets/prefab/structures/nethergate.gz",
            "textures/gui/nether_gate_top_down.png",
            false,
            false);

    public static NetherGateOptions CorruptedTree = new NetherGateOptions(
            "item.prefab.item_nether_gate_tree",
            "assets/prefab/structures/nethergate_tree.gz",
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
