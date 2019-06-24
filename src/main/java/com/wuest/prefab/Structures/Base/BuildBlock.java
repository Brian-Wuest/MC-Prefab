package com.wuest.prefab.Structures.Base;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.base.Optional;
import com.google.gson.annotations.Expose;
import com.wuest.prefab.Structures.Config.StructureConfiguration;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.client.renderer.BlockModelRenderer.Orientation;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.IProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class defines a single block and where it will be in the structure.
 * @author WuestMan
 */
public class BuildBlock
{
	@Expose
	private String blockDomain;
	
	@Expose
	private String blockName;
	
	@Expose
	private PositionOffset startingPosition;
	
	@Expose
	private ArrayList<BuildProperty> properties;
	
	@Expose
	private BuildBlock subBlock;
	
	@Expose
	private boolean hasFacing;
	
	@Expose
	private BlockState state;
	
	@Expose
	private String blockStateData;
	
	public BlockPos blockPos;
	
	public BuildBlock()
	{
		this.Initialize();
	}
	
	public String getBlockDomain()
	{
		return this.blockDomain;
	}
	
	public void setBlockDomain(String value)
	{
		this.blockDomain = value;
	}
	
	public String getBlockName()
	{
		return this.blockName;
	}
	
	public void setBlockName(String value)
	{
		this.blockName = value;
	}
	
	public ResourceLocation getResourceLocation()
	{
		ResourceLocation location = new ResourceLocation(this.blockDomain, this.blockName);
		return location;
	}
	
	public PositionOffset getStartingPosition()
	{
		return this.startingPosition;
	}
	
	public void setStartingPosition(PositionOffset value)
	{
		this.startingPosition = value;
	}
	
	public ArrayList<BuildProperty> getProperties()
	{
		return this.properties;
	}
	
	public void setProperties(ArrayList<BuildProperty> value)
	{
		this.properties = value;
	}
	
	public BuildProperty getProperty(String name)
	{
		for (BuildProperty property : this.getProperties())
		{
			if (name.equals(property.getName()))
			{
				return property;
			}
		}
		
		return null;
	}
	
	public BuildBlock getSubBlock()
	{
		return this.subBlock;
	}
	
	public void setSubBlock(BuildBlock value)
	{
		this.subBlock = value;
	}
	
	public boolean getHasFacing()
	{
		return this.hasFacing;
	}
	
	public void setHasFacing(boolean value)
	{
		this.hasFacing = value;
	}
	
	public BlockState getBlockState()
	{
		return this.state;
	}
	
	public void setBlockState(BlockState value)
	{
		this.state = value;
	}
	
	public String getBlockStateData()
	{
		return this.blockStateData;
	}
	
	public void setBlockStateData(String value)
	{
		this.blockStateData = value;
	}
	
	public void setBlockStateData(NBTTagCompound tagCompound)
	{
		this.blockStateData = tagCompound.toString();
	}
	
	public NBTTagCompound getBlockStateDataTag()
	{
		NBTTagCompound tag = null;
		
		if (!this.blockStateData.equals(""))
		{
			try
			{
				tag = JsonToNBT.getTagFromJson(this.blockStateData);
			}
			catch (NBTException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return tag;
	}
	
	public BlockState getBlockStateFromDataTag()
	{
		BlockState state = null;
		
		if (!this.blockStateData.equals(""))
		{
			NBTTagCompound tag = this.getBlockStateDataTag();
			
			if (tag != null)
			{
				state = NBTUtil.readBlockState(tag.getCompoundTag("tag"));
			}
		}
		
		return state;
	}
	
	public void Initialize()
	{
		this.blockDomain = "";
		this.blockName = "";
		this.properties = new ArrayList<BuildProperty>();
		this.hasFacing = false;
		this.state = null;
		this.subBlock = null;
		this.startingPosition = new PositionOffset();
		this.blockStateData = "";
	}
	
	public static BuildBlock SetBlockState(StructureConfiguration configuration, World world, BlockPos originalPos, Direction assumedNorth, BuildBlock block, Block foundBlock, BlockState blockState, Structure structure)
	{
		try
		{
			if (!block.blockStateData.equals(""))
			{
				return BuildBlock.SetBlockStateFromTagData(configuration, world, originalPos, assumedNorth, block, foundBlock, blockState, structure);
			}
			
			Direction vineFacing = BuildBlock.getVineFacing(configuration, foundBlock, block, structure.getClearSpace().getShape().getDirection());
			EnumAxis logFacing = BuildBlock.getLogFacing(configuration, foundBlock, block, structure.getClearSpace().getShape().getDirection());
			Axis boneFacing = BuildBlock.getBoneFacing(configuration, foundBlock, block, structure.getClearSpace().getShape().getDirection());
			BlockQuartz.EnumType quartzFacing = BuildBlock.getQuartsFacing(configuration, foundBlock, block, structure.getClearSpace().getShape().getDirection());
			EnumOrientation leverOrientation = BuildBlock.getLeverOrientation(configuration, foundBlock, block, structure.getClearSpace().getShape().getDirection());
			
			// If this block has custom processing for block state just continue onto the next block. The sub-class is expected to place the block.
			if (block.getProperties().size() > 0)
			{
				Collection<IProperty<?>> properties = blockState.getPropertyKeys();
				
				// Go through each property of this block and set it.
				// The state will be updated as the properties are
				// applied.
				for (IProperty<?> property : properties)
				{
					BuildProperty buildProperty = block.getProperty(property.getName());
					
					// Make sure that this property exists in our file. The only way it wouldn't be there would be if a mod adds properties to vanilla blocks.
					if (buildProperty != null)
					{
						try
						{
							Optional<?> propertyValue  = property.parseValue(buildProperty.getValue());
							
							if (!propertyValue.isPresent()
									|| propertyValue.getClass().getName().equals("com.google.common.base.Absent"))
							{
								FMLLog.log.warn("Property value for property name [" + property.getName() + "] for block [" + block.getBlockName() + "] is considered Absent, figure out why.");
								continue;
							}
							
							Comparable<?> comparable = property.getValueClass().cast(propertyValue.get());
							
							if (comparable == null)
							{
								continue;
							}
							
							comparable = BuildBlock.setComparable(comparable, foundBlock, property, configuration, block, assumedNorth, propertyValue, vineFacing, logFacing, boneFacing, quartzFacing, leverOrientation, structure);
			
							if (comparable == null)
							{
								continue;
							}
							
							try
							{
								if (blockState.getValue(property) != comparable)
								{
									blockState = BuildBlock.setProperty(blockState, property, comparable);
								}
							}
							catch (Exception ex)
							{
								System.out.println("Error setting properly value for property name [" + property.getName() + "] property value [" + buildProperty.getValue() + "] for block [" + block.getBlockName() + "] The default value will be used.");
							}
						}
						catch (Exception ex)
						{
							if (property != null && buildProperty != null)
							{
								System.out.println("Error getting properly value for property name [" + property.getName() + "] property value [" + buildProperty.getValue() + "] for block [" + block.getBlockName() + "]");
								throw ex;
							}
						}
					}
					else
					{
						//System.out.println("Property: [" + property.getName() + "] does not exist for Block: [" + block.getBlockName() + "] this is usually due to mods adding properties to vanilla blocks.");
					}
				}
			}
			
			block.setBlockState(blockState);
			return block;
		}
		catch (Exception ex)
		{
			System.out.println("Error setting block state for block [" + block.getBlockName() + "] for structure configuration class [" + configuration.getClass().getName() + "]");
			throw ex;
		}
	}
	
	public static Direction getHorizontalFacing(Direction currentFacing, Direction configurationFacing, Direction structureDirection)
	{
		if (currentFacing != null && currentFacing != Direction.UP && currentFacing != Direction.DOWN)
		{
			if (configurationFacing.getOpposite() == structureDirection.rotateY())
			{				
				currentFacing = currentFacing.rotateY();
			}
			else if (configurationFacing.getOpposite() == structureDirection.getOpposite())
			{
				currentFacing = currentFacing.getOpposite();
			}
			else if (configurationFacing.getOpposite() == structureDirection.rotateYCCW())
			{
				currentFacing = currentFacing.rotateYCCW();
			}
		}
		
		return currentFacing;
	}
	
	private static Comparable setComparable(Comparable<?> comparable, Block foundBlock, IProperty<?> property, StructureConfiguration configuration, BuildBlock block, Direction assumedNorth, Optional<?> propertyValue
			, Direction vineFacing, EnumAxis logFacing, Axis boneFacing, BlockQuartz.EnumType quartzFacing, EnumOrientation leverOrientation, Structure structure)
	{
		if (property.getName().equals("facing") && !(foundBlock instanceof BlockLever))
		{
			// Facing properties should be relative to the configuration facing.
			Direction facing = Direction.byName(propertyValue.get().toString());
			
			// Cannot rotate verticals.
			facing = BuildBlock.getHorizontalFacing(facing, configuration.houseFacing, structure.getClearSpace().getShape().getDirection());
			
			comparable = facing;
			
			block.setHasFacing(true);
		}
		else if (property.getName().equals("facing") && foundBlock instanceof BlockLever)
		{
			comparable = leverOrientation;
			block.setHasFacing(true);
		}
		else if (property.getName().equals("rotation"))
		{
			// 0 = South
			// 4 = West
			// 8 = North
			// 12 = East
			int rotation = (Integer)propertyValue.get();
			Direction facing = rotation == 0 ? Direction.SOUTH : rotation == 4 ? Direction.WEST : rotation == 8 ? Direction.NORTH : Direction.EAST;
			
			if (configuration.houseFacing.getOpposite() == structure.getClearSpace().getShape().getDirection().rotateY())
			{
				facing = facing.rotateY();
			}
			else if (configuration.houseFacing.getOpposite() == structure.getClearSpace().getShape().getDirection().getOpposite())
			{
				facing = facing.getOpposite();
			}
			else if (configuration.houseFacing.getOpposite() == structure.getClearSpace().getShape().getDirection().rotateYCCW())
			{
				facing = facing.rotateYCCW();
			}
			
			rotation = facing == Direction.SOUTH ? 0 : facing == Direction.WEST ? 4 : facing == Direction.NORTH ? 8 : 12;
			comparable = rotation;
			block.setHasFacing(true);
		}
		else if (foundBlock instanceof BlockVine)
		{
			// Vines have a special state. There is 1 property for each "facing".
			if (property.getName().equals(vineFacing.getName2()))
			{
				comparable = true;
				block.setHasFacing(true);
			}
			else
			{
				comparable = false;
			}
		}
		else if (foundBlock instanceof BlockWall)
		{
			if (!property.getName().equals("variant"))
			{
				if (property.getName().equals(vineFacing.getName2())
						|| property.getName().equals(vineFacing.getOpposite().getName2()))
				{
					comparable = true;
					block.setHasFacing(true);
				}
				else
				{
					comparable = false;
				}
			}
		}
		else if (foundBlock instanceof BlockLog)
		{
			// logs have a special state. There is a property called axis and it only has 3 directions.
			if (property.getName().equals("axis"))
			{
				comparable = logFacing;
			}
		}
		else if (foundBlock instanceof BlockBone)
		{
			// bones have a special state. There is a property called axis and it only has 3 directions.
			if (property.getName().equals("axis"))
			{
				comparable = boneFacing;
			}
		}
		else if (foundBlock instanceof BlockQuartz)
		{
			if (property.getName().equals("variant") && quartzFacing != BlockQuartz.EnumType.DEFAULT)
			{
				comparable = quartzFacing;
			}
		}
		
		return comparable;
	}
	
	private static Direction getVineFacing(StructureConfiguration configuration, Block foundBlock, BuildBlock block, Direction assumedNorth)
	{
		Direction vineFacing = Direction.UP;
		
		// Vines have a special property for it's "facing"
		if (foundBlock instanceof BlockVine
				|| foundBlock instanceof BlockWall)
		{
			if (block.getProperty("east").getValue().equals("true"))
			{
				vineFacing = Direction.EAST;
			}
			else if (block.getProperty("west").getValue().equals("true"))
			{
				vineFacing = Direction.WEST;
			}
			else if (block.getProperty("south").getValue().equals("true"))
			{
				vineFacing = Direction.SOUTH;
			}
			else if (block.getProperty("north").getValue().equals("true"))
			{
				vineFacing = Direction.NORTH;
			}
			
			if (vineFacing != Direction.UP)
			{
				if (configuration.houseFacing.rotateY() == assumedNorth)
				{
					vineFacing = vineFacing.rotateY();
				}
				else if (configuration.houseFacing.getOpposite() == assumedNorth)
				{
				}
				else if (configuration.houseFacing.rotateYCCW() == assumedNorth)
				{
					vineFacing = vineFacing.rotateYCCW();
				}
				else
				{
					vineFacing = vineFacing.getOpposite();
				}
			}
		}
		
		return vineFacing;
	}
	
	private static EnumAxis getLogFacing(StructureConfiguration configuration, Block foundBlock, BuildBlock block, Direction assumedNorth)
	{
		EnumAxis logFacing = EnumAxis.X;
		
		if (foundBlock instanceof BlockLog)
		{
			if (block.getProperty("axis").getValue().equals("x"))
			{
				logFacing = EnumAxis.X;
			}
			else if (block.getProperty("axis").getValue().equals("y"))
			{
				logFacing = EnumAxis.Y;
			}
			else
			{
				logFacing = EnumAxis.Z;
			}
			
			if (logFacing != EnumAxis.Y)
			{
				logFacing = 
						configuration.houseFacing == assumedNorth || configuration.houseFacing == assumedNorth.getOpposite() 
							? logFacing : 
						logFacing == EnumAxis.X 
							? EnumAxis.Z : EnumAxis.X; 
			}
		}
		
		return logFacing;
	}
	
	private static Axis getBoneFacing(StructureConfiguration configuration, Block foundBlock, BuildBlock block, Direction assumedNorth)
	{
		Axis boneFacing = Axis.X;
		
		if (foundBlock instanceof BlockBone)
		{
			if (block.getProperty("axis").getValue().equals("x"))
			{
				boneFacing = Axis.X;
			}
			else if (block.getProperty("axis").getValue().equals("y"))
			{
				boneFacing = Axis.Y;
			}
			else
			{
				boneFacing = Axis.Z;
			}
			
			if (boneFacing != Axis.Y)
			{
				boneFacing = 
						configuration.houseFacing == assumedNorth || configuration.houseFacing == assumedNorth.getOpposite() 
							? boneFacing : 
								boneFacing == Axis.X 
									? Axis.Z : Axis.X; 
			}
		}
		
		return boneFacing;
	}
	
	private static BlockQuartz.EnumType getQuartsFacing(StructureConfiguration configuration, Block foundBlock, BuildBlock block, Direction assumedNorth)
	{
		BlockQuartz.EnumType quartzFacing = BlockQuartz.EnumType.DEFAULT;
		
		if (foundBlock instanceof BlockQuartz)
		{
			if (block.getProperty("variant").getValue().startsWith("lines"))
			{
				if (block.getProperty("variant").getValue().equals("lines_x"))
				{
					quartzFacing = BlockQuartz.EnumType.LINES_X;
				}
				else if (block.getProperty("variant").getValue().equals("lines_z"))
				{
					quartzFacing = BlockQuartz.EnumType.LINES_Z;
				}
				
				if (quartzFacing == BlockQuartz.EnumType.LINES_X
						|| quartzFacing == BlockQuartz.EnumType.LINES_Z)
				{
					quartzFacing = 
							configuration.houseFacing == assumedNorth || configuration.houseFacing.getOpposite() == assumedNorth 
								? quartzFacing : 
									quartzFacing == BlockQuartz.EnumType.LINES_X 
								? BlockQuartz.EnumType.LINES_Z : BlockQuartz.EnumType.LINES_X;
				}
			}
		}
		
		return quartzFacing;
	}
	
	private static Direction getLeverOrientation(StructureConfiguration configuration, Block foundBlock, BuildBlock block, Direction assumedNorth)
	{
		Direction leverOrientation = Direction.NORTH;
		
		if (foundBlock instanceof LeverBlock)
		{
			// Levers have a special facing.
			leverOrientation = LeverBlock.HORIZONTAL_FACING.parseValue(block.getProperty("facing").getValue().toUpperCase());
			
			if (leverOrientation == Direction.DOWN
					|| leverOrientation == Direction.UP)
			{
				if (leverOrientation == Direction.DOWN)
				{
					leverOrientation = 
							configuration.houseFacing == assumedNorth || configuration.houseFacing == assumedNorth.getOpposite() 
								? leverOrientation : 
									leverOrientation == Orientation.DOWN_X 
								? Orientation.DOWN_Z : Orientation.DOWN_X;
				}
				else
				{
					leverOrientation = 
							configuration.houseFacing == assumedNorth || configuration.houseFacing == assumedNorth.getOpposite() 
								? leverOrientation : 
									leverOrientation == Orientation.UP_X 
								? Orientation.UP_Z : Orientation.UP_X;
				}
			}
			else
			{
				Direction facing = leverOrientation;
				
				if (configuration.houseFacing.rotateY() == assumedNorth)
				{				
					facing = facing.rotateY();
				}
				else if (configuration.houseFacing.getOpposite() == assumedNorth)
				{
				}
				else if (configuration.houseFacing.rotateYCCW() == assumedNorth)
				{
					facing = facing.rotateYCCW();
				}
				else
				{
					facing = facing.getOpposite();
				}
				
				for (Orientation tempOrientation : Orientation.values())
				{
					if (tempOrientation.getFacing() == facing)
					{
						leverOrientation = tempOrientation;
						break;
					}
				}
			}
		}
		
		return leverOrientation;
	}
	
	private static BlockState setProperty(BlockState state, IProperty property, Comparable comparable)
	{
		// This method is required since the properties and comparables have a <?> in them and it doesn't work properly when that is there. There is a compilation error since it's not hard typed.
		return state.with(property, comparable);
	}

	private static BuildBlock SetBlockStateFromTagData(StructureConfiguration configuration, World world, BlockPos originalPos, Direction assumedNorth, BuildBlock block, Block foundBlock, BlockState blockState, Structure structure)
	{
		BlockState tagState = block.getBlockStateFromDataTag();
		
		if (tagState != null)
		{
			block.setBlockState(block.getBlockStateFromDataTag());
		}
		else
		{
			block.setBlockStateData("");
			return BuildBlock.SetBlockState(configuration, world, originalPos, assumedNorth, block, foundBlock, blockState, structure);
		}
		
		return block;
	}
}