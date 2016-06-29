package com.wuest.prefab.StructureGen;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * This class represents the offsets for a particular position.
 * @author WuestMan
 */
public class PositionOffset
{
	private int northOffset;
	private int southOffset;
	private int eastOffset;
	private int westOffset;
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
}