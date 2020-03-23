package com.wuest.prefab.Structures.Predefined;

import com.wuest.prefab.Structures.Base.BuildBlock;
import com.wuest.prefab.Structures.Base.BuildClear;
import com.wuest.prefab.Structures.Base.BuildingMethods;
import com.wuest.prefab.Structures.Base.Structure;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import com.wuest.prefab.Structures.Config.VillagerHouseConfiguration;
import com.wuest.prefab.Tuple;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;

/**
 * This is the structure class used to generate simple villager houses.
 *
 * @author WuestMan
 */
public class StructureVillagerHouses extends Structure {
	private ArrayList<Tuple<BlockPos, BlockPos>> bedPositions = new ArrayList<>();

	public static void ScanStructure(World world, BlockPos originalPos, Direction playerFacing, VillagerHouseConfiguration.HouseStyle houseStyle) {
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(Direction.SOUTH);
		clearedSpace.getShape().setHeight(houseStyle.getHeight());
		clearedSpace.getShape().setLength(houseStyle.getLength());
		clearedSpace.getShape().setWidth(houseStyle.getWidth());
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(houseStyle.getEastOffSet());

		BlockPos cornerPos = originalPos.south().east(houseStyle.getEastOffSet());
		Structure.ScanStructure(
				world,
				originalPos,
				cornerPos,
				cornerPos.south(houseStyle.getLength()).west(houseStyle.getWidth()).up(houseStyle.getHeight()),
				"../src/main/resources/" + houseStyle.getStructureLocation(),
				clearedSpace,
				playerFacing, false, false);
	}

	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
												   Direction assumedNorth, Block foundBlock, BlockState blockState, PlayerEntity player) {
		if (foundBlock instanceof BedBlock) {
			BlockPos bedHeadPosition = block.getStartingPosition().getRelativePosition(originalPos, this.getClearSpace().getShape().getDirection(), configuration.houseFacing);
			BlockPos bedFootPosition = block.getSubBlock().getStartingPosition().getRelativePosition(
					originalPos,
					this.getClearSpace().getShape().getDirection(),
					configuration.houseFacing);

			this.bedPositions.add(new Tuple<>(bedHeadPosition, bedFootPosition));

			return true;
		}

		return false;
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
		VillagerHouseConfiguration houseConfig = (VillagerHouseConfiguration) configuration;

		if (this.bedPositions.size() > 0) {
			for (Tuple<BlockPos, BlockPos> bedPosition : this.bedPositions) {
				BuildingMethods.PlaceColoredBed(world, bedPosition.getFirst(), bedPosition.getSecond(), houseConfig.bedColor);
			}
		}
	}
}
