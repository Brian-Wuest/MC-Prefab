package com.wuest.prefab.items;

import com.wuest.prefab.blocks.BlockDoubleGraniteSlab;
import com.wuest.prefab.blocks.BlockHalfGraniteSlab;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockGraniteSlab extends ItemSlab {
    public ItemBlockGraniteSlab(Block block, BlockHalfGraniteSlab slab, BlockDoubleGraniteSlab doubleSlab, Boolean stacked) {
        super(block, slab, doubleSlab);
    }
}
