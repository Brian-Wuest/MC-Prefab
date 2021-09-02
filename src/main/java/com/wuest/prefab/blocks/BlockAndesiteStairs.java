package com.wuest.prefab.blocks;

import com.wuest.prefab.ModRegistry;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;

/**
 * @author WuestMan
 */
public class BlockAndesiteStairs extends BlockStairs {
    /**
     * Initializes a new instance of the BlockAndesiteStairs class.
     *
     * @param name The registered name of this block.
     */
    public BlockAndesiteStairs(String name) {
        super(Blocks.STONE.getStateFromMeta(BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata()));

        this.setHarvestLevel("pickaxe", 0);
        this.setCreativeTab(ModRegistry.PREFAB_GROUP);
        ModRegistry.setBlockName(this, name);
    }
}