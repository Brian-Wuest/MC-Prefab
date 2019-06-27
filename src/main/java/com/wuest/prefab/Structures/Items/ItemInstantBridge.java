package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Gui.GuiInstantBridge;
import com.wuest.prefab.Structures.Gui.GuiStructure;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * This is the instant bridge item.
 * 
 * @author WuestMan
 *
 */
public class ItemInstantBridge extends StructureItem
{
	public ItemInstantBridge(String name)
	{
		super(name, new Item.Properties()
			.group(ItemGroup.MISC)
			.maxDamage(10)
			.maxStackSize(1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public GuiStructure getScreen()
	{
		return new GuiInstantBridge();
	}
}
