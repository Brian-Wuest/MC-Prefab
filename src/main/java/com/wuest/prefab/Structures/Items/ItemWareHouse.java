package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Config.WareHouseConfiguration;

/**
 * This class is used to build the warehouse structure.
 * @author WuestMan
 *
 */
public class ItemWareHouse extends StructureItem
{
	public ItemWareHouse(String name)
	{
		super(name, ModRegistry.GuiWareHouse);
	}
}