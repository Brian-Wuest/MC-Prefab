package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Predefined.StructureWarehouse;
import net.minecraft.item.ItemUseContext;

/**
 * This class is used to build the warehouse structure.
 *
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemWareHouse extends StructureItem {
	public ItemWareHouse(String name) {
		super(name);
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureWarehouse.ScanStructure(
				context.getWorld(),
				context.getPos(),
				context.getPlayer().getHorizontalFacing(),
				false);
	}
}