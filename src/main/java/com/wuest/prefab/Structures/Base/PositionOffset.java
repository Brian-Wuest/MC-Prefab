package com.wuest.prefab.Structures.Base;

import com.google.gson.annotations.Expose;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

/**
 * This class represents the offsets for a particular position.
 * 
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

	public int getOffSetValueForFacing(Direction facing)
	{
		switch (facing)
		{
			case DOWN:
			case UP:
			{
				return this.heightOffset;
			}

			case EAST:
			{
				return this.eastOffset;
			}

			case NORTH:
			{
				return this.northOffset;
			}

			case SOUTH:
			{
				return this.southOffset;
			}

			case WEST:
			{
				return this.westOffset;
			}

			default:
			{
				return this.northOffset;
			}
		}
	}

	public BlockPos getRelativePosition(BlockPos pos, Direction assumedNorth, Direction configurationFacing)
	{
		configurationFacing = configurationFacing.getOpposite();
		Direction originalDirection = assumedNorth;

		for (int i = 0; i < 4; i++)
		{
			int offSetValue = this.getOffSetValueForFacing(originalDirection);

			Direction offsetFace = configurationFacing;

			if (configurationFacing == originalDirection.rotateY())
			{
				offsetFace = offsetFace.rotateY();
			}
			else if (configurationFacing == originalDirection.getOpposite())
			{
				offsetFace = offsetFace.getOpposite();
			}
			else if (configurationFacing == originalDirection.rotateYCCW())
			{
				offsetFace = offsetFace.rotateYCCW();
			}

			pos = pos.offset(configurationFacing, offSetValue);

			originalDirection = originalDirection.rotateY();
			configurationFacing = configurationFacing.rotateY();
		}

		pos = pos.offset(Direction.UP, this.heightOffset);

		return pos;
	}

}