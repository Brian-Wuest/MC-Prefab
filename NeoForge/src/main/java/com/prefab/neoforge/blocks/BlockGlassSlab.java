package com.prefab.neoforge.blocks;

import com.prefab.Utils;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.fml.loading.FMLEnvironment;

public class BlockGlassSlab extends com.prefab.blocks.BlockGlassSlab {
    public BlockGlassSlab(Block.Properties properties) {
        super(properties);
    }


    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        if (!FMLEnvironment.getDist().isClient()) {
            super.skipRendering(state, adjacentBlockState, side);
        }

        Block adjacentBlock = adjacentBlockState.getBlock();
        boolean foundBlock = Utils.doesBlockStateHaveTag(
                adjacentBlockState, Identifier.parse("c:glass_blocks"));

		/*
			Hide this side under the following conditions
			1. The other block is a "Glass" block (this includes colored glass).
			2. This block and the other block has a matching type.
			3. The other block is a double slab and this is a single slab.
		*/
        return foundBlock || (adjacentBlock == this
                && (adjacentBlockState.getValue(SlabBlock.TYPE) == state.getValue(SlabBlock.TYPE)
                || (adjacentBlockState.getValue(SlabBlock.TYPE) == SlabType.DOUBLE
                && state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE)));
    }
}