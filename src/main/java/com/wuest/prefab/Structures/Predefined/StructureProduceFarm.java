package com.wuest.prefab.Structures.Predefined;

import com.wuest.prefab.Structures.Base.BuildBlock;
import com.wuest.prefab.Structures.Base.BuildClear;
import com.wuest.prefab.Structures.Base.Structure;
import com.wuest.prefab.Structures.Config.ProduceFarmConfiguration;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class StructureProduceFarm extends Structure {
	public static final String ASSETLOCATION = "assets/prefab/structures/producefarm.zip";

	public static void ScanStructure(World world, BlockPos originalPos, Direction playerFacing) {
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(Direction.SOUTH);
		clearedSpace.getShape().setHeight(9);
		clearedSpace.getShape().setLength(32);
		clearedSpace.getShape().setWidth(32);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(28);
		clearedSpace.getStartingPosition().setHeightOffset(-1);

		Structure.ScanStructure(
				world,
				originalPos,
				originalPos.east(28).south().below(1),
				originalPos.south(32).west(3).above(9),
				"..\\src\\main\\resources\\assets\\prefab\\structures\\producefarm.zip",
				clearedSpace,
				playerFacing, false, false);
	}

	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos, Direction assumedNorth,
												   Block foundBlock, BlockState blockState, PlayerEntity player) {
		if (foundBlock.getRegistryName().getNamespace().equals(Blocks.WHITE_STAINED_GLASS.getRegistryName().getNamespace())
				&& foundBlock.getRegistryName().getPath().endsWith("stained_glass")) {
			ProduceFarmConfiguration wareHouseConfiguration = (ProduceFarmConfiguration) configuration;

			blockState = this.getStainedGlassBlock(wareHouseConfiguration.dyeColor);
			block.setBlockState(blockState);
			// this.placedBlocks.add(block);
			this.priorityOneBlocks.add(block);

			return true;
		}

		return false;
	}
}
