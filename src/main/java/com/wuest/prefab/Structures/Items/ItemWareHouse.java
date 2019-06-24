package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Gui.GuiStructure;
import com.wuest.prefab.Structures.Gui.GuiWareHouse;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * This class is used to build the warehouse structure.
 * 
 * @author WuestMan
 *
 */
public class ItemWareHouse extends StructureItem
{
	public ItemWareHouse(String name)
	{
		super(name);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public GuiStructure getScreen()
	{
		return new GuiWareHouse();
	}
}