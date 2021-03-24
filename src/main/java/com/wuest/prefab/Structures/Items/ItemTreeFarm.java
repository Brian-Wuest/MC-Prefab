package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Predefined.StructureTreeFarm;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemTreeFarm extends StructureItem {
	public ItemTreeFarm( ) {
		super();
	}


	@Override
	public void scanningMode(ItemUseContext context) {
		StructureTreeFarm.ScanStructure(
				context.getLevel(),
				context.getClickedPos(),
				context.getPlayer().getDirection());
	}
}
