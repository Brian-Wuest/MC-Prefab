package com.wuest.prefab.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This class defines a set of grass stairs.
 *
 * @author WuestMan
 */
public class BlockGrassStairs extends StairBlock {
    public BlockGrassStairs() {
        super(Blocks.GRASS_BLOCK.defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK));
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
    }
}
