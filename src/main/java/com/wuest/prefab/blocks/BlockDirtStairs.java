package com.wuest.prefab.blocks;

import com.wuest.prefab.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

/**
 * This class is used to define a set of dirt stairs.
 *
 * @author WuestMan
 */
public class BlockDirtStairs extends StairBlock implements IGrassSpreadable {
    /**
     * Initializes a new instance of the BlockDirtStairs class.
     */
    public BlockDirtStairs() {
        super(Blocks.DIRT.defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.DIRT));
    }

    /**
     * Returns whether or not this block is of a type that needs random ticking.
     * Called for ref-counting purposes by ExtendedBlockStorage in order to broadly
     * cull a chunk from the random chunk update list for efficiency's sake.
     */
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        this.DetermineGrassSpread(state, worldIn, pos, random);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
    }

    @Override
    public BlockState getGrassBlockState(BlockState originalState) {
        return ModRegistry.GrassStairs.get().defaultBlockState()
                .setValue(StairBlock.FACING, originalState.getValue(StairBlock.FACING))
                .setValue(StairBlock.HALF, originalState.getValue(StairBlock.HALF))
                .setValue(StairBlock.SHAPE, originalState.getValue(StairBlock.SHAPE));
    }
}