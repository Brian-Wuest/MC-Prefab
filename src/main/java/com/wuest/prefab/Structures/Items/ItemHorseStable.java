package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Gui.GuiHorseStable;
import com.wuest.prefab.Structures.Gui.GuiStructure;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemHorseStable extends StructureItem
{
	public ItemHorseStable(String name)
	{
		super(name);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public GuiStructure getScreen()
	{
		return new GuiHorseStable();
	}
}
