package com.wuest.prefab.StructureGen;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.base.Optional;
import com.google.gson.annotations.Expose;
import com.wuest.prefab.Config.Structures.StructureConfiguration;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBone;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

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
	private IBlockState state;
	
	@Expose
	private String blockStateData;
	
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
	
	public IBlockState getBlockState()
	{
		return this.state;
	}
	
	public void setBlockState(IBlockState value)
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
	
	public IBlockState getBlockStateFromDataTag()
	{
		IBlockState state = null;
		
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
	
	public static BuildBlock SetBlockState(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, BuildBlock block, Block foundBlock, IBlockState blockState)
	{
		try
		{
			if (!block.blockStateData.equals(""))
			{
				return BuildBlock.SetBlockStateFromTagData(configuration, world, originalPos, assumedNorth, block, foundBlock, blockState);
			}
			
			EnumFacing vineFacing = BuildBlock.getVineFacing(configuration, foundBlock, block, assumedNorth);
			EnumAxis logFacing = BuildBlock.getLogFacing(configuration, foundBlock, block, assumedNorth);
			Axis boneFacing = BuildBlock.getBoneFacing(configuration, foundBlock, block, assumedNorth);
			BlockQuartz.EnumType quartzFacing = BuildBlock.getQuartsFacing(configuration, foundBlock, block, assumedNorth);
			EnumOrientation leverOrientation = BuildBlock.getLeverOrientation(configuration, foundBlock, block, assumedNorth);
			
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
							
							comparable = BuildBlock.setComparable(comparable, foundBlock, property, configuration, block, assumedNorth, propertyValue, vineFacing, logFacing, boneFacing, quartzFacing, leverOrientation);
			
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
	
	private static Comparable setComparable(Comparable<?> comparable, Block foundBlock, IProperty<?> property, StructureConfiguration configuration, BuildBlock block, EnumFacing assumedNorth, Optional<?> propertyValue
			, EnumFacing vineFacing, EnumAxis logFacing, Axis boneFacing, BlockQuartz.EnumType quartzFacing, EnumOrientation leverOrientation)
	{
		if (property.getName().equals("facing") && !(foundBlock instanceof BlockLever))
		{
			// Facing properties should be relative to the configuration facing.
			EnumFacing facing = EnumFacing.byName(propertyValue.get().toString());
			
			// Cannot rotate verticals.
			if (facing != null && facing != EnumFacing.UP && facing != EnumFacing.DOWN)
			{
				if (configuration.houseFacing == assumedNorth.rotateY())
				{				
					facing = facing.rotateY();
				}
				else if (configuration.houseFacing == assumedNorth.getOpposite())
				{
					facing = facing.getOpposite();
				}
				else if (configuration.houseFacing == assumedNorth.rotateYCCW())
				{
					facing = facing.rotateYCCW();
				}
			}
			
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
			EnumFacing facing = rotation == 0 ? EnumFacing.SOUTH : rotation == 4 ? EnumFacing.WEST : rotation == 8 ? EnumFacing.NORTH : EnumFacing.EAST;
			
			if (configuration.houseFacing == assumedNorth.rotateY())
			{
				facing = facing.rotateY();
			}
			else if (configuration.houseFacing == assumedNorth.getOpposite())
			{
				facing = facing.getOpposite();
			}
			else if (configuration.houseFacing == assumedNorth.rotateYCCW())
			{
				facing = facing.rotateYCCW();
			}
			
			rotation = facing == EnumFacing.SOUTH ? 0 : facing == EnumFacing.WEST ? 4 : facing == EnumFacing.NORTH ? 8 : 12;
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
	
	private static EnumFacing getVineFacing(StructureConfiguration configuration, Block foundBlock, BuildBlock block, EnumFacing assumedNorth)
	{
		EnumFacing vineFacing = EnumFacing.UP;
		
		// Vines have a special property for it's "facing"
		if (foundBlock instanceof BlockVine
				|| foundBlock instanceof BlockWall)
		{
			if (block.getProperty("east").getValue().equals("true"))
			{
				vineFacing = EnumFacing.EAST;
			}
			else if (block.getProperty("west").getValue().equals("true"))
			{
				vineFacing = EnumFacing.WEST;
			}
			else if (block.getProperty("south").getValue().equals("true"))
			{
				vineFacing = EnumFacing.SOUTH;
			}
			else if (block.getProperty("north").getValue().equals("true"))
			{
				vineFacing = EnumFacing.NORTH;
			}
			
			if (vineFacing != EnumFacing.UP)
			{
				if (configuration.houseFacing == assumedNorth.rotateY())
				{
					vineFacing = vineFacing.rotateY();
				}
				else if (configuration.houseFacing == assumedNorth.getOpposite())
				{
					vineFacing = vineFacing.getOpposite();
				}
				else if (configuration.houseFacing == assumedNorth.rotateYCCW())
				{
					vineFacing = vineFacing.rotateYCCW();
				}
			}
		}
		
		return vineFacing;
	}
	
	private static EnumAxis getLogFacing(StructureConfiguration configuration, Block foundBlock, BuildBlock block, EnumFacing assumedNorth)
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
	
	private static Axis getBoneFacing(StructureConfiguration configuration, Block foundBlock, BuildBlock block, EnumFacing assumedNorth)
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
	
	private static BlockQuartz.EnumType getQuartsFacing(StructureConfiguration configuration, Block foundBlock, BuildBlock block, EnumFacing assumedNorth)
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
							configuration.houseFacing == assumedNorth || configuration.houseFacing == assumedNorth.getOpposite() 
								? quartzFacing : 
									quartzFacing == BlockQuartz.EnumType.LINES_X 
								? BlockQuartz.EnumType.LINES_Z : BlockQuartz.EnumType.LINES_X;
				}
			}
		}
		
		return quartzFacing;
	}
	
	private static EnumOrientation getLeverOrientation(StructureConfiguration configuration, Block foundBlock, BuildBlock block, EnumFacing assumedNorth)
	{
		EnumOrientation leverOrientation = EnumOrientation.NORTH;
		
		if (foundBlock instanceof BlockLever)
		{
			// Levers have a special facing.
			leverOrientation = BlockLever.EnumOrientation.valueOf(block.getProperty("facing").getValue().toUpperCase());
			
			if (leverOrientation.getFacing() == EnumFacing.DOWN
					|| leverOrientation.getFacing() == EnumFacing.UP)
			{
				if (leverOrientation.getFacing() == EnumFacing.DOWN)
				{
					leverOrientation = 
							configuration.houseFacing == assumedNorth || configuration.houseFacing == assumedNorth.getOpposite() 
								? leverOrientation : 
									leverOrientation == EnumOrientation.DOWN_X 
								? EnumOrientation.DOWN_Z : EnumOrientation.DOWN_X;
				}
				else
				{
					leverOrientation = 
							configuration.houseFacing == assumedNorth || configuration.houseFacing == assumedNorth.getOpposite() 
								? leverOrientation : 
									leverOrientation == EnumOrientation.UP_X 
								? EnumOrientation.UP_Z : EnumOrientation.UP_X;
				}
			}
			else
			{
				EnumFacing facing = leverOrientation.getFacing();
				
				if (configuration.houseFacing == assumedNorth.rotateY())
				{				
					facing = facing.rotateY();
				}
				else if (configuration.houseFacing == assumedNorth.getOpposite())
				{
					facing = facing.getOpposite();
				}
				else if (configuration.houseFacing == assumedNorth.rotateYCCW())
				{
					facing = facing.rotateYCCW();
				}
				
				for (EnumOrientation tempOrientation : EnumOrientation.values())
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
	
	private static IBlockState setProperty(IBlockState state, IProperty property, Comparable comparable)
	{
		// This method is required since the properties and comparables have a <?> in them and it doesn't work properly when that is there. There is a compilation error since it's not hard typed.
		return state.withProperty(property, comparable);
	}

	private static BuildBlock SetBlockStateFromTagData(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, BuildBlock block, Block foundBlock, IBlockState blockState)
	{
		IBlockState tagState = block.getBlockStateFromDataTag();
		
		if (tagState != null)
		{
			block.setBlockState(block.getBlockStateFromDataTag());
		}
		else
		{
			block.setBlockStateData("");
			return BuildBlock.SetBlockState(configuration, world, originalPos, assumedNorth, block, foundBlock, blockState);
		}
		
		return block;
	}
}