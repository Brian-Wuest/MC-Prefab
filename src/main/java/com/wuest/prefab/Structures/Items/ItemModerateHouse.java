package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Config.ModerateHouseConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureModerateHouse;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
public class ItemModerateHouse extends StructureItem {
	/**
	 * Initializes a new instance of the {@link ItemModerateHouse} class.
	 *
	 * @param name The name to register this item as.
	 */
	public ItemModerateHouse(String name) {
		super(name);
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureModerateHouse.ScanStructure(
				context.getWorld(),
				context.getPos(),
				context.getPlayer().getHorizontalFacing(),
				ModerateHouseConfiguration.HouseStyle.ACACIA_HOME);
	}
}
