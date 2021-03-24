package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Predefined.StructureFishPond;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemFishPond extends StructureItem {
	public ItemFishPond( ) {
		super();
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureFishPond.ScanStructure(
				context.getLevel(),
				context.getClickedPos(),
				context.getPlayer().getDirection());
	}
}
