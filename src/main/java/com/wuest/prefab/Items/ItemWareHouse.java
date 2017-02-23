package com.wuest.prefab.Items;

import java.io.*;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.WareHouseConfiguration;
import com.wuest.prefab.Gui.GuiWareHouse;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.StructureGen.*;
import com.wuest.prefab.StructureGen.CustomStructures.StructureWarehouse;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * This class is used to build the warehouse structure.
 * @author WuestMan
 *
 */
public class ItemWareHouse extends Item
{
	private WareHouseConfiguration currentConfiguration = null;

	public ItemWareHouse(String name)
	{
		super();

		this.setCreativeTab(CreativeTabs.MISC);
		ModRegistry.setItemName(this, name);
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
					player.addChatMessage(new TextComponentString("The Warehouse cannot be placed in the nether or the end."));
					return EnumActionResult.FAIL;
				}
				
				// Open the client side gui to determine the house options.
				player.openGui(Prefab.instance, ModRegistry.GuiWareHouse, player.worldObj, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
				return EnumActionResult.PASS;
			}
		}

		return EnumActionResult.FAIL;
	}
	
	public static void BuildHouse(EntityPlayer player, World world, WareHouseConfiguration configuration)
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
					String assetLocation = configuration.advanced ? StructureWarehouse.ADVANCED_ASSET_LOCATION : StructureWarehouse.ASSETLOCATION;
					
					StructureWarehouse structure = StructureWarehouse.CreateInstance(assetLocation, StructureWarehouse.class);
					
					if (structure.BuildStructure(configuration, world, hitBlockPos, EnumFacing.NORTH, player))
					{
						if (configuration.advanced)
						{
							player.inventory.clearMatchingItems(ModRegistry.AdvancedWareHouse(), -1, 1, null);
						}
						else
						{
							player.inventory.clearMatchingItems(ModRegistry.WareHouse(), -1, 1, null);
						}
						
						player.inventoryContainer.detectAndSendChanges();
					}
				}
			}
		}
	}
}