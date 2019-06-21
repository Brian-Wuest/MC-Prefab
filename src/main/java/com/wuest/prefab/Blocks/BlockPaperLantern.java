package com.wuest.prefab.Blocks;

import java.util.Random;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * This block is used as an alternate light source to be used in the structures created in this mod.
 * 
 * @author WuestMan
 *
 */
public class BlockPaperLantern extends Block
{
	public final ItemGroup itemGroup;
	
	/**
	 * Initializes a new instance of the BlockPaperLantern class.
	 * 
	 * @param name The name to register this block as.
	 */
	public BlockPaperLantern(String name)
	{
		super(Properties.create(Prefab.SeeThroughImmovable)
			.sound(SoundType.SNOW)
			.hardnessAndResistance(0.6f)
			.lightValue(14));
		
		this.itemGroup = ItemGroup.BUILDING_BLOCKS;

		ModRegistry.setBlockName(this, name);
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isSolid(BlockState state)
	{
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	/**
	 * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
	 * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
	 * of whether the block can receive random update ticks
	*/
	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
	      double d0 = (double)pos.getX() + 0.5D;
	      double d1 = (double)pos.getY() + 0.7D;
	      double d2 = (double)pos.getZ() + 0.5D;
	      worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	      worldIn.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	}
}
