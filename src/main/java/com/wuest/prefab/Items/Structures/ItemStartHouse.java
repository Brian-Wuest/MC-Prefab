package com.wuest.prefab.Items.Structures;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Config.Structures.HouseConfiguration;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemStartHouse extends StructureItem
{
	public ItemStartHouse(String name)
	{
		super(name, ModRegistry.GuiStartHouseChooser);
	}
}