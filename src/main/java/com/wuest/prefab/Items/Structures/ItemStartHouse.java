package com.wuest.prefab.Items.Structures;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Config.HouseConfiguration;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemStartHouse extends StructureItem
{
	private HouseConfiguration currentConfiguration = null;

	public ItemStartHouse(String name)
	{
		super(name, ModRegistry.GuiStartHouseChooser);
	}
}