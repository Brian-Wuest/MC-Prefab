package com.wuest.prefab.Blocks;

import javax.annotation.Nullable;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Blocks.BlockPhasing.EnumPhasingProgress;
import com.wuest.prefab.Events.ModEventHandler;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSmartGlass extends Block
{
	public static final PropertyBool Powered = PropertyBool.create("powered");
	
	public BlockSmartGlass(String name)
	{
		super(BlockPhasing.BlockMaterial);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setSoundType(SoundType.STONE);
		this.setHardness(0.6F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(Powered, false));
		
		ModRegistry.setBlockName(this, name);
	}
	
    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        
        if (ModEventHandler.RedstoneAffectedBlockPositions.contains(pos))
        {
        	ModEventHandler.RedstoneAffectedBlockPositions.remove(pos);
        }
    }
	
    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
		if (!worldIn.isRemote)
		{
			// Only worry about powering blocks.
			if (blockIn.getDefaultState().canProvidePower())
			{
		        boolean poweredSide = worldIn.isBlockPowered(pos);
		        
		        this.setNeighborGlassBlocksPoweredStatus(worldIn, pos, worldIn.getBlockState(pos).getValue(Powered));
			}
		}
    }
	

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
    	if (meta % 2 == 0)
    	{
    		// Not Powered.
    		return this.getDefaultState().withProperty(Powered, false);
    	}
    	
        return this.getDefaultState().withProperty(Powered,  true);
    }
	
    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
    	boolean powered = state.getValue(Powered);
        return powered ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {Powered});
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
    	if (!state.getValue(Powered))
    	{
    		return NULL_AABB;
    	}
    	
    	return super.getBoundingBox(state, source, pos);
    }
    
    @Nullable
    @Override
    protected RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox)
    {
    	// Make sure to check for NULL_AABB since this can happen when the block is phasing out/in.
    	if (boundingBox == NULL_AABB)
    	{
    		return null;
    	}
    	
        return super.rayTrace(pos, start, end, boundingBox);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        AxisAlignedBB axisalignedbb = blockState.getBoundingBox(blockAccess, pos);

        // Make sure to check for NULL_AABB since this can happen when the block is phasing out/in.
        if (axisalignedbb == NULL_AABB)
        {
        	return false;
        }
        
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
    
    protected void setNeighborGlassBlocksPoweredStatus(World world, BlockPos pos, boolean isPowered)
    {
    	IBlockState state = world.getBlockState(pos);
    	
    	world.setBlockState(pos, state.withProperty(Powered, isPowered));
    	
    	for (EnumFacing facing : EnumFacing.values())
    	{
    		Block neighborBlock = world.getBlockState(pos.offset(facing)).getBlock();
    		
    		if (neighborBlock instanceof BlockSmartGlass)
    		{
    			IBlockState blockState = world.getBlockState(pos.offset(facing));
    			
    			// If the block is already in the correct state, there is no need to cascade to it's neighbors.
    			boolean blockPowered = blockState.getValue(Powered);
    			
    			if (blockPowered == isPowered)
    			{
    				continue;
    			}
    			
    			// running this method for the neighbor block will cascade out to it's other neighbors until there are no more Phasic blocks around.
    			((BlockSmartGlass)neighborBlock).setNeighborGlassBlocksPoweredStatus(world, pos.offset(facing), isPowered);
    		}
    	}
    }
}
