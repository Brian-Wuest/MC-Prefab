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
public class BlockGraniteStairs extends BlockStairs
{
	/**
	 * Initializes a new instance of the BlockGraniteStairs class.
	 * 
	 * @param name The registered name of this block.
	 */
	public BlockGraniteStairs(String name)
	{
		super(Blocks.STONE.getStateFromMeta(BlockStone.EnumType.GRANITE_SMOOTH.getMetadata()));

		this.setHarvestLevel("pickaxe", 0);
		ModRegistry.setBlockName(this, name);
	}
}