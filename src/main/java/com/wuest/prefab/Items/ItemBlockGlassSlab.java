package com.wuest.prefab.Items;

import com.wuest.prefab.Blocks.BlockDoubleGlassSlab;
import com.wuest.prefab.Blocks.BlockHalfGlassSlab;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockGlassSlab extends ItemSlab
{
	public ItemBlockGlassSlab(Block block, BlockHalfGlassSlab slab, BlockDoubleGlassSlab doubleSlab, Boolean stacked)
	{
		super(block, slab, doubleSlab);
	}
}
