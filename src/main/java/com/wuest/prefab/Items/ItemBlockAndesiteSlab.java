package com.wuest.prefab.Items;

import com.wuest.prefab.Blocks.BlockDoubleAndesiteSlab;
import com.wuest.prefab.Blocks.BlockHalfAndesiteSlab;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockAndesiteSlab extends ItemSlab
{
	public ItemBlockAndesiteSlab(Block block, BlockHalfAndesiteSlab slab, BlockDoubleAndesiteSlab doubleSlab, Boolean stacked)
	{
		super(block, slab, doubleSlab);
	}
}
