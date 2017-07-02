package com.wuest.prefab.StructureGen;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Lists;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.wuest.prefab.*;
import com.wuest.prefab.Config.Structures.StructureConfiguration;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Each structure represents a building which is pre-defined in a JSON file.
 * 
 * @author WuestMan
 */
public class Structure
{
	@Expose
	private String name;

	@Expose
	private BuildClear clearSpace;

	@Expose
	private ArrayList<BuildBlock> blocks;

	public ArrayList<BlockPos> clearedBlockPos = new ArrayList<BlockPos>();
	public ArrayList<BuildBlock> priorityOneBlocks = new ArrayList<BuildBlock>();
	public ArrayList<BuildBlock> priorityTwoBlocks = new ArrayList<BuildBlock>();
	public ArrayList<BuildBlock> priorityThreeBlocks = new ArrayList<BuildBlock>();
	public StructureConfiguration configuration;
	public World world;
	public BlockPos originalPos;
	public EnumFacing assumedNorth;

	public Structure()
	{
		this.Initialize();
	}

	/**
	 * Creates an instance of the structure after reading from a resource
	 * location and converting it from JSON.
	 * 
	 * @param <T> The type which extends Structure.
	 * @param resourceLocation The location of the JSON file to load. Example:
	 *            "assets/prefab/structures/warehouse.json"
	 * @param child The child class which extends Structure.
	 * @return Null if the resource wasn't found or the JSON could not be
	 *         parsed, otherwise the de-serialized object.
	 */
	public static <T extends Structure> T CreateInstance(String resourceLocation, Class<? extends Structure> child)
	{
		T structure = null;

		Gson file = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		structure = (T) file.fromJson(ZipUtil.decompressResource(resourceLocation), child);

		return structure;
	}

	public static void CreateStructureFile(Structure structure, String fileLocation)
	{
		try
		{
			Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			StringWriter stringWriter = new StringWriter();
			converter.toJson(structure, stringWriter);

			ZipUtil.zipStringToFile(stringWriter.toString(), fileLocation);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void ScanStructure(World world, BlockPos originalPos, BlockPos cornerPos1, BlockPos cornerPos2, String fileLocation, BuildClear clearedSpace,
			EnumFacing playerFacing, boolean includeAir, boolean excludeWater)
	{
		Structure scannedStructure = new Structure();
		scannedStructure.setClearSpace(clearedSpace);

		for (BlockPos currentPos : BlockPos.getAllInBox(cornerPos1, cornerPos2))
		{
			if (world.isAirBlock(currentPos) && !includeAir)
			{
				continue;
			}

			IBlockState currentState = world.getBlockState(currentPos);
			Block currentBlock = currentState.getBlock();
			
			if (currentState.getMaterial() == Material.WATER && excludeWater)
			{
				continue;
			}

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
			}
			else
			{
				buildBlock.getStartingPosition().setWestOffset(originalPos.getX() - currentPos.getX());
			}

			if (currentPos.getZ() > originalPos.getZ())
			{
				buildBlock.getStartingPosition().setSouthOffset(currentPos.getZ() - originalPos.getZ());
			}
			else
			{
				buildBlock.getStartingPosition().setNorthOffset(originalPos.getZ() - currentPos.getZ());
			}

			buildBlock.getStartingPosition().setHeightOffset(currentPos.getY() - originalPos.getY());

			ImmutableMap<IProperty<?>, Comparable<?>> properties = currentState.getProperties();

			for (Entry<IProperty<?>, Comparable<?>> entry : properties.entrySet())
			{
				BuildProperty property = new BuildProperty();
				property.setName(entry.getKey().getName());

				if (currentBlock instanceof BlockQuartz && property.getName().equals("variant"))
				{
					property.setValue(((BlockQuartz.EnumType) entry.getValue()).getName());
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
	 * @param player The player requesting the structure.
	 * @return True if the build can occur, otherwise false.
	 */
	public boolean BuildStructure(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player)
	{
		BlockPos startBlockPos = this.clearSpace.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing);
		BlockPos endBlockPos = startBlockPos.offset(configuration.houseFacing.rotateYCCW(), this.clearSpace.getShape().getWidth() - 1)
				.offset(configuration.houseFacing.getOpposite(), this.clearSpace.getShape().getWidth() - 1)
				.offset(EnumFacing.UP, this.clearSpace.getShape().getHeight());
		
		// Make sure this structure can be placed here.
		if (!BuildingMethods.CheckBuildSpaceForAllowedBlockReplacement(configuration, world, startBlockPos, endBlockPos, player))
		{
			// Send a message to the player saying that the structure could not
			// be built.
			player.sendMessage(new TextComponentTranslation(GuiLangKeys.GUI_STRUCTURE_NOBUILD).setStyle(new Style().setColor(TextFormatting.GREEN)));
			return false;
		}

		if (!this.BeforeBuilding(configuration, world, originalPos, assumedNorth, player))
		{
			// First, clear the area where the structure will be built.
			this.ClearSpace(configuration, world, originalPos, assumedNorth);

			// Now place all of the blocks.
			for (BuildBlock block : this.getBlocks())
			{
				Block foundBlock = Block.REGISTRY.getObject(block.getResourceLocation());

				if (foundBlock != null)
				{
					IBlockState blockState = foundBlock.getDefaultState();
					BuildBlock subBlock = null;

					// Check if water should be replaced with cobble.
					if (!this.WaterReplacedWithCobbleStone(configuration, block, world, originalPos, assumedNorth, foundBlock, blockState, player) 
							&& !this.CustomBlockProcessingHandled(configuration, block, world, originalPos, assumedNorth, foundBlock, blockState, player))
					{
						block = BuildBlock.SetBlockState(configuration, world, originalPos, assumedNorth, block, foundBlock, blockState);

						if (block.getSubBlock() != null)
						{
							foundBlock = Block.REGISTRY.getObject(block.getSubBlock().getResourceLocation());
							blockState = foundBlock.getDefaultState();

							subBlock = BuildBlock.SetBlockState(configuration, world, originalPos, assumedNorth, block.getSubBlock(), foundBlock, blockState);
						}

						if (subBlock != null)
						{
							block.setSubBlock(subBlock);
						}

						if (!block.getHasFacing())
						{
							if (subBlock != null)
							{
								block.setSubBlock(subBlock);
							}

							this.priorityOneBlocks.add(block);
						}
						else
						{
							// These blocks may be attached to other facing
							// blocks and must be done later.
							if (foundBlock instanceof BlockTorch || foundBlock instanceof BlockSign || foundBlock instanceof BlockLever
									|| foundBlock instanceof BlockButton)
							{
								this.priorityThreeBlocks.add(block);
							}
							else
							{
								this.priorityTwoBlocks.add(block);
							}
						}
					}
				}
			}

			if (ModEventHandler.structuresToBuild.containsKey(player))
			{
				ModEventHandler.structuresToBuild.get(player).add(this);
			}
			else
			{
				ArrayList<Structure> structures = new ArrayList<Structure>();
				structures.add(this);
				this.configuration = configuration;
				this.world = world;
				this.assumedNorth = assumedNorth;
				this.originalPos = originalPos;
				ModEventHandler.structuresToBuild.put(player, structures);
			}
		}

		return true;
	}

	/**
	 * This method is used before any building occurs to check for things or
	 * possibly pre-build locations. Note: This is even done before blocks are
	 * cleared.
	 * 
	 * @param configuration The structure configuration.
	 * @param world The current world.
	 * @param originalPos The original position clicked on.
	 * @param assumedNorth The assumed northern direction.
	 * @param player The player which initiated the construction.
	 * @return False if processing should continue, otherwise true to cancel
	 *         processing.
	 */
	protected boolean BeforeBuilding(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player)
	{
		return false;
	}

	/**
	 * This method is used after the main building is build for any additional
	 * structures or modifications.
	 * 
	 * @param configuration The structure configuration.
	 * @param world The current world.
	 * @param originalPos The original position clicked on.
	 * @param assumedNorth The assumed northern direction.
	 * @param player The player which initiated the construction.
	 */
	public void AfterBuilding(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player)
	{
	}

	protected void ClearSpace(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth)
	{
		if (this.clearSpace.getShape().getWidth() > 0 
				&& this.clearSpace.getShape().getLength() > 0) 
		{
			BlockPos startBlockPos = this.clearSpace.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing);
			BlockPos endBlockPos = startBlockPos.offset(configuration.houseFacing.rotateYCCW(), this.clearSpace.getShape().getWidth() - 1)
					.offset(configuration.houseFacing.getOpposite(), this.clearSpace.getShape().getLength() - 1)
					.offset(EnumFacing.UP, this.clearSpace.getShape().getHeight());
					
			this.clearedBlockPos = Lists.newArrayList(BlockPos.getAllInBox(startBlockPos, endBlockPos));
		}
		else
		{
			this.clearedBlockPos = new ArrayList<BlockPos>();
		}
	}

	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
			EnumFacing assumedNorth, Block foundBlock, IBlockState blockState, EntityPlayer player)
	{
		return false;
	}

	/**
	 * Determines if a water block was replaced with cobblestone because this structure was built in the nether or the end.
	 * @param configuration The structure configuration.
	 * @param block The build block object.
	 * @param world The workd object.
	 * @param originalPos The original block position this structure was built on.
	 * @param assumedNorth The assumed north direction (typically north).
	 * @param foundBlock The actual block found at the current location.
	 * @param blockState The block state to set for the current block.
	 * @param player The player requesting this build.
	 * @return Returns true if the water block was replaced by cobblestone, otherwise false.
	 */
	protected Boolean WaterReplacedWithCobbleStone(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
			EnumFacing assumedNorth, Block foundBlock, IBlockState blockState, EntityPlayer player)
	{
		// Replace water blocks with cobblestone.
		if (foundBlock instanceof BlockLiquid && blockState.getMaterial() == Material.WATER
				&& (world.provider.getDimensionType() == DimensionType.NETHER))
		{
			block.setBlockDomain(Blocks.COBBLESTONE.getRegistryName().getResourceDomain());
			block.setBlockName(Blocks.COBBLESTONE.getRegistryName().getResourcePath());
			block.setBlockState(Blocks.COBBLESTONE.getDefaultState());
			
			// Add this as a priority 3 block since it should be done at the end.
			this.priorityThreeBlocks.add(block);
			return true;
		}
		
		return false;
	}
}