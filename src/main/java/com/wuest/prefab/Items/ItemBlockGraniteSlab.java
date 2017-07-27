package com.wuest.prefab.Items;

import com.wuest.prefab.Blocks.BlockDoubleGraniteSlab;
import com.wuest.prefab.Blocks.BlockHalfGraniteSlab;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockGraniteSlab extends ItemSlab
{
	public ItemBlockGraniteSlab(Block block, BlockHalfGraniteSlab slab, BlockDoubleGraniteSlab doubleSlab, Boolean stacked)
	{
		super(block, slab, doubleSlab);
	}
}
