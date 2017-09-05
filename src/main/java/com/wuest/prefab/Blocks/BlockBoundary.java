package com.wuest.prefab.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Blocks.BlockPhasing.EnumPhasingProgress;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author WuestMan
 *
 */
public class BlockBoundary extends Block
{
	/**
	 * The powered meta data property.
	 */
	public static final PropertyBool Powered = PropertyBool.create("powered");
	
	/**
	 * An empty collision box.
	 */
	public static final AxisAlignedBB Empty_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
	
	/**
	 * Initializes a new instance of the BlockBoundary class.
	 * @param name The name of the block to register.
	 */
	public BlockBoundary(String name)
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
        
        boolean poweredSide = worldIn.isBlockPowered(pos);
        
        if (poweredSide)
        {
        	this.setNeighborGlassBlocksPoweredStatus(worldIn, pos, !poweredSide, 0, new ArrayList<BlockPos>(), false);
        }
    }
	
	/**
	* Queries if this block should render in a given layer.
	* ISmartBlockModel can use {@link MinecraftForgeClient#getRenderLayer()} to alter their model based on layer.
	*/
    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
    	boolean powered = state.getValue(Powered);
    	
    	if ((layer == BlockRenderLayer.TRANSLUCENT && !powered)
    			|| (layer == BlockRenderLayer.SOLID && powered))
    	{
    		return true;
    	}

    	return false;
    }
    
    /**
     * Gets the {@link IBlockState} to place
     * @param world The world the block is being placed in
     * @param pos The position the block is being placed at
     * @param facing The side the block is being placed on
     * @param hitX The X coordinate of the hit vector
     * @param hitY The Y coordinate of the hit vector
     * @param hitZ The Z coordinate of the hit vector
     * @param meta The metadata of {@link ItemStack} as processed by {@link Item#getMetadata(int)}
     * @param placer The entity placing the block
     * @param hand The hand used for the placement.
     * @return The state to be placed in the world
     */
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        /**
         * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
         * IBlockstate
         */
    	boolean poweredSide = world.isBlockPowered(pos);
    	
    	if (poweredSide)
    	{
    		this.setNeighborGlassBlocksPoweredStatus(world, pos, poweredSide, 0, new ArrayList<BlockPos>(), false);
    	}
    	
    	return this.getDefaultState().withProperty(Powered, poweredSide);
    }
	
    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_)
    {
		if (!worldIn.isRemote)
		{
			// Only worry about powering blocks.
			if (blockIn.getDefaultState().canProvidePower())
			{
		        boolean poweredSide = worldIn.isBlockPowered(pos);
		        
		        this.setNeighborGlassBlocksPoweredStatus(worldIn, pos, poweredSide, 0, new ArrayList<BlockPos>(), true);
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
    
    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
    	super.addInformation(stack, player, tooltip, advanced);
    	
    	boolean advancedKeyDown = Minecraft.getMinecraft().currentScreen.isShiftKeyDown();
    	
    	if (!advancedKeyDown)
    	{
    		tooltip.add(GuiLangKeys.translateString(GuiLangKeys.SHIFT_TOOLTIP));
    	}
    	else
    	{
    		tooltip.add(GuiLangKeys.translateString(GuiLangKeys.BOUNDARY_TOOLTIP));
    	}
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
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
    	return FULL_BLOCK_AABB;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
    	if (!state.getValue(Powered))
    	{
    		return Empty_AABB;
    	}
    	
    	return super.getBoundingBox(state, source, pos);
    }
    
    @Nullable
    @Override
    protected RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox)
    {
    	// Make sure to check for NULL_AABB since this can happen when the block is phasing out/in.
    	if (boundingBox == Empty_AABB)
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
        if (axisalignedbb == Empty_AABB)
        {
        	return false;
        }
        
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
    
    /**
     * Sets the neighbor powered status
     * @param world The world where the block resides.
     * @param pos The position of the block.
     * @param isPowered Determines if the block is powered.
     * @param cascadeCount How many times this has been cascaded.
     * @param cascadedBlockPos All of the block positions which have been cascaded too.
     * @param setCurrentBlock Determines if the current block should be set.
     */
    protected void setNeighborGlassBlocksPoweredStatus(World world, BlockPos pos, boolean isPowered, int cascadeCount, ArrayList<BlockPos> cascadedBlockPos, boolean setCurrentBlock)
    {
    	cascadeCount++;
    	
    	if (cascadeCount > 100)
    	{
    		return;
    	}
    	
    	if (setCurrentBlock)
    	{
    		IBlockState state = world.getBlockState(pos);
    		world.setBlockState(pos, state.withProperty(Powered, isPowered));
    	}
    	
    	cascadedBlockPos.add(pos);
    	
    	for (EnumFacing facing : EnumFacing.values())
    	{
    		Block neighborBlock = world.getBlockState(pos.offset(facing)).getBlock();
    		
    		if (neighborBlock instanceof BlockBoundary)
    		{
    			IBlockState blockState = world.getBlockState(pos.offset(facing));
    			
    			// If the block is already in the correct state, there is no need to cascade to it's neighbors.
    			boolean blockPowered = blockState.getValue(Powered);
    			
    			if (cascadedBlockPos.contains(pos.offset(facing)))
    			{
    				continue;
    			}
    			
    			// running this method for the neighbor block will cascade out to it's other neighbors until there are no more Phasic blocks around.
    			((BlockBoundary)neighborBlock).setNeighborGlassBlocksPoweredStatus(world, pos.offset(facing), isPowered, cascadeCount, cascadedBlockPos, true);
    		}
    	}
    }
}
