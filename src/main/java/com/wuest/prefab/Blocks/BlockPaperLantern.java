package com.wuest.prefab.Blocks;

import java.util.Random;

import com.wuest.prefab.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This block is used as an alternate light source to be used in the structures created in this mod.
 * @author WuestMan
 *
 */
public class BlockPaperLantern extends Block
{
	/**
	 * The see through material for this block.
	 */
	public static SeeThroughMaterial BlockMaterial = new SeeThroughMaterial(MapColor.AIR).setTranslucent(true);
	
	/**
	 * Initializes a new instance of the BlockPaperLantern class.
	 * @param name The name to register this block as.
	 */
	public BlockPaperLantern(String name)
	{
		super(BlockMaterial);
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
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
	/**
	 * Determines if a torch can be placed on the top surface of this block.
	 * Useful for creating your own block that torches can be on, such as fences.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos Block position in world
	 * @return True to allow the torch to be placed
	 */
	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	@Override
    public boolean isFullCube(IBlockState state)
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
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        EnumFacing enumfacing = EnumFacing.DOWN;
        double d0 = (double)pos.getX() + 0.5D;
        double d1 = (double)pos.getY() + 0.7D;
        double d2 = (double)pos.getZ() + 0.5D;
        double d3 = 0.22D;
        double d4 = 0.27D;

        if (enumfacing.getAxis().isHorizontal())
        {
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.27D * (double)enumfacing1.getFrontOffsetX(), d1 + 0.22D, d2 + 0.27D * (double)enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.27D * (double)enumfacing1.getFrontOffsetX(), d1 + 0.22D, d2 + 0.27D * (double)enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
        }
        else
        {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    /**
     * A simple transparent material which does block movement.
     * The core MaterialTransparent doesn't block movement.
     * @author WuestMan
     *
     */
	public static class SeeThroughMaterial extends Material
	{

		protected boolean translucent;
		protected boolean blocksMovement;
		
		public SeeThroughMaterial(MapColor color)
		{
			super(color);
			this.blocksMovement = true;
		}
		
	    /**
	     * Will prevent grass from growing on dirt underneath and kill any grass below it if it returns true
	     */
	    @Override
		public boolean blocksLight()
	    {
	        return false;
	    }
	    
	    public SeeThroughMaterial setTranslucent(boolean value)
	    {
	    	this.translucent = value;
	    	return this;
	    }
	    
	    /**
	     * Indicate if the material is opaque
	     */
	    @Override
	    public boolean isOpaque()
	    {
	        return this.translucent ? false : this.blocksMovement();
	    }

	    /**
	     * Returns if this material is considered solid or not
	     */
	    @Override
	    public boolean blocksMovement()
	    {
	        return this.blocksMovement;
	    }
	    
	    /**
	     * Sets the blocks movement field.
	     * @param value The new value of the field.
	     * @return This instance for property shortcuts.
	     */
	    public SeeThroughMaterial setBlocksMovement(boolean value)
	    {
	    	this.blocksMovement = value;
	    	return this;
	    }
	    
	    /**
	     * Sets the immovable field.
	     * @param value The new value of the field.
	     * @return This instance for property shortcuts.
	     */
	    public SeeThroughMaterial setImmovable(boolean value)
	    {
	    	return (SeeThroughMaterial)this.setImmovableMobility();
	    }
		
	}
	
}
