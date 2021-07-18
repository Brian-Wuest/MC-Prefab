package com.wuest.prefab.blocks;

import com.wuest.prefab.ModRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.Random;

/**
 * This class is used to define a set of dirt stairs.
 *
 * @author WuestMan
 */
public class BlockDirtStairs extends StairsBlock implements IGrassSpreadable {
    /**
     * Initializes a new instance of the BlockDirtStairs class.
     */
    public BlockDirtStairs() {
        super(Blocks.DIRT.defaultBlockState(), AbstractBlock.Properties.copy(Blocks.DIRT)
                .harvestTool(ToolType.SHOVEL)
                .harvestLevel(0));
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
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        this.DetermineGrassSpread(state, worldIn, pos, random);
    }

    @Override
    public BlockState getGrassBlockState(BlockState originalState) {
        return ModRegistry.GrassStairs.get().defaultBlockState()
                .setValue(StairsBlock.FACING, originalState.getValue(StairsBlock.FACING))
                .setValue(StairsBlock.HALF, originalState.getValue(StairsBlock.HALF))
                .setValue(StairsBlock.SHAPE, originalState.getValue(StairsBlock.SHAPE));
    }
}