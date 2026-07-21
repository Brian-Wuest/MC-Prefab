package com.prefab.neoforge.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.loading.FMLEnvironment;


public class BlockBoundary extends com.prefab.blocks.BlockBoundary {

    public BlockBoundary(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        if (FMLEnvironment.dist.isClient()) {
            return !state.getValue(Powered);
        }

        return super.skipRendering(state, adjacentBlockState, side);
    }
}
