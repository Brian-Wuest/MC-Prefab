package com.wuest.prefab.items;

import com.wuest.prefab.blocks.BlockDoubleGlassSlab;
import com.wuest.prefab.blocks.BlockHalfGlassSlab;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockGlassSlab extends ItemSlab {
    public ItemBlockGlassSlab(Block block, BlockHalfGlassSlab slab, BlockDoubleGlassSlab doubleSlab, Boolean stacked) {
        super(block, slab, doubleSlab);
    }
}
