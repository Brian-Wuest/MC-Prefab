package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
	
	/**
	 * Initializes a new instance of the StructureItem class.
	 */
	public StructureItem(String name, int guiId)
	{
		super();
		
		this.Initialize(name, guiId);
	}
	
	/**
	 * Does something when the item is right-clicked.
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			if (side == EnumFacing.UP)
			{
				// Open the client side gui to determine the house options.
				player.openGui(Prefab.instance, this.guiId, player.world, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
				return EnumActionResult.PASS;
			}
		}

		return EnumActionResult.FAIL;
	}
	
	/**
	 * Initializes common fields/properties for this structure item.
	 */
	protected void Initialize(String name, int guiId)
	{
		ModRegistry.setItemName(this, name);
		this.guiId = guiId;
		this.setCreativeTab(CreativeTabs.MISC);
	}
}
