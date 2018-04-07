package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.ModRegistry;

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