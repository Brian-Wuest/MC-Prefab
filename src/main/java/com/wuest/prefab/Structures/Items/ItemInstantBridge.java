package com.wuest.prefab.Structures.Items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * This is the instant bridge item.
 *
 * @author WuestMan
 */
public class ItemInstantBridge extends StructureItem {
	public ItemInstantBridge( ) {
		super(new Item.Properties()
				.tab(ItemGroup.TAB_MISC)
				.durability(10));
	}
}
