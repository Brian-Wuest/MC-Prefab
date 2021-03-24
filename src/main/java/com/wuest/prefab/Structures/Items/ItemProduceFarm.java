package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Predefined.StructureProduceFarm;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemProduceFarm extends StructureItem {
	public ItemProduceFarm( ) {
		super();
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureProduceFarm.ScanStructure(
				context.getLevel(),
				context.getClickedPos(),
				context.getPlayer().getDirection());
	}
}