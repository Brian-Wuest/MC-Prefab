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
	public ItemWareHouse( ) {
		super();
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureWarehouse.ScanStructure(
				context.getLevel(),
				context.getClickedPos(),
				context.getPlayer().getDirection(),
				false);
	}
}