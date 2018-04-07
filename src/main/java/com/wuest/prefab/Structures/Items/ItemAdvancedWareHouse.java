package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.ModRegistry;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemAdvancedWareHouse extends ItemWareHouse
{
	public ItemAdvancedWareHouse(String name)
	{
		super(name);
		this.guiId = ModRegistry.GuiAdvancedWareHouse;
	}
}
