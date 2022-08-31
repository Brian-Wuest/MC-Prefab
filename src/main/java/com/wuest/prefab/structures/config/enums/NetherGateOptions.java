package com.wuest.prefab.structures.config.enums;

public class NetherGateOptions extends BaseOption {
    public static NetherGateOptions AncientSkull = new NetherGateOptions(
            "item.prefab.item_nether_gate_skull",
            "assets/prefab/structures/nether_gate.zip",
            "textures/gui/nether_gate.png",
            false,
            false);

    public static NetherGateOptions CorruptedTree = new NetherGateOptions(
            "item.prefab.item_nether_gate_tree",
            "assets/prefab/structures/nether_gate_tree.zip",
            "textures/gui/nether_gate_tree.png",
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
