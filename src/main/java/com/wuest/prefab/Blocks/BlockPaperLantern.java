package com.wuest.prefab.Blocks;

import java.util.Random;

import com.wuest.prefab.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * This block is used as an alternate light source to be used in the structures created in this mod.
 * 
 * @author WuestMan
 *
 */
public class BlockPaperLantern extends Block
{		
	/**
	 * Initializes a new instance of the BlockPaperLantern class.
	 * 
	 * @param name The name to register this block as.
	 */
	public BlockPaperLantern(String name)
	{
		super(SeeThrough);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setSoundType(SoundType.SNOW);
		this.setHardness(0.6F);

		// Use same light level as a torch.
		this.setLightLevel(0.9375F);
		ModRegistry.setBlockName(this, name);
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(BlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(BlockState state)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public EnumBlockRenderType getRenderType(BlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		Direction Direction = Direction.DOWN;
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 0.7D;
		double d2 = (double) pos.getZ() + 0.5D;
		double d3 = 0.22D;
		double d4 = 0.27D;

		if (Direction.getAxis().isHorizontal())
		{
			Direction Direction1 = Direction.getOpposite();
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.27D * (double) Direction1.getFrontOffsetX(), d1 + 0.22D,
				d2 + 0.27D * (double) Direction1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.27D * (double) Direction1.getFrontOffsetX(), d1 + 0.22D,
				d2 + 0.27D * (double) Direction1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
		}
		else
		{
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}
}
