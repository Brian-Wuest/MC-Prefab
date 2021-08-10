package com.wuest.prefab.blocks;

import com.wuest.prefab.ModRegistry;

import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class BlockDirtSlab extends SlabBlock implements IGrassSpreadable {
    public BlockDirtSlab() {
        super(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).sound(SoundType.GRAVEL)
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
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
        this.DetermineGrassSpread(state, worldIn, pos, random);
    }

    @Override
    public BlockState getGrassBlockState(BlockState originalState) {
        return ModRegistry.GrassSlab.get().defaultBlockState().setValue(SlabBlock.TYPE,
                originalState.getValue(SlabBlock.TYPE));
    }
}
