package com.prefab.fabric.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockBoundary extends com.prefab.blocks.BlockBoundary {

    public BlockBoundary(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return !state.getValue(Powered);
    }
}
