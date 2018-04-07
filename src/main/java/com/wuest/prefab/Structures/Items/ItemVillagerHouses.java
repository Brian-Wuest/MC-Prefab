package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.ModRegistry;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemVillagerHouses extends StructureItem
{
	public ItemVillagerHouses(String name)
	{
		super(name, ModRegistry.GuiVillagerHouses);

		this.setMaxDamage(10);
		this.setMaxStackSize(1);
	}
}
