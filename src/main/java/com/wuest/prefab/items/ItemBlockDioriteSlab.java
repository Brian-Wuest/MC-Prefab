package com.wuest.prefab.items;

import com.wuest.prefab.blocks.BlockDoubleDioriteSlab;
import com.wuest.prefab.blocks.BlockHalfDioriteSlab;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockDioriteSlab extends ItemSlab {
    public ItemBlockDioriteSlab(Block block, BlockHalfDioriteSlab slab, BlockDoubleDioriteSlab doubleSlab, Boolean stacked) {
        super(block, slab, doubleSlab);
    }
}
