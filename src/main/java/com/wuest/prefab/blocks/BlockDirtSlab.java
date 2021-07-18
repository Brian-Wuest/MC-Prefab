package com.wuest.prefab.blocks;

import com.wuest.prefab.ModRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class BlockDirtSlab extends SlabBlock implements IGrassSpreadable {
    public BlockDirtSlab() {
        super(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.DIRT).sound(SoundType.GRAVEL)
                .strength(0.5f, 0.5f)
                .harvestTool(ToolType.SHOVEL)
                .harvestLevel(0));
    }

    /**
     * Returns whether or not this block is of a type that needs random ticking.
     * Called for ref-counting purposes by ExtendedBlockStorage in order to broadly
     * cull a chunk from the random chunk update list for efficiency's sake.
     */
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        this.DetermineGrassSpread(state, worldIn, pos, random);
    }

    @Override
    public BlockState getGrassBlockState(BlockState originalState) {
        return ModRegistry.GrassSlab.get().defaultBlockState().setValue(SlabBlock.TYPE,
                originalState.getValue(SlabBlock.TYPE));
    }
}
