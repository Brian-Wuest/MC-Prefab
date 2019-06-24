package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Gui.GuiStructure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class StructureItem extends Item
{

	/**
	 * Get's the GuiId to show to the user when this item is used.
	 */
	protected int guiId = 0;
	protected GuiStructure screen = null;
	
	/**
	 * Initializes a new instance of the StructureItem class.
	 */
	public StructureItem(String name, GuiStructure screen)
	{
		super(new Item.Properties().group(ItemGroup.MISC));
		this.Initialize(name, screen);
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
				this.screen.pos = context.getPos();
				Minecraft.getInstance().displayGuiScreen(this.screen);
				return ActionResultType.PASS;
			}
		}

		return ActionResultType.FAIL;
	}
	
	/**
	 * Initializes common fields/properties for this structure item.
	 */
	protected void Initialize(String name, GuiStructure screen)
	{
		ModRegistry.setItemName(this, name);
		this.screen = screen;
	}
}
