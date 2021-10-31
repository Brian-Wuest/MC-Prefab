package com.wuest.prefab.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockPileOfBricks extends Block {
    protected static final VoxelShape AABB = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 5.0D, 12.0D);

    public BlockPileOfBricks(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return BlockPileOfBricks.AABB;
    }
}
