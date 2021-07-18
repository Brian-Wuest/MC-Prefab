package com.wuest.prefab.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraftforge.common.ToolType;

/**
 * This class defines a set of grass stairs.
 *
 * @author WuestMan
 */
public class BlockGrassStairs extends StairsBlock {

    public BlockGrassStairs() {
        super(Blocks.GRASS_BLOCK.defaultBlockState(), AbstractBlock.Properties.copy(Blocks.GRASS_BLOCK)
                .harvestTool(ToolType.SHOVEL)
                .harvestLevel(0));
    }
}
