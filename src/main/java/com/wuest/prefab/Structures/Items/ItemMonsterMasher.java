package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Predefined.StructureMonsterMasher;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemMonsterMasher extends StructureItem {
	public ItemMonsterMasher( ) {
		super();
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureMonsterMasher.ScanStructure(
				context.getWorld(),
				context.getPos(),
				context.getPlayer().getHorizontalFacing());
	}
}
