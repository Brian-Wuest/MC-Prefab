package com.wuest.prefab.blocks;

import com.wuest.prefab.Prefab;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

/**
 * This block is used as an alternate light source to be used in the structures created in this mod.
 *
 * @author WuestMan
 */
@SuppressWarnings("NullableProblems")
public class BlockPaperLantern extends Block {

    /**
     * Initializes a new instance of the BlockPaperLantern class.
     */
    public BlockPaperLantern() {
        // The "func_226896_b_" function causes the "isSolid" field on the block to be set to false.
        super(Properties.of(Prefab.SeeThroughImmovable)
                .sound(SoundType.SNOW)
                .strength(0.6f)
                .lightLevel(value -> 14)
                .noOcclusion());
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
     * this method will always be called regardless of whether the block can receive random update ticks
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.7D;
        double d2 = (double) pos.getZ() + 0.5D;
        worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        worldIn.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }
}
