package com.wuest.prefab.Blocks;

import com.wuest.prefab.ModRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author WuestMan
 *
 */
public class BlockDoubleDioriteSlab extends BlockDioriteSlab
{
	@Override
	public boolean isDouble()
	{
		return true;
	}
}