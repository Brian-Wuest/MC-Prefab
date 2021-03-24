package com.wuest.prefab.Structures.Items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * @author WuestMan
 */
public class ItemStructurePart extends StructureItem {
	public ItemStructurePart() {
		super(new Item.Properties()
				.tab(ItemGroup.TAB_MISC)
				.durability(10));
	}
}
