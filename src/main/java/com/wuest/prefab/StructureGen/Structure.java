package com.wuest.prefab.StructureGen;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.gson.Gson;
import com.wuest.prefab.BuildingMethods;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.ZipUtil;
import com.wuest.prefab.Config.StructureConfiguration;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Each structure represents a building which is pre-defined in a JSON file.
 * 
 * @author WuestMan
 */
public class Structure
{
	private String name;
	private BuildClear clearSpace;
	private ArrayList<BuildBlock> blocks;
	protected ArrayList<BuildBlock> placedBlocks;

	public Structure()
	{
		this.Initialize();
	}

	/**
	 * Creates an instance of the structure after reading from a resource
	 * location and converting it from JSON.
	 * 
	 * @param resourceLocation The location of the JSON file to load. Example:
	 *            "assets/prefab/structures/warehouse.json"
	 * @return 
	 * @return Null if the resource wasn't found or the JSON could not be
	 *         parsed, otherwise the de-serialized object.
	 */
	public static <T extends Structure> T CreateInstance(String resourceLocation, Class<? extends Structure> child)
	{
		T structure = null;

		Gson file = new Gson();
		//InputStreamReader reader = new InputStreamReader(Prefab.class.getClassLoader().getResourceAsStream(resourceLocation), "UTF-8");
		structure = (T) file.fromJson(ZipUtil.decompressResource(resourceLocation), child);

		return structure;
	}

	public static void CreateStructureFile(Structure structure, String fileLocation)
	{
		try
		{
			Gson converter = new Gson();
			StringWriter stringWriter = new StringWriter();
			converter.toJson(structure, stringWriter);
			
			ZipUtil.zipStringToFile(stringWriter.toString(), fileLocation);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void ScanStructure(World world, BlockPos originalPos, BlockPos cornerPos1, BlockPos cornerPos2, String fileLocation, BuildClear clearedSpace, EnumFacing playerFacing)
	{
		Structure scannedStructure = new Structure();
		scannedStructure.setClearSpace(clearedSpace);

		for (BlockPos currentPos : BlockPos.getAllInBox(cornerPos1, cornerPos2))
		{
			if (world.isAirBlock(currentPos))
			{
				continue;
			}

			IBlockState currentState = world.getBlockState(currentPos);
			Block currentBlock = currentState.getBlock();

			BuildBlock buildBlock = new BuildBlock();
			buildBlock.setBlockDomain(currentBlock.getRegistryName().getResourceDomain());
			buildBlock.setBlockName(currentBlock.getRegistryName().getResourcePath());

			// if (currentPos.getX() > originalPos.getX()). currentPos is "East"
			// of hitBlock
			// if (currentPos.getZ() > originalPos.getZ()). currentPos is
			// "South" of hitBlock

			if (currentPos.getX() > originalPos.getX())
			{
				buildBlock.getStartingPosition().setEastOffset(currentPos.getX() - originalPos.getX());
				//buildBlock.getStartingPosition().setAppropriateOffSet(playerFacing, EnumFacing.EAST, currentPos.getX() - originalPos.getX());
			}
			else
			{
				buildBlock.getStartingPosition().setWestOffset(originalPos.getX() - currentPos.getX());
				//buildBlock.getStartingPosition().setAppropriateOffSet(playerFacing, EnumFacing.WEST, originalPos.getX() - currentPos.getX());
			}

			if (currentPos.getZ() > originalPos.getZ())
			{
				buildBlock.getStartingPosition().setSouthOffset(currentPos.getZ() - originalPos.getZ());
				//buildBlock.getStartingPosition().setAppropriateOffSet(playerFacing, EnumFacing.SOUTH, currentPos.getZ() - originalPos.getZ());
			}
			else
			{
				buildBlock.getStartingPosition().setNorthOffset(originalPos.getZ() - currentPos.getZ());
				//buildBlock.getStartingPosition().setAppropriateOffSet(playerFacing, EnumFacing.NORTH, originalPos.getZ() - currentPos.getZ());
			}

			buildBlock.getStartingPosition().setHeightOffset(currentPos.getY() - originalPos.getY());

			ImmutableMap<IProperty<?>, Comparable<?>> properties = currentState.getProperties();

			for (Entry<IProperty<?>, Comparable<?>> entry : properties.entrySet())
			{
				BuildProperty property = new BuildProperty();
				property.setName(entry.getKey().getName());
				
				if (currentBlock instanceof BlockQuartz && property.getName().equals("variant"))
				{
					property.setValue(((BlockQuartz.EnumType)entry.getValue()).getName());
				}
				else
				{
					property.setValue(entry.getValue().toString());
				}

				buildBlock.getProperties().add(property);
			}

			scannedStructure.getBlocks().add(buildBlock);
		}

		Structure.CreateStructureFile(scannedStructure, fileLocation);
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String value)
	{
		this.name = value;
	}

	public BuildClear getClearSpace()
	{
		return this.clearSpace;
	}

	public void setClearSpace(BuildClear value)
	{
		this.clearSpace = value;
	}

	public ArrayList<BuildBlock> getBlocks()
	{
		return this.blocks;
	}

	public void setBlocks(ArrayList<BuildBlock> value)
	{
		this.blocks = value;
	}

	public void Initialize()
	{
		this.name = "";
		this.clearSpace = new BuildClear();
		this.blocks = new ArrayList<BuildBlock>();
	}

	/**
	 * This is the main building method for this structure.
	 * 
	 * @param configuration The configuration the user updated.
	 * @param world The current world.
	 * @param originalPos The block the user clicked on.
	 * @param assumedNorth This should always be "NORTH" when the file is based on a scan.
	 */
	public void BuildStructure(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player)
	{
		if (!this.BeforeBuilding(configuration, world, originalPos, assumedNorth, player))
		{
			// First, clear the area where the structure will be built.
			this.ClearSpace(configuration, world, originalPos, assumedNorth);
			
			this.placedBlocks = new ArrayList<BuildBlock>();
			
			// Now place all of the blocks.
			for (BuildBlock block : this.getBlocks())
			{
				Block foundBlock = Block.REGISTRY.getObject(block.getResourceLocation());
	
				if (foundBlock != null)
				{
					IBlockState blockState = foundBlock.getDefaultState();
					BuildBlock subBlock = null;
					
					if (!this.CustomBlockProcessingHandled(configuration, block, world, originalPos, assumedNorth, foundBlock, blockState, player))
					{
						block = BuildBlock.SetBlockState(configuration, world, originalPos, assumedNorth, block, foundBlock, blockState);
						
						if (block.getSubBlock() != null)
						{
							foundBlock = Block.REGISTRY.getObject(block.getSubBlock().getResourceLocation());
							blockState = foundBlock.getDefaultState();
							
							subBlock = BuildBlock.SetBlockState(configuration, world, originalPos, assumedNorth, block.getSubBlock(), foundBlock, blockState);
						}
						
						if (!block.getHasFacing())
						{
							BuildingMethods.ReplaceBlockNoAir(world, block.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing), block.getBlockState());
							
							if (subBlock != null)
							{
								BuildingMethods.ReplaceBlockNoAir(world, subBlock.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing), subBlock.getBlockState());
							}
						}
						else
						{
							if (subBlock != null)
							{
								block.setSubBlock(subBlock);
							}
							
							this.placedBlocks.add(block);
						}
					}
				}
			}
			
			ArrayList<BuildBlock> otherFacingBlocks = new ArrayList<BuildBlock>();
			
			// Now place all of the facing blocks. This needs to be done here these blocks may not "stick" before all of the other solid blocks are placed.
			for (BuildBlock currentBlock : this.placedBlocks)
			{
				IBlockState state = currentBlock.getBlockState();

				// These blocks may be attached to other facing blocks and must be done later.
				if (state.getBlock() instanceof BlockTorch 
						|| state.getBlock() instanceof BlockSign
						|| state.getBlock() instanceof BlockLever
						|| state.getBlock() instanceof BlockButton)
				{
					otherFacingBlocks.add(currentBlock);
					continue;
				}
				
				BuildingMethods.ReplaceBlockNoAir(world, currentBlock.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing), state);
				
				// After placing the initial block, set the sub-block. This needs to happen as the list isn't always in the correct order.
				if (currentBlock.getSubBlock() != null)
				{
					BuildBlock subBlock = currentBlock.getSubBlock();
					
					BuildingMethods.ReplaceBlockNoAir(world, subBlock.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing), state);
				}
			}
			
			// Do the final facing blocks, these ones MUST be done last.
			for (BuildBlock currentBlock : otherFacingBlocks)
			{
				BuildingMethods.ReplaceBlockNoAir(world, currentBlock.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing), currentBlock.getBlockState());
			}
		}
		
		// Process any after block building needs.
		this.AfterBuilding(configuration, world, originalPos, assumedNorth, player);
	}

	/**
	 * This method is used before any building occurs to check for things or possibly pre-build locations. Note: This is even done before blocks are cleared.
	 * @param configuration The structure configuration.
	 * @param world The current world.
	 * @param originalPos The original position clicked on.
	 * @param assumedNorth The assumed northern direction.
	 * @param player The player which initiated the construction.
	 * @return False if processing should continue, otherwise true to cancel processing.
	 */
	protected boolean BeforeBuilding(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player)
	{
		return false;
	}
	
	/**
	 * This method is used after the main building is build for any additional structures or modifications.
	 * @param configuration The structure configuration.
	 * @param world The current world.
	 * @param originalPos The original position clicked on.
	 * @param assumedNorth The assumed northern direction.
	 * @param player The player which initiated the construction.
	 */
	protected void AfterBuilding(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player)
	{
	}
	
	protected void ClearSpace(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth)
	{
		BuildingMethods.ClearSpaceExact(world, this.clearSpace.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing),
				this.clearSpace.getShape().getWidth(), this.clearSpace.getShape().getHeight(), this.clearSpace.getShape().getLength(), configuration.houseFacing);
	}

	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos, EnumFacing assumedNorth,
			Block foundBlock, IBlockState blockState, EntityPlayer player)
	{
		return false;
	}
}