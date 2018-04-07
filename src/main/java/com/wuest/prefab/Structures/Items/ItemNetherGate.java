package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.ModRegistry;

/**
 * 
 * @author WuestMan
 * This is the item used to generate the Nether Gate structure.
 */
public class ItemNetherGate extends StructureItem
{
	public ItemNetherGate(String name)
	{
		super(name, ModRegistry.GuiNetherGate);
	}
}
