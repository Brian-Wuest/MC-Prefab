package com.wuest.prefab.StructureGen;

import com.google.gson.annotations.Expose;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * This class represents the offsets for a particular position.
 * @author WuestMan
 */
public class PositionOffset
{
	@Expose
	private int northOffset;
	
	@Expose
	private int southOffset;
	
	@Expose
	private int eastOffset;
	
	@Expose
	private int westOffset;
	
	@Expose
	private int heightOffset;
	
	public PositionOffset()
	{
		this.Initialize();
	}
	
	public int getNorthOffset()
	{
		return this.northOffset;
	}
	
	public void setNorthOffset(int value)
	{
		this.northOffset = value;
	}
	
	public int getSouthOffset()
	{
		return this.southOffset;
	}
	
	public void setSouthOffset(int value)
	{
		this.southOffset = value;
	}
	
	public int getEastOffset()
	{
		return this.eastOffset;
	}
	
	public void setEastOffset(int value)
	{
		this.eastOffset = value;
	}
	
	public int getWestOffset()
	{
		return this.westOffset;
	}
	
	public void setWestOffset(int value)
	{
		this.westOffset = value;
	}
	
	public int getHeightOffset()
	{
		return this.heightOffset;
	}
	
	public void setHeightOffset(int value)
	{
		this.heightOffset = value;
	}
	
	protected void Initialize()
	{
		this.northOffset = 0;
		this.southOffset = 0;
		this.eastOffset = 0;
		this.westOffset = 0;
		this.heightOffset = 0;
	}
	
	public BlockPos getRelativePosition(BlockPos pos, EnumFacing assumedNorth)
	{
		// North offset.
		pos = pos.offset(assumedNorth, this.northOffset);
		
		// East offset.
		pos = pos.offset(assumedNorth.rotateY(), this.eastOffset);
		
		// South offset.
		pos = pos.offset(assumedNorth.getOpposite(), this.southOffset);
		
		// West offset.
		pos = pos.offset(assumedNorth.rotateYCCW(), this.westOffset);
		
		pos = pos.offset(EnumFacing.UP, this.heightOffset);
		
		return pos;
	}
	
	public BlockPos getRelativePosition(BlockPos pos, EnumFacing assumedNorth, EnumFacing configurationFacing)
	{
		EnumFacing facing = this.getRelativeFacing(assumedNorth, configurationFacing);

		for (int i = 0; i < 4; i++)
		{
			switch (facing)
			{
				case EAST:
				{
					pos = pos.offset(facing, this.eastOffset);
					break;
				}
				
				case SOUTH:
				{
					pos = pos.offset(facing, this.southOffset);
					break;
				}
				
				case WEST:
				{
					pos = pos.offset(facing, this.westOffset);
					break;
				}
				
				default:
				{
					pos = pos.offset(facing, this.northOffset);
					break;
				}
			}
			
			facing = facing.rotateY();
		}
		
		pos = pos.offset(EnumFacing.UP, this.heightOffset);
		
		return pos;
	}
	
	public EnumFacing getRelativeFacing(EnumFacing assumedNorth, EnumFacing facing)
	{
		if (facing == assumedNorth.rotateY())
		{
			return EnumFacing.EAST;
		}
		else if (facing == assumedNorth.getOpposite())
		{
			return EnumFacing.SOUTH;
		}
		else if (facing == assumedNorth.rotateYCCW())
		{
			return EnumFacing.WEST;
		}
		else
		{
			return EnumFacing.NORTH;
		}
	}
	
	public void setAppropriateOffSet(EnumFacing assumedNorth, EnumFacing facing, int offset)
	{
		facing = this.getRelativeFacing(assumedNorth, facing);
		
		switch (facing)
		{
			case EAST:
			{
				this.eastOffset = offset;
				break;
			}
			
			case WEST:
			{
				this.westOffset = offset;
				break;
			}
			
			case SOUTH:
			{
				this.southOffset = offset;
				break;
			}
			
			default:
			{
				this.northOffset = offset;
				break;
			}
		}
	}
}