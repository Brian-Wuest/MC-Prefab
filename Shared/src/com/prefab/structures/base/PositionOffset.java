package com.prefab.structures.base;

import com.google.gson.annotations.Expose;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/**
 * This class represents the offsets for a particular position.
 *
 * @author WuestMan
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class PositionOffset {
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

	public PositionOffset() {
		this.Initialize();
	}

	public void setHorizontalOffset(Direction direction, int value) {
		switch (direction) {
			case EAST: {
				this.setEastOffset(value);
				break;
			}
			case SOUTH: {
				this.setSouthOffset(value);
				break;
			}
			case WEST: {
				this.setWestOffset(value);
				break;
			}
			case NORTH: {
				this.setNorthOffset(value);
				break;
			}
		}
	}

	public int getHorizontalOffset(Direction direction) {
		switch (direction) {
			case EAST: {
				return this.getEastOffset();
			}

			case SOUTH: {
				return this.getSouthOffset();
			}

			case WEST: {
				return this.getWestOffset();
			}

			case NORTH: {
				return this.getNorthOffset();
			}
		}

		return 0;
	}

	public int getNorthOffset() {
		return this.northOffset;
	}

	public PositionOffset setNorthOffset(int value) {
		this.northOffset = value;
		return this;
	}

	public int getSouthOffset() {
		return this.southOffset;
	}

	public PositionOffset setSouthOffset(int value) {
		this.southOffset = value;

		return this;
	}

	public int getEastOffset() {
		return this.eastOffset;
	}

	public PositionOffset setEastOffset(int value) {
		this.eastOffset = value;
		return this;
	}

	public int getWestOffset() {
		return this.westOffset;
	}

	public PositionOffset setWestOffset(int value) {
		this.westOffset = value;

		return this;
	}

	public int getHeightOffset() {
		return this.heightOffset;
	}

	public PositionOffset setHeightOffset(int value) {
		this.heightOffset = value;

		return this;
	}

	protected void Initialize() {
		this.northOffset = 0;
		this.southOffset = 0;
		this.eastOffset = 0;
		this.westOffset = 0;
		this.heightOffset = 0;
	}

	public int getOffSetValueForFacing(Direction facing) {
		switch (facing) {
			case DOWN:
			case UP: {
				return this.heightOffset;
			}

			case EAST: {
				return this.eastOffset;
			}

			case SOUTH: {
				return this.southOffset;
			}

			case WEST: {
				return this.westOffset;
			}

			default: {
				return this.northOffset;
			}
		}
	}

	public BlockPos getRelativePosition(BlockPos pos, Direction assumedNorth, Direction configurationFacing) {
		configurationFacing = configurationFacing.getOpposite();
		Direction originalDirection = assumedNorth;

		for (int i = 0; i < 4; i++) {
			int offSetValue = this.getOffSetValueForFacing(originalDirection);

			pos = pos.relative(configurationFacing, offSetValue);

			originalDirection = originalDirection.getClockWise();
			configurationFacing = configurationFacing.getClockWise();
		}

		pos = pos.relative(Direction.UP, this.heightOffset);

		return pos;
	}

}