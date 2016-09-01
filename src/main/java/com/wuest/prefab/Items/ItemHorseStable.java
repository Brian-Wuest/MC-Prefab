package com.wuest.prefab.Items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.ChickenCoopConfiguration;
import com.wuest.prefab.Config.HorseStableConfiguration;
import com.wuest.prefab.StructureGen.CustomStructures.StructureChickenCoop;
import com.wuest.prefab.StructureGen.CustomStructures.StructureHorseStable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHorseStable extends Item
{
	private ChickenCoopConfiguration currentConfiguration = null;

	public ItemHorseStable(String name)
	{
		super();

		this.setCreativeTab(CreativeTabs.MISC);
		ModRegistry.setItemName(this, name);
	}
	
	/**
	 * Does something when the item is right-clicked.
	 */
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			if (side == EnumFacing.UP)
			{
				// Open the client side gui to determine the house options.
				//StructureHorseStable horseStable = new StructureHorseStable();
				//horseStable.ScanStructure(world, hitBlockPos, player.getHorizontalFacing());
				player.openGui(Prefab.instance, ModRegistry.GuiHorseStable, player.worldObj, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
				return EnumActionResult.PASS;
			}
		}

		return EnumActionResult.FAIL;
	}
	
	public static void BuildHouse(EntityPlayer player, World world, HorseStableConfiguration configuration)
	{
		// This is always on the server.
		if (configuration != null)
		{
			BlockPos hitBlockPos = configuration.pos;
			BlockPos playerPosition = player.getPosition();

			IBlockState hitBlockState = world.getBlockState(hitBlockPos);

			if (hitBlockState != null)
			{
				Block hitBlock = hitBlockState.getBlock();
 
				if (hitBlock != null)
				{
					StructureHorseStable structure = StructureHorseStable.CreateInstance(StructureHorseStable.ASSETLOCATION, StructureHorseStable.class);
					structure.BuildStructure(configuration, world, hitBlockPos, EnumFacing.NORTH, player);
					
					player.inventory.clearMatchingItems(ModRegistry.HorseStable(), -1, 1, null);
					player.inventoryContainer.detectAndSendChanges();
				}
			}
		}
	}
}
