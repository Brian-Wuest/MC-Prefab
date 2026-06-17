package com.prefab.blocks;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.BlockPos;

/**
 * This block is used as an alternate light source to be used in the structures created in this mod.
 *
 * @author WuestMan
 */
@SuppressWarnings("NullableProblems")
public class BlockPaperLantern extends Block {

	public static final String BlockName = "block_paper_lantern";

	/**
	 * Initializes a new instance of the BlockPaperLantern class.
	 */
	public BlockPaperLantern(Block.Properties properties) {
		super(properties
				.sound(SoundType.SNOW)
				.strength(0.6f)
				.lightLevel(value -> 14)
				.noOcclusion());
	}

	@Override
	public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return 1.0F;
	}
}
