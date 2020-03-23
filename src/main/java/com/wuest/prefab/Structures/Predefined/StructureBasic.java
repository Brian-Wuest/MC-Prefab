package com.wuest.prefab.Structures.Predefined;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.wuest.prefab.Structures.Base.*;
import com.wuest.prefab.Structures.Config.BasicStructureConfiguration;
import com.wuest.prefab.Structures.Config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;

/**
 * This is the basic structure to be used for structures which don't need a lot of configuration or a custom player
 * created structures.
 *
 * @author WuestMan
 */
public class StructureBasic extends Structure {
	private BlockPos customBlockPos = null;
	private ArrayList<Tuple<BlockPos, BlockPos>> bedPositions = new ArrayList<>();

	public static void ScanStructure(World world, BlockPos originalPos, Direction playerFacing, BasicStructureConfiguration configuration, boolean includeAir, boolean excludeWater) {
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.setShape(configuration.basicStructureName.getClearShape());
		clearedSpace.setStartingPosition(configuration.basicStructureName.getClearPositionOffset());
		clearedSpace.getShape().setDirection(playerFacing);

		if (!configuration.IsCustomStructure()) {
			BuildShape buildShape = configuration.basicStructureName.getClearShape().Clone();

			// Scanning the structure doesn't contain the starting corner block but the clear does.
			buildShape.setWidth(buildShape.getWidth() - 1);
			buildShape.setLength(buildShape.getLength() - 1);

			PositionOffset offset = configuration.basicStructureName.getClearPositionOffset();

			clearedSpace.getShape().setWidth(clearedSpace.getShape().getWidth());
			clearedSpace.getShape().setLength(clearedSpace.getShape().getLength());

			int downOffset = offset.getHeightOffset() < 0 ? Math.abs(offset.getHeightOffset()) : 0;
			BlockPos cornerPos = originalPos
					.offset(playerFacing.rotateYCCW(), offset.getOffSetValueForFacing(playerFacing.rotateYCCW()))
					.offset(playerFacing, offset.getOffSetValueForFacing(playerFacing))
					.down(downOffset);

			BlockPos otherCorner = cornerPos
					.offset(playerFacing, buildShape.getLength())
					.offset(playerFacing.rotateY(), buildShape.getWidth())
					.up(buildShape.getHeight());

			Structure.ScanStructure(
					world,
					originalPos,
					cornerPos,
					otherCorner,
					"..\\src\\main\\resources\\assets\\prefab\\structures\\" + configuration.basicStructureName.getName() + ".zip",
					clearedSpace,
					playerFacing,
					includeAir,
					excludeWater);
		}
	}

	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
												   Direction assumedNorth, Block foundBlock, BlockState blockState, PlayerEntity player) {
		BasicStructureConfiguration config = (BasicStructureConfiguration) configuration;

		if (foundBlock instanceof HopperBlock && config.basicStructureName.getName().equals(EnumBasicStructureName.AdvancedCoop.getName())) {
			customBlockPos = block.getStartingPosition().getRelativePosition(
					originalPos,
					this.getClearSpace().getShape().getDirection(),
					configuration.houseFacing);
		} else if (foundBlock instanceof TrapDoorBlock && config.basicStructureName.getName().equals(EnumBasicStructureName.MineshaftEntrance.getName())) {
			customBlockPos = block.getStartingPosition().getRelativePosition(
					originalPos,
					this.getClearSpace().getShape().getDirection(),
					configuration.houseFacing);
		} else if (foundBlock instanceof BedBlock) {
			BlockPos bedHeadPosition = block.getStartingPosition().getRelativePosition(originalPos, this.getClearSpace().getShape().getDirection(), configuration.houseFacing);
			BlockPos bedFootPosition = block.getSubBlock().getStartingPosition().getRelativePosition(
					originalPos,
					this.getClearSpace().getShape().getDirection(),
					configuration.houseFacing);

			this.bedPositions.add(new Tuple<>(bedHeadPosition, bedFootPosition));

			return  true;
		}

		return false;
	}

	@Override
	protected Boolean BlockShouldBeClearedDuringConstruction(StructureConfiguration configuration, World world, BlockPos originalPos, Direction assumedNorth, BlockPos blockPos) {
		BasicStructureConfiguration config = (BasicStructureConfiguration) configuration;

		if (config.basicStructureName.getName().equals(EnumBasicStructureName.AquaBase.getName())) {
			BlockState blockState = world.getBlockState(blockPos);
			// Don't clear water blocks for this building.
			return blockState.getMaterial() != Material.WATER;
		}

		return true;
	}

	/**
	 * This method is used after the main building is build for any additional structures or modifications.
	 *
	 * @param configuration The structure configuration.
	 * @param world         The current world.
	 * @param originalPos   The original position clicked on.
	 * @param assumedNorth  The assumed northern direction.
	 * @param player        The player which initiated the construction.
	 */
	@Override
	public void AfterBuilding(StructureConfiguration configuration, ServerWorld world, BlockPos originalPos, Direction assumedNorth, PlayerEntity player) {
		BasicStructureConfiguration config = (BasicStructureConfiguration) configuration;

		if (this.customBlockPos != null) {
			if (config.basicStructureName.getName().equals(EnumBasicStructureName.AdvancedCoop.getName())) {
				// For the advanced chicken coop, spawn 4 chickens above the hopper.
				for (int i = 0; i < 4; i++) {
					ChickenEntity entity = new ChickenEntity(EntityType.CHICKEN, world);
					entity.setPosition(this.customBlockPos.getX(), this.customBlockPos.up().getY(), this.customBlockPos.getZ());
					world.addEntity(entity);
				}
			} else if (config.basicStructureName.getName().equals(EnumBasicStructureName.MineshaftEntrance.getName())) {
				// Build the mineshaft where the trap door exists.
				StructureAlternateStart.PlaceMineShaft(world, this.customBlockPos.down(), configuration.houseFacing, true);
			}

			this.customBlockPos = null;
		}

		if (this.bedPositions.size() > 0) {
			for (Tuple<BlockPos, BlockPos> bedPosition : this.bedPositions) {
				BuildingMethods.PlaceColoredBed(world, bedPosition.getFirst(), bedPosition.getSecond(), config.bedColor);
			}
		}

		if (config.basicStructureName.getName().equals(EnumBasicStructureName.AquaBase.getName())) {
			// Replace the entrance area with air blocks.
			BlockPos airPos = originalPos.up(4).offset(configuration.houseFacing.getOpposite(), 1);

			// This is the first wall.
			world.removeBlock(airPos.offset(configuration.houseFacing.rotateY()), false);
			world.removeBlock(airPos, false);
			world.removeBlock(airPos.offset(configuration.houseFacing.rotateYCCW()), false);

			airPos = airPos.down();
			world.removeBlock(airPos.offset(configuration.houseFacing.rotateY()), false);
			world.removeBlock(airPos, false);
			world.removeBlock(airPos.offset(configuration.houseFacing.rotateYCCW()), false);

			airPos = airPos.down();
			world.removeBlock(airPos.offset(configuration.houseFacing.rotateY()), false);
			world.removeBlock(airPos, false);
			world.removeBlock(airPos.offset(configuration.houseFacing.rotateYCCW()), false);

			airPos = airPos.down();
			world.removeBlock(airPos.offset(configuration.houseFacing.rotateY()), false);
			world.removeBlock(airPos, false);
			world.removeBlock(airPos.offset(configuration.houseFacing.rotateYCCW()), false);

			// Second part of the wall.
			airPos = airPos.offset(configuration.houseFacing.getOpposite()).up();
			world.removeBlock(airPos.offset(configuration.houseFacing.rotateY()), false);
			world.removeBlock(airPos, false);
			world.removeBlock(airPos.offset(configuration.houseFacing.rotateYCCW()), false);

			airPos = airPos.up();
			world.removeBlock(airPos.offset(configuration.houseFacing.rotateY()), false);
			world.removeBlock(airPos, false);
			world.removeBlock(airPos.offset(configuration.houseFacing.rotateYCCW()), false);

			airPos = airPos.up();
			world.removeBlock(airPos, false);
		}
	}
}
