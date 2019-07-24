package com.wuest.prefab.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.BlockRenderLayer;

public class BlockGlassSlab extends SlabBlock {

    public BlockGlassSlab(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
