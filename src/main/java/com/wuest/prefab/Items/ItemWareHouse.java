package com.wuest.prefab.Items;

import java.io.*;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.WareHouseConfiguration;
import com.wuest.prefab.Gui.GuiWareHosue;
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
import net.minecraft.world.World;

/**
 * This class is used to build the warehouse structure.
 * @author WuestMan
 *
 */
public class ItemWareHouse extends Item
{
	protected static BlockPos NorthEastCorner;
	protected static BlockPos SouthEastCorner;
	protected static BlockPos SouthWestCorner;
	protected static BlockPos NorthWestCorner;

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
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			if (side == EnumFacing.UP)
			{
				// Open the client side gui to determine the house options.
				//Structure.ScanStructure(world, hitBlockPos, hitBlockPos.east(7).south(1).down(5), hitBlockPos.west(8).south(16).up(10), "C:\\Users\\Brian\\Documents\\GitHub\\MC-Prefab\\src\\main\\resources\\assets\\prefab\\structures\\warehouse.json");
				player.openGui(Prefab.instance, GuiWareHosue.GUI_ID, player.worldObj, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
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
					StructureWarehouse structure = StructureWarehouse.CreateInstance("assets/prefab/structures/warehouse.json", StructureWarehouse.class);
					structure.BuildStructure(configuration, world, hitBlockPos, EnumFacing.NORTH);
					
					player.inventory.clearMatchingItems(ModRegistry.WareHouse(), -1, 1, null);
					player.inventoryContainer.detectAndSendChanges();
				}
			}
		}
	}
	
	private static Structure getSeedStructure()
	{
		Structure structure = new Structure();
		structure.setName("WareHouse");
		
		// North wall.
		BuildWall wall = new BuildWall();
		wall.setBlockDomain("minecraft");
		wall.setBlockName("stoneBrick");
		wall.getShape().setDirection(EnumFacing.EAST);
		wall.getShape().setHeight(5);
		wall.getShape().setWidth(1);
		wall.getShape().setLength(10);
		wall.getStartingPosition().setWestOffset(4);
		structure.getWalls().add(wall);
		
		// West Wall.
		wall = new BuildWall();
		wall.setBlockDomain("minecraft");
		wall.setBlockName("stoneBrick");
		wall.getShape().setDirection(EnumFacing.SOUTH);
		wall.getShape().setHeight(5);
		wall.getShape().setWidth(1);
		wall.getShape().setLength(10);
		wall.getStartingPosition().setWestOffset(4);
		structure.getWalls().add(wall);
		
		// South Wall.
		wall = new BuildWall();
		wall.setBlockDomain("minecraft");
		wall.setBlockName("stoneBrick");
		wall.getShape().setDirection(EnumFacing.EAST);
		wall.getShape().setHeight(5);
		wall.getShape().setWidth(1);
		wall.getShape().setLength(10);
		wall.getStartingPosition().setSouthOffset(10);
		wall.getStartingPosition().setWestOffset(4);
		structure.getWalls().add(wall);
		
		// East Wall.
		wall = new BuildWall();
		wall.setBlockDomain("minecraft");
		wall.setBlockName("stoneBrick");
		wall.getShape().setDirection(EnumFacing.SOUTH);
		wall.getShape().setHeight(5);
		wall.getShape().setWidth(1);
		wall.getShape().setLength(10);
		wall.getStartingPosition().setEastOffset(6);
		structure.getWalls().add(wall);
		
		// Floor
		BuildFloor floor = new BuildFloor();
		floor.setBlockDomain("minecraft");
		floor.setBlockName("brick");
		floor.getShape().setDirection(EnumFacing.SOUTH);
		floor.getShape().setHeight(1);
		floor.getShape().setLength(10);
		floor.getShape().setWidth(10);
		floor.getStartingPosition().setWestOffset(4);
		structure.getFloors().add(floor);
		
		BuildBlock block = new BuildBlock();
		block.setBlockDomain("minecraft");
		block.setBlockName("furnace");
		block.getStartingPosition().setEastOffset(1);
		block.getStartingPosition().setSouthOffset(1);
		
		BuildProperty property = new BuildProperty();
		property.setName("facing");
		property.setValue("east");
		block.getProperties().add(property);
		structure.getBlocks().add(block);
		
		block = new BuildBlock();
		block.setBlockDomain("prefab");
		block.setBlockName("blockCompressedGlowstone");
		block.getStartingPosition().setEastOffset(2);
		block.getStartingPosition().setSouthOffset(1);
		structure.getBlocks().add(block);
		
		return structure;
	}
}