package com.wuest.prefab.Structures.Config.Enums;

public class NetherGateOptions extends BaseOption {
	public static NetherGateOptions AncientSkull = new NetherGateOptions("item.prefab.item_nether_gate_skull", "assets/prefab/structures/nethergate.zip", "textures/gui/nether_gate_top_down.png", 164, 108);
	public static NetherGateOptions CorruptedTree = new NetherGateOptions("item.prefab.item_nether_gate_tree", "assets/prefab/structures/nethergate_tree.zip", "textures/gui/nether_gate_tree_top_down.png", 164, 108);

	protected NetherGateOptions(String translationString, String assetLocation, String pictureLocation, int imageWidth, int imageHeight) {
		super(translationString, assetLocation, pictureLocation, imageWidth, imageHeight);
	}
}
