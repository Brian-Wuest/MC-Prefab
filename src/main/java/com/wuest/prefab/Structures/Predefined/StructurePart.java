package com.wuest.prefab.Structures.Predefined;

import java.util.ArrayList;

import com.wuest.prefab.Structures.Base.BuildBlock;
import com.wuest.prefab.Structures.Base.BuildClear;
import com.wuest.prefab.Structures.Base.Structure;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import com.wuest.prefab.Structures.Config.StructurePartConfiguration;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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
	 * @param world The current world.
	 * @param originalPos The block the user clicked on.
	 * @param assumedNorth This should always be "NORTH" when the file is based on a scan.
	 * @param player The player requesting the structure.
	 * @return True if the build can occur, otherwise false.
	 */
	@Override
	public boolean BuildStructure(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player)
	{
		StructurePartConfiguration specificConfig = (StructurePartConfiguration) configuration;

		this.setClearSpace(new BuildClear());

		this.setupStructure(specificConfig, originalPos);

		return super.BuildStructure(specificConfig, world, originalPos, assumedNorth, player);
	}

	public void setupStructure(StructurePartConfiguration configuration, BlockPos originalPos)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
		IBlockState materialState = configuration.partMaterial.getBlockType();
		EnumFacing facing = EnumFacing.SOUTH;

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
				buildingBlocks = this.setupStairs(configuration, originalPos, materialState, facing);
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
			}

			default:
			{
				break;
			}
		}

		this.setBlocks(buildingBlocks);
	}

	private ArrayList<BuildBlock> setupFrame(StructurePartConfiguration configuration, BlockPos originalPos, IBlockState materialState, EnumFacing facing)
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

	private void makeBlockListForPositions(ArrayList<BuildBlock> buildingBlocks, StructurePartConfiguration configuration,
		BlockPos originalPos, IBlockState materialState, EnumFacing facing, BlockPos position1, BlockPos position2)
	{
		for (BlockPos pos : BlockPos.getAllInBox(position1, position2))
		{
			buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), pos, originalPos));
		}
	}

	private ArrayList<BuildBlock> setupGate(StructurePartConfiguration configuration, BlockPos originalPos, IBlockState materialState, EnumFacing facing)
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

	private ArrayList<BuildBlock> setupDoorway(StructurePartConfiguration configuration, BlockPos originalPos, IBlockState materialState, EnumFacing facing)
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

		BlockDoor door = Blocks.OAK_DOOR;
		BuildBlock doorBlockBottom = Structure.createBuildBlockFromBlockState(door.getDefaultState(), door, originalPos.up(), originalPos);
		BuildBlock doorBlockTop = Structure.createBuildBlockFromBlockState(door.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER),
			door, originalPos.up(2), originalPos);
		doorBlockBottom.setSubBlock(doorBlockTop);
		buildingBlocks.add(doorBlockBottom);

		return buildingBlocks;
	}

	private ArrayList<BuildBlock> setupStairs(StructurePartConfiguration configuration, BlockPos originalPos, IBlockState materialState, EnumFacing facing)
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

	private ArrayList<BuildBlock> setupWall(StructurePartConfiguration configuration, BlockPos originalPos, IBlockState materialState, EnumFacing facing)
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
}
