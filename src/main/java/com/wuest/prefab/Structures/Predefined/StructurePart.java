package com.wuest.prefab.Structures.Predefined;

import java.util.ArrayList;

import com.wuest.prefab.Structures.Base.BuildBlock;
import com.wuest.prefab.Structures.Base.BuildClear;
import com.wuest.prefab.Structures.Base.BuildingMethods;
import com.wuest.prefab.Structures.Base.Structure;
import com.wuest.prefab.Structures.Config.InstantBridgeConfiguration;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import com.wuest.prefab.Structures.Config.StructurePartConfiguration;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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

	private void setupStructure(StructurePartConfiguration configuration, BlockPos originalPos)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
		IBlockState materialState = configuration.partMaterial.getBlockType();
		EnumFacing facing = EnumFacing.SOUTH;

		switch (configuration.style)
		{
			case Circle:
			{
				buildingBlocks = this.setupCircle(configuration, originalPos, materialState, facing);
				break;
			}

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

			default:
			{
				break;
			}
		}

		this.setBlocks(buildingBlocks);
	}

	private ArrayList<BuildBlock> setupCircle(StructurePartConfiguration configuration, BlockPos originalPos, IBlockState materialState, EnumFacing facing)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();

		return buildingBlocks;
	}

	private ArrayList<BuildBlock> setupFrame(StructurePartConfiguration configuration, BlockPos originalPos, IBlockState materialState, EnumFacing facing)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();

		return buildingBlocks;
	}

	private ArrayList<BuildBlock> setupGate(StructurePartConfiguration configuration, BlockPos originalPos, IBlockState materialState, EnumFacing facing)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();

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
		BlockPos wallOriginalPos = originalPos.west((int) (configuration.dimensions.width) / 2).up();

		for (int i = 0; i < configuration.dimensions.height; i++)
		{
			// Reset wall building position to the starting position up by the
			// height counter.
			wallPos = wallOriginalPos.up(i);

			for (int j = 0; j < configuration.dimensions.width; j++)
			{
				// j is the north/south counter.
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), wallPos, originalPos));

				wallPos = wallPos.offset(facing.rotateYCCW());
			}
		}

		return buildingBlocks;
	}
}
