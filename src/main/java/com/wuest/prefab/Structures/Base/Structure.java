package com.wuest.prefab.Structures.Base;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.wuest.prefab.ZipUtil;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import com.wuest.prefab.Structures.Events.StructureEventHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBed.EnumPartType;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

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
	
	@Expose
	public ArrayList<BuildTileEntity> tileEntities = new ArrayList<BuildTileEntity>();
	
	@Expose
	public ArrayList<BuildEntity> entities = new ArrayList<BuildEntity>();

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

			BuildBlock buildBlock = Structure.createBuildBlockFromBlockState(currentState, currentBlock, currentPos, originalPos);
			
			if (currentBlock instanceof BlockDoor)
			{
				EnumDoorHalf blockHalf = currentState.getValue(BlockDoor.HALF);
				
				if (blockHalf == EnumDoorHalf.LOWER)
				{
					IBlockState upperHalfState = world.getBlockState(currentPos.up());
					
					if (upperHalfState != null && upperHalfState.getBlock() instanceof BlockDoor)
					{
						Block upperBlock = upperHalfState.getBlock();
						BuildBlock upperHalf = Structure.createBuildBlockFromBlockState(upperHalfState, upperBlock, currentPos.up(), originalPos);
						
						buildBlock.setSubBlock(upperHalf);
					}
				}
				else
				{
					// Don't process upper door halves. These were already done.
					continue;
				}
			}
			else if (currentBlock instanceof BlockBed)
			{
				EnumPartType bedPart = currentState.getValue(BlockBed.PART);
				
				if (bedPart == EnumPartType.HEAD)
				{
					IBlockState bedFoot = null;
					boolean foundFoot = false;
					EnumFacing facing = EnumFacing.NORTH;
					
					while (foundFoot == false)
					{
						bedFoot = world.getBlockState(currentPos.offset(facing));
						
						if (bedFoot.getBlock() instanceof BlockBed && bedFoot.getValue(BlockBed.PART) == EnumPartType.FOOT)
						{
							foundFoot = true;
							break;
						}
						
						facing = facing.rotateY();
						
						if (facing == EnumFacing.NORTH)
						{
							// Got back to north, break out to avoid infinite loop.
							break;
						}
					}
					
					if (foundFoot)
					{
						Block footBedBlock = bedFoot.getBlock();
						BuildBlock bed = Structure.createBuildBlockFromBlockState(bedFoot, footBedBlock, currentPos.offset(facing), originalPos);
						buildBlock.setSubBlock(bed);
					}
				}
				else
				{
					// Don't process foot of bed, it was already done.
					continue;
				}
			}

			scannedStructure.getBlocks().add(buildBlock);
			
			TileEntity tileEntity = world.getTileEntity(currentPos);
			
			if (tileEntity != null)
			{
				// Don't write data for empty tile entities.
				if ((tileEntity instanceof TileEntityChest && ((TileEntityChest)tileEntity).isEmpty())
						|| (tileEntity instanceof TileEntityFurnace && ((TileEntityFurnace)tileEntity).isEmpty()))
				{
					continue;
				}
				
				ResourceLocation resourceLocation = TileEntity.getKey(tileEntity.getClass());
				NBTTagCompound tagCompound = new NBTTagCompound();
				tileEntity.writeToNBT(tagCompound);
				
				BuildTileEntity buildTileEntity = new BuildTileEntity();
				buildTileEntity.setEntityDomain(resourceLocation.getResourceDomain());
				buildTileEntity.setEntityName(resourceLocation.getResourcePath());
				buildTileEntity.setStartingPosition(Structure.getStartingPositionFromOriginalAndCurrentPosition(currentPos, originalPos));
				buildTileEntity.setEntityNBTData(tagCompound);
				scannedStructure.tileEntities.add(buildTileEntity);
			}
		}
		
		int x_radiusRangeBegin = cornerPos1.getX() < cornerPos2.getX() ? cornerPos1.getX() : cornerPos2.getX();
		int x_radiusRangeEnd = cornerPos1.getX() < cornerPos2.getX() ? cornerPos2.getX() : cornerPos1.getX();
		int y_radiusRangeBegin = cornerPos1.getY() < cornerPos2.getY() ? cornerPos1.getY() : cornerPos2.getY();
		int y_radiusRangeEnd = cornerPos1.getY() < cornerPos2.getY() ? cornerPos2.getY() : cornerPos1.getY();
		int z_radiusRangeBegin = cornerPos1.getZ() < cornerPos2.getZ() ? cornerPos1.getZ() : cornerPos2.getZ();
		int z_radiusRangeEnd = cornerPos1.getZ() < cornerPos2.getZ() ? cornerPos2.getZ() : cornerPos1.getZ();
		
		for (Entity entity : world.getLoadedEntityList())
		{
			BlockPos entityPos = entity.getPosition();
			
			if (entityPos.getX() >= x_radiusRangeBegin && entityPos.getX() <= x_radiusRangeEnd
					&& entityPos.getZ() >= z_radiusRangeBegin && entityPos.getZ() <= z_radiusRangeEnd
					&& entityPos.getY() >= y_radiusRangeBegin && entityPos.getY() <= y_radiusRangeEnd)
			{
				BuildEntity buildEntity = new BuildEntity();
				buildEntity.setEntityId(EntityList.getID(entity.getClass()));
				buildEntity.setEntityResourceString(EntityList.getKey(entity));
				buildEntity.setStartingPosition(Structure.getStartingPositionFromOriginalAndCurrentPosition(entityPos, originalPos));
				buildEntity.entityXAxisOffset = entityPos.getX() - entity.posX;
				buildEntity.entityYAxisOffset = entityPos.getY() - entity.posY;
				buildEntity.entityZAxisOffset = entityPos.getZ() - entity.posZ;
				
				if (entity instanceof EntityItemFrame)
				{
					buildEntity.entityYAxisOffset = buildEntity.entityYAxisOffset * -1;
				}
				
				if (entity instanceof EntityHanging)
				{
					buildEntity.entityFacing = ((EntityHanging)entity).facingDirection;
				}
				
				NBTTagCompound entityTagCompound = new NBTTagCompound();
				entity.writeToNBT(entityTagCompound);
				buildEntity.setEntityNBTData(entityTagCompound);
				scannedStructure.entities.add(buildEntity);
			}
		}

		Structure.CreateStructureFile(scannedStructure, fileLocation);
	}
	
	/**
	 * Creates a build block from the current block state.
	 * @param currentState The block state.
	 * @param currentBlock The current block.
	 * @param currentPos The current position.
	 * @return A new Build block object.
	 */
	public static BuildBlock createBuildBlockFromBlockState(IBlockState currentState, Block currentBlock, BlockPos currentPos, BlockPos originalPos)
	{
		BuildBlock buildBlock = new BuildBlock();
		buildBlock.setBlockDomain(currentBlock.getRegistryName().getResourceDomain());
		buildBlock.setBlockName(currentBlock.getRegistryName().getResourcePath());
		buildBlock.setStartingPosition(Structure.getStartingPositionFromOriginalAndCurrentPosition(currentPos, originalPos));
		buildBlock.blockPos = currentPos;

		ImmutableMap<IProperty<?>, Comparable<?>> properties = currentState.getProperties();

		for (Entry<IProperty<?>, Comparable<?>> entry : properties.entrySet())
		{
			BuildProperty property = new BuildProperty();
			property.setName(entry.getKey().getName());

			if (currentBlock instanceof BlockQuartz && property.getName().equals("variant"))
			{
				property.setValue(((BlockQuartz.EnumType) entry.getValue()).getName());
			}
			else if (currentBlock instanceof BlockColored || currentBlock instanceof BlockCarpet && property.getName().equals("color"))
			{
				EnumDyeColor dyeColor = (EnumDyeColor)entry.getValue();
				property.setValue(dyeColor.getName());
			}
			else
			{
				property.setValue(entry.getValue().toString());
			}

			buildBlock.getProperties().add(property);
		}
		
		return buildBlock;
	}
	
	public static PositionOffset getStartingPositionFromOriginalAndCurrentPosition(BlockPos currentPos, BlockPos originalPos)
	{
		// if (currentPos.getX() > originalPos.getX()). currentPos is "East"
		// of hitBlock
		// if (currentPos.getZ() > originalPos.getZ()). currentPos is
		// "South" of hitBlock
		PositionOffset positionOffSet = new PositionOffset();
		
		if (currentPos.getX() > originalPos.getX())
		{
			positionOffSet.setEastOffset(currentPos.getX() - originalPos.getX());
		}
		else
		{
			positionOffSet.setWestOffset(originalPos.getX() - currentPos.getX());
		}

		if (currentPos.getZ() > originalPos.getZ())
		{
			positionOffSet.setSouthOffset(currentPos.getZ() - originalPos.getZ());
		}
		else
		{
			positionOffSet.setNorthOffset(originalPos.getZ() - currentPos.getZ());
		}

		positionOffSet.setHeightOffset(currentPos.getY() - originalPos.getY());
		
		return positionOffSet;
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
		BlockPos startBlockPos = this.clearSpace.getStartingPosition().getRelativePosition(originalPos, this.clearSpace.getShape().getDirection(), configuration.houseFacing);
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

			boolean blockPlacedWithCobbleStoneInstead = false;
			
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
						block = BuildBlock.SetBlockState(configuration, world, originalPos, assumedNorth, block, foundBlock, blockState, this);

						if (block.getSubBlock() != null)
						{
							foundBlock = Block.REGISTRY.getObject(block.getSubBlock().getResourceLocation());
							blockState = foundBlock.getDefaultState();

							subBlock = BuildBlock.SetBlockState(configuration, world, originalPos, assumedNorth, block.getSubBlock(), foundBlock, blockState, this);
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

							if (foundBlock instanceof BlockFlowerPot
									|| foundBlock instanceof BlockCarpet
									|| foundBlock instanceof BlockBed)
							{
								this.priorityThreeBlocks.add(block);
							}
							else
							{
								this.priorityOneBlocks.add(block);
							}
						}
						else
						{
							// These blocks may be attached to other facing blocks and must be done later.
							if (foundBlock instanceof BlockTorch || foundBlock instanceof BlockSign || foundBlock instanceof BlockLever
									|| foundBlock instanceof BlockButton
									|| foundBlock instanceof BlockBed)
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
				else
				{
					// Cannot find this block in the registry. This can happen if a structure file has a mod block that no longer exists.
					// In this case, print an informational message and replace it with cobblestone.
					String blockTypeNotFound = block.getResourceLocation().toString();
					block = BuildBlock.SetBlockState(configuration, world, originalPos, assumedNorth, block, Blocks.COBBLESTONE, Blocks.COBBLESTONE.getDefaultState(), this);
					this.priorityOneBlocks.add(block);
					
					if (!blockPlacedWithCobbleStoneInstead)
					{
						blockPlacedWithCobbleStoneInstead = true;
						FMLLog.log.warn("A Block was in the structure, but it is not registred. This block was replaced with vanilla cobblestone instead. Block type not found: [" + blockTypeNotFound + "]");
					}
				}
			}

			this.configuration = configuration;
			this.world = world;
			this.assumedNorth = assumedNorth;
			this.originalPos = originalPos;
			
			if (StructureEventHandler.structuresToBuild.containsKey(player))
			{
				StructureEventHandler.structuresToBuild.get(player).add(this);
			}
			else
			{
				ArrayList<Structure> structures = new ArrayList<Structure>();
				structures.add(this);
				StructureEventHandler.structuresToBuild.put(player, structures);
			}
		}

		return true;
	}

	/**
	 * This method is to process before a clear space block is set to air.
	 * @param pos The block position being processed.
	 */
	public void BeforeClearSpaceBlockReplaced(BlockPos pos)
	{
		
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
			BlockPos startBlockPos = this.clearSpace.getStartingPosition().getRelativePosition(originalPos, this.clearSpace.getShape().getDirection(), configuration.houseFacing);
			
			BlockPos endBlockPos = startBlockPos
					.offset(configuration.houseFacing.getOpposite().rotateY(), this.clearSpace.getShape().getWidth() - 1)
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