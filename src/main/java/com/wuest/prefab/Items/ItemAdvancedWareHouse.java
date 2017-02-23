package com.wuest.prefab.Items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.StructureGen.CustomStructures.StructureWarehouse;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemAdvancedWareHouse extends ItemWareHouse
{
	public ItemAdvancedWareHouse(String name)
	{
		super(name);
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
				if (player.dimension == -1 || player.dimension == 1)
				{
					player.addChatMessage(new TextComponentString("The Advanced Warehouse cannot be placed in the nether or the end."));
					return EnumActionResult.FAIL;
				}
				
				// Open the client side gui to determine the house options.
				//StructureWarehouse wareHouse = new StructureWarehouse();
				//wareHouse.ScanStructure(world, hitBlockPos, player.getHorizontalFacing(), true);
				player.openGui(Prefab.instance, ModRegistry.GuiAdvancedWareHouse, player.worldObj, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
				return EnumActionResult.PASS;
			}
		}

		return EnumActionResult.FAIL;
	}

}
