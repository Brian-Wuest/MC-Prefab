package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Predefined.StructureWarehouse;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemAdvancedWareHouse extends ItemWareHouse {
	public ItemAdvancedWareHouse( ) {
		super();
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureWarehouse.ScanStructure(
				context.getLevel(),
				context.getClickedPos(),
				context.getPlayer().getDirection(),
				true);
	}
}
