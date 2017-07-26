package com.wuest.prefab.Items;

import com.wuest.prefab.Blocks.BlockDoubleDioriteSlab;
import com.wuest.prefab.Blocks.BlockHalfDioriteSlab;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockDioriteSlab extends ItemSlab
{
	public ItemBlockDioriteSlab(Block block, BlockHalfDioriteSlab slab, BlockDoubleDioriteSlab doubleSlab, Boolean stacked)
	{
		super(block, slab, doubleSlab);
	}
}
