package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Config.VillagerHouseConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureVillagerHouses;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemVillagerHouses extends StructureItem {
	public ItemVillagerHouses() {
		super(new Item.Properties()
				.group(ItemGroup.MISC)
				.maxDamage(10));
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureVillagerHouses.ScanStructure(
				context.getWorld(),
				context.getPos(),
				context.getPlayer().getHorizontalFacing(),
				VillagerHouseConfiguration.HouseStyle.BLACKSMITH);
	}
}
