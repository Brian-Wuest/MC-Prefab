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
public class ItemInstantBridge extends Item
{
	public ItemInstantBridge(String name)
	{
		super(new Item.Properties()
			.group(ItemGroup.MISC)
			.maxDamage(10)
			.maxStackSize(1));

		ModRegistry.setItemName(this, name);
	}

	@OnlyIn(Dist.CLIENT)
	public GuiStructure getScreen()
	{
		return new GuiInstantBridge();
	}

	/**
	 * Does something when the item is right-clicked.
	 */
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		if (context.getWorld().isRemote)
		{
			if (context.getFace() == Direction.UP)
			{
				// Open the client side gui to determine the house options.
				GuiStructure screen = this.getScreen();
				screen.pos = context.getPos();
				Minecraft.getInstance().displayGuiScreen(screen);
				return ActionResultType.PASS;
			}
		}

		return ActionResultType.FAIL;
	}
}
