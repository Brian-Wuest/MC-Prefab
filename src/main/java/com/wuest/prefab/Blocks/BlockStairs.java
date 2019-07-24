package com.wuest.prefab.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

/**
 * This class allows custom stairs blocks to be created.
 *
 * @author Brian
 */
public class BlockStairs extends StairsBlock {
    public BlockStairs(BlockState state, Block.Properties properties) {
        super(state, properties);
    }
}
