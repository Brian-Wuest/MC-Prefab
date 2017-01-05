package com.wuest.prefab.StructureGen;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.gson.annotations.Expose;
import com.wuest.prefab.BuildingMethods;
import com.wuest.prefab.Config.StructureConfiguration;

import net.minecraft.block.*;
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.util.*;
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
	private IBlockState state;
	
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
	
	public void Initialize()
	{
		this.blockDomain = "";
		this.blockName = "";
		this.properties = new ArrayList<BuildProperty>();
		this.hasFacing = false;
		this.state = null;
		this.subBlock = null;
		this.startingPosition = new PositionOffset();
	}
	
	public static BuildBlock SetBlockState(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, BuildBlock block, Block foundBlock, IBlockState blockState)
	{
		try
		{
			EnumFacing vineFacing = EnumFacing.UP;
			EnumAxis logFacing = EnumAxis.X;
			BlockQuartz.EnumType quartzFacing = BlockQuartz.EnumType.DEFAULT;
			EnumOrientation leverOrientation = EnumOrientation.NORTH;
			
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
			
			if (foundBlock instanceof BlockLog || foundBlock instanceof BlockBone)
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
			
			// If this block has custom processing for block state just continue onto the next block. The sub-class is expected to place the block.
			if (block.getProperties().size() > 0)
			{
				ImmutableMap<IProperty<?>, Comparable<?>> properties = blockState.getProperties();
				
				// Go through each property of this block and set it.
				// The state will be updated as the properties are
				// applied.
				for (Entry<IProperty<?>, Comparable<?>> set : properties.entrySet())
				{
					IProperty<?> property = set.getKey();
					BuildProperty buildProperty = block.getProperty(property.getName());
					
					try
					{
						Optional<?> propertyValue  = property.parseValue(buildProperty.getValue());
						Comparable<?> comparable = set.getValue().getClass().cast(propertyValue.get());
						
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
						else if (foundBlock instanceof BlockLog || foundBlock instanceof BlockBone)
						{
							// logs have a special state. There is a property called axis and it only has 3 directions.
							if (property.getName().equals("axis"))
							{
								comparable = logFacing;
							}
						}
						else if (foundBlock instanceof BlockQuartz)
						{
							if (property.getName().equals("variant") && quartzFacing != BlockQuartz.EnumType.DEFAULT)
							{
								comparable = quartzFacing;
							}
						}
						
						if (comparable == null)
						{
							continue;
						}
		
						try
						{
							boolean continueCycle = true;
							IBlockState tempBlockState = blockState;
							
							Comparable<?> original = tempBlockState.getValue(property);
							
							while (continueCycle)
							{
								if (tempBlockState.getValue(property) == comparable)
								{
									break;
								}

								tempBlockState = tempBlockState.cycleProperty(property);
								
								if (tempBlockState.getValue(property) == original)
								{
									// The original value was reached after this cycle and no record was found, just break out.
									continueCycle = false;
								}
							}
							
							blockState = tempBlockState;
						}
						catch (Exception ex)
						{
							System.out.println("Error getting properly value for property name [" + property.getName() + "] property value [" + buildProperty.getValue() + "] for block [" + block.getBlockName() + "] The default value will be used.");
						}
					}
					catch (Exception ex)
					{
						System.out.println("Error getting properly value for property name [" + property.getName() + "] property value [" + buildProperty.getValue() + "] for block [" + block.getBlockName() + "]");
						throw ex;
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
}