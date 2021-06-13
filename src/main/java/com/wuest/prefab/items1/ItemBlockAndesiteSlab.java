package com.wuest.prefab.items;

import com.wuest.prefab.blocks.BlockDoubleAndesiteSlab;
import com.wuest.prefab.blocks.BlockHalfAndesiteSlab;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockAndesiteSlab extends ItemSlab {
    public ItemBlockAndesiteSlab(Block block, BlockHalfAndesiteSlab slab, BlockDoubleAndesiteSlab doubleSlab, Boolean stacked) {
        super(block, slab, doubleSlab);
    }
}
