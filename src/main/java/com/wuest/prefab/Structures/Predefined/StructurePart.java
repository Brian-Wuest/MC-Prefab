package com.wuest.prefab.Structures.Predefined;

import java.util.ArrayList;

import com.wuest.prefab.Structures.Base.BuildBlock;
import com.wuest.prefab.Structures.Base.BuildClear;
import com.wuest.prefab.Structures.Base.Structure;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import com.wuest.prefab.Structures.Config.StructurePartConfiguration;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class StructurePart extends Structure
{
	/*
	 * Initializes a new instance of the StructurePart class.
	 */
	public StructurePart()
	{
		super();
	}

	/**
	 * Creates an instance of the structure after reading from a resource location and converting it from JSON.
	 * 
	 * @return A new instance of this class.
	 */
	public static StructurePart CreateInstance()
	{
		StructurePart structure = new StructurePart();
		return structure;
	}

	/**
	 * This is the main building method for this structure.
	 * 
	 * @param configuration The configuration the user updated.
	 * @param world         The current world.
	 * @param originalPos   The block the user clicked on.
	 * @param assumedNorth  This should always be "NORTH" when the file is based on a scan.
	 * @param player        The player requesting the structure.
	 * @return True if the build can occur, otherwise false.
	 */
	@Override
	public boolean BuildStructure(StructureConfiguration configuration, ServerWorld world, BlockPos originalPos, Direction assumedNorth, PlayerEntity player)
	{
		StructurePartConfiguration specificConfig = (StructurePartConfiguration) configuration;

		this.setClearSpace(new BuildClear());

		this.setupStructure(world, specificConfig, originalPos);

		return super.BuildStructure(specificConfig, world, originalPos, assumedNorth, player);
	}

	public void setupStructure(World world, StructurePartConfiguration configuration, BlockPos originalPos)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
		BlockState materialState = configuration.partMaterial.getBlockType();
		Direction facing = Direction.SOUTH;

		switch (configuration.style)
		{
			case Frame:
			{
				buildingBlocks = this.setupFrame(configuration, originalPos, materialState, facing);
				break;
			}

			case Gate:
			{
				buildingBlocks = this.setupGate(configuration, originalPos, materialState, facing);
				break;
			}

			case Stairs:
			{
				buildingBlocks = this.setupStairs(configuration, originalPos, configuration.stairsMaterial.stairsState, facing);
				break;
			}

			case Wall:
			{
				buildingBlocks = this.setupWall(configuration, originalPos, materialState, facing);
				break;
			}

			case DoorWay:
			{
				buildingBlocks = this.setupDoorway(configuration, originalPos, materialState, facing);
				break;
			}

			case Floor:
			{
				buildingBlocks = this.setupFloor(configuration, originalPos, materialState, facing);
				break;
			}

			case Roof:
			{
				buildingBlocks = this.setupRoof(configuration, originalPos, configuration.stairsMaterial.stairsState, facing);
				break;
			}

			default:
			{
				break;
			}
		}

		this.setBlocks(buildingBlocks);
	}

	private ArrayList<BuildBlock> setupFrame(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
		int width = configuration.generalWidth - 1;
		int height = configuration.generalHeight - 1;

		// Get all 8 Corners
		BlockPos lowerNearLeft = originalPos.west((int) (configuration.generalWidth) / 2);
		BlockPos upperNearLeft = lowerNearLeft.up(height);
		BlockPos lowerFarLeft = lowerNearLeft.north(width);
		BlockPos upperFarLeft = lowerNearLeft.north(width).up(height);
		BlockPos lowerNearRight = lowerNearLeft.east(width);
		BlockPos upperNearRight = lowerNearRight.up(height);
		BlockPos lowerFarRight = lowerNearRight.north(width);
		BlockPos upperFarRight = lowerNearRight.north(width).up(height);

		// Now make ALL connections.
		this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerNearLeft, lowerFarLeft);
		this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerNearLeft, lowerNearRight);
		this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerNearLeft, upperNearLeft);
		this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerFarLeft, lowerFarRight);
		this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerFarLeft, upperFarLeft);
		this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerNearRight, lowerFarRight);
		this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerNearRight, upperNearRight);
		this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerFarRight, upperFarRight);
		this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, upperNearLeft, upperNearRight);
		this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, upperNearLeft, upperFarLeft);
		this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, upperFarRight, upperFarLeft);
		this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, upperFarRight, upperNearRight);

		return buildingBlocks;
	}

	private void makeBlockListForPositions(ArrayList<BuildBlock> buildingBlocks, StructurePartConfiguration configuration, BlockPos originalPos,
		BlockState materialState, Direction facing, BlockPos position1, BlockPos position2)
	{
		for (BlockPos pos : BlockPos.getAllInBoxMutable(position1, position2))
		{
			buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), pos, originalPos));
		}
	}

	private ArrayList<BuildBlock> setupGate(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();

		BlockPos gatePos = null;
		BlockPos gateOriginalPos = originalPos.west((int) (configuration.generalWidth) / 2).up();

		ArrayList<Long> ignoredPositions = new ArrayList<Long>();
		ignoredPositions.add(originalPos.up().toLong());
		ignoredPositions.add(originalPos.up(2).toLong());

		// Only create a 3x3 opening if there are enough blocks for it. Otherwise we are essentially doing nothing.
		if (configuration.generalWidth > 3 && configuration.generalHeight > 3)
		{
			ignoredPositions.add(originalPos.up(3).toLong());
			ignoredPositions.add(originalPos.up().west().toLong());
			ignoredPositions.add(originalPos.up(2).west().toLong());
			ignoredPositions.add(originalPos.up(3).west().toLong());

			ignoredPositions.add(originalPos.up().east().toLong());
			ignoredPositions.add(originalPos.up(2).east().toLong());
			ignoredPositions.add(originalPos.up(3).east().toLong());
		}

		for (int i = 0; i < configuration.generalHeight; i++)
		{
			// Reset gate building position to the starting position up by the
			// height counter.
			gatePos = gateOriginalPos.up(i);

			for (int j = 0; j < configuration.generalWidth; j++)
			{
				if (ignoredPositions.contains(gatePos.toLong()))
				{
					gatePos = gatePos.offset(facing.rotateYCCW());
					continue;
				}

				// j is the north/south counter.
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), gatePos, originalPos));

				gatePos = gatePos.offset(facing.rotateYCCW());
			}
		}

		return buildingBlocks;
	}

	private ArrayList<BuildBlock> setupDoorway(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();

		BlockPos gatePos = null;
		BlockPos gateOriginalPos = originalPos.west((int) (configuration.generalWidth) / 2).up();

		for (int i = 0; i < configuration.generalHeight; i++)
		{
			// Reset gate building position to the starting position up by the
			// height counter.
			gatePos = gateOriginalPos.up(i);

			for (int j = 0; j < configuration.generalWidth; j++)
			{
				if (gatePos.toLong() == originalPos.up().toLong() || gatePos.toLong() == originalPos.up(2).toLong())
				{
					gatePos = gatePos.offset(facing.rotateYCCW());
					continue;
				}

				// j is the north/south counter.
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), gatePos, originalPos));

				gatePos = gatePos.offset(facing.rotateYCCW());
			}
		}

		DoorBlock door = (DoorBlock) Blocks.OAK_DOOR;
		BuildBlock doorBlockBottom = Structure.createBuildBlockFromBlockState(door.getDefaultState(), door, originalPos.up(), originalPos);
		BuildBlock doorBlockTop = Structure.createBuildBlockFromBlockState(door.getDefaultState().with(DoorBlock.HALF, DoubleBlockHalf.UPPER),
			door, originalPos.up(2), originalPos);
		doorBlockBottom.setSubBlock(doorBlockTop);
		buildingBlocks.add(doorBlockBottom);

		return buildingBlocks;
	}

	private ArrayList<BuildBlock> setupStairs(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
		BlockPos stepPos = null;
		BlockPos stepOriginalPos = originalPos.west((int) (configuration.stairWidth - .2) / 2).up();

		for (int i = 0; i < configuration.stairHeight; i++)
		{
			// Reset step building position to the starting position up by the
			// height counter.
			stepPos = stepOriginalPos.up(i).north(i);

			for (int j = 0; j < configuration.stairWidth; j++)
			{
				// j is the north/south counter.
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), stepPos, originalPos));

				stepPos = stepPos.offset(facing.rotateYCCW());
			}
		}

		return buildingBlocks;
	}

	private ArrayList<BuildBlock> setupWall(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
		BlockPos wallPos = null;
		BlockPos wallOriginalPos = originalPos.west((int) (configuration.generalWidth) / 2).up();

		for (int i = 0; i < configuration.generalHeight; i++)
		{
			// Reset wall building position to the starting position up by the
			// height counter.
			wallPos = wallOriginalPos.up(i);

			for (int j = 0; j < configuration.generalWidth; j++)
			{
				// j is the north/south counter.
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), wallPos, originalPos));

				wallPos = wallPos.offset(facing.rotateYCCW());
			}
		}

		return buildingBlocks;
	}

	private ArrayList<BuildBlock> setupFloor(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
		BlockPos floorPos = null;
		BlockPos floorOriginalPos = originalPos.west((int) (configuration.generalWidth) / 2);

		for (int i = 0; i < configuration.generalHeight; i++)
		{
			// Reset wall building position to the starting position up by the
			// height counter.
			floorPos = floorOriginalPos.north(i);

			for (int j = 0; j < configuration.generalWidth; j++)
			{
				// j is the north/south counter.
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), floorPos, originalPos));

				floorPos = floorPos.offset(facing.rotateYCCW());
			}
		}

		return buildingBlocks;
	}

	private ArrayList<BuildBlock> setupRoof(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
		BlockPos wallPos = null;
		BlockPos wallOriginalPos = originalPos.west((int) (configuration.stairWidth) / 2).up();

		// Get the stairs state without the facing since it will change.
		BlockState stateWithoutFacing = materialState.with(StairsBlock.HALF, Half.BOTTOM).with(StairsBlock.SHAPE,
			StairsShape.STRAIGHT);

		int wallWidth = configuration.stairWidth;
		int wallDepth = configuration.stairWidth;
		int height = wallWidth / 2;
		boolean isWider = false;

		if (wallWidth > wallDepth)
		{
			height = wallDepth / 2;
			isWider = true;
		}

		wallPos = wallOriginalPos;

		for (int i = 0; i <= height; i++)
		{
			// I is the vaulted roof level.
			for (int j = 0; j < 4; j++)
			{
				// Default is depth.
				Direction tempFacing = facing.rotateYCCW();
				Direction flowDirection = facing.getOpposite();
				int wallSize = wallDepth;

				switch (j)
				{
					case 1:
					{
						tempFacing = facing;
						flowDirection = facing.rotateYCCW();
						wallSize = wallWidth;
						break;
					}

					case 2:
					{
						tempFacing = facing.rotateY();
						flowDirection = facing;
						wallSize = wallDepth;
						break;
					}

					case 3:
					{
						tempFacing = facing.getOpposite();
						flowDirection = facing.rotateY();
						wallSize = wallWidth;
						break;
					}
				}

				for (int k = 0; k <= wallSize; k++)
				{
					// j is the north/south counter.
					buildingBlocks.add(Structure.createBuildBlockFromBlockState(stateWithoutFacing.with(StairsBlock.FACING, tempFacing),
						materialState.getBlock(), wallPos, originalPos));

					wallPos = wallPos.offset(flowDirection);
				}
			}

			wallPos = wallPos.offset(facing.rotateYCCW()).offset(facing.getOpposite()).up();
			wallWidth = wallWidth - 2;
			wallDepth = wallDepth - 2;
		}

		long wallPosLong = wallPos.down().toLong();

		if (!buildingBlocks.stream().anyMatch(x -> x.blockPos.toLong() == wallPosLong))
		{
			// Create final blocks.
			int finalStoneCount = wallDepth;

			if (isWider)
			{
				finalStoneCount = wallWidth;
			}

			// Add the number of blocks based on the depth/width (minimum 1);
			if (finalStoneCount < 1)
			{
				finalStoneCount = 1;
			}
			else
			{
				finalStoneCount = finalStoneCount + 2;
			}

			for (int i = 0; i < finalStoneCount; i++)
			{
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(configuration.stairsMaterial.getFullBlock(),
					configuration.stairsMaterial.getFullBlock().getBlock(), wallPos, originalPos));

				if (isWider)
				{
					wallPos = wallPos.offset(facing.rotateYCCW());
				}
				else
				{
					wallPos = wallPos.offset(facing.getOpposite());
				}
			}
		}

		return buildingBlocks;
	}
}
