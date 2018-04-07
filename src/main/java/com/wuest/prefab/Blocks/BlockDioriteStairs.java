package com.wuest.prefab.Blocks;

import com.wuest.prefab.ModRegistry;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;

/**
 * 
 * @author WuestMan
 *
 */
public class BlockDioriteStairs extends BlockStairs
{
	/**
	 * Initializes a new instance of the BlockDioriteStairs class.
	 * 
	 * @param name The registered name of this block.
	 */
	public BlockDioriteStairs(String name)
	{
		super(Blocks.STONE.getStateFromMeta(BlockStone.EnumType.DIORITE_SMOOTH.getMetadata()));

		this.setHarvestLevel("pickaxe", 0);
		ModRegistry.setBlockName(this, name);
	}
}