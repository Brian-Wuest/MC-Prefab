package com.wuest.prefab.StructureGen;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.wuest.prefab.BuildingMethods;
import com.wuest.prefab.Config.StructureConfiguration;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class defines a single block and where it will be in the structure.
 * @author WuestMan
 */
public class BuildBlock
{
	private String blockDomain;
	private String blockName;
	private PositionOffset startingPosition;
	private ArrayList<BuildProperty> properties;
	private BuildBlock subBlock;
	private boolean hasFacing;
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
				Optional<?> propertyValue = property.parseValue(buildProperty.getValue());
				Comparable<?> comparable = set.getValue().getClass().cast(propertyValue.get());
				
				if (property.getName().equals("facing"))
				{
					// Facing properties should be relative to the configuration facing.
					EnumFacing facing = EnumFacing.byName(propertyValue.get().toString());
					
					// Cannot rotate verticals.
					if (facing != EnumFacing.UP && facing != EnumFacing.DOWN)
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

				ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> table = ((BlockStateContainer.StateImplementation) blockState)
						.getPropertyValueTable();
				ImmutableMap<IProperty<?>, IBlockState> map = table.column(comparable);

				for (Entry<IProperty<?>, IBlockState> mapping : map.entrySet())
				{
					if (property.getName().equals(mapping.getKey().getName()))
					{
						// Found the appropriate mapping for this
						// value and property, update the block
						// state and go to the next property.
						blockState = mapping.getValue();
					}
				}
			}
		}
		
		block.setBlockState(blockState);
		return block;
	}
}