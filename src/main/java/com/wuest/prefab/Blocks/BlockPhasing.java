package com.wuest.prefab.Blocks;

import com.wuest.prefab.Blocks.BlockPaperLantern.SeeThroughMaterial;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

/**
 * This is a phasing block.
 * @author WuestMan
 *
 */
public class BlockPhasing extends Block
{
	/**
	 * The see through material for this block.
	 */
	public static SeeThroughMaterial BlockMaterial = new SeeThroughMaterial(MapColor.AIR).setTranslucent(true).setImmovable(true);
	
	/**
	 * The phasing progress property.
	 */
	public static final PropertyEnum<EnumPhasingProgress> Phasing_Progress = PropertyEnum.<EnumPhasingProgress>create("phasing_progress", EnumPhasingProgress.class);
	
	/**
	 * The phasing out block property.
	 */
	public static final PropertyBool Phasing_Out = PropertyBool.create("phasing_out");
	
	/**
	 * The empty collision box value.
	 */
	public static final AxisAlignedBB Empty_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
	
	/**
	 * The tick rage for this block.
	 */
	protected int tickRate = 2;
	
	/**
	 * Initializes a new instance of the BlockPhasing class.
	 * @param name The name to register the block as.
	 */
	public BlockPhasing(String name)
	{
		super(BlockPhasing.BlockMaterial);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setSoundType(SoundType.STONE);
		this.setHardness(0.6F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(Phasing_Out, false).withProperty(Phasing_Progress, EnumPhasingProgress.base));
		
		ModRegistry.setBlockName(this, name);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote) 
		{
			EnumPhasingProgress progress = state.getValue(Phasing_Progress);
			
			if (progress == EnumPhasingProgress.base)
			{
				// Only trigger the phasing when this block is not currently phasing.
				world.scheduleUpdate(pos, this, this.tickRate);
			}
		}

		return true;
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
     * @param stack The stack being used to place this block
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
    		this.updateNeighborPhasicBlocks(true, world, pos, this.getDefaultState(), false, false);
    	}
    	
    	return this.getDefaultState().withProperty(Phasing_Out, poweredSide).withProperty(Phasing_Progress, EnumPhasingProgress.base);
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
        EnumPhasingProgress currentState = state.getValue(Phasing_Progress);
        
        if (poweredSide && currentState == EnumPhasingProgress.transparent)
        {
        	// Set this block and all neighbor Phasic Blocks to base. This will cascade to tall touching Phasic blocks.
        	this.updateNeighborPhasicBlocks(false, worldIn, pos, state, false, false);
        }
    }

	/**
	 * How many world ticks before ticking
	 */
	@Override
	public int tickRate(World worldIn)
	{
		return this.tickRate;
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
		        EnumPhasingProgress currentState = state.getValue(Phasing_Progress);
		        boolean setToTransparent = false;
		        
		        if (poweredSide && currentState == EnumPhasingProgress.base)
		        {
		        	setToTransparent = true;
		        }
		        
		        if (currentState == EnumPhasingProgress.base || currentState == EnumPhasingProgress.transparent)
		        {
		        	this.updateNeighborPhasicBlocks(setToTransparent, worldIn, pos, state, true, true);
		        }
			}
		}
    }
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		int tickDelay = this.tickRate;
		
		if (ModEventHandler.RedstoneAffectedBlockPositions.contains(pos))
		{
			return;
		}
		
		EnumPhasingProgress progress = state.getValue(Phasing_Progress);
		boolean phasingOut = state.getValue(Phasing_Out);
		
		// If the state is at base, progress trigger the phasing out to the neighboring blocks.
		if (progress == EnumPhasingProgress.base)
		{
			for (EnumFacing facing : EnumFacing.VALUES)
			{
				Block currentBlock = worldIn.getBlockState(pos.offset(facing)).getBlock();
				
				if (currentBlock instanceof BlockPhasing && !ModEventHandler.RedstoneAffectedBlockPositions.contains(pos.offset(facing)))
				{
					worldIn.scheduleUpdate(pos.offset(facing), currentBlock, tickDelay);
				}
			}
			
			phasingOut = true;
		}
		
		int updatedMeta = progress.getMeta();
		
		if (updatedMeta == EnumPhasingProgress.eighty_percent.getMeta()
				&& phasingOut)
		{
			// This next phase should take 100 ticks (5 seconds) since this is the phase out.
			tickDelay = 100;
		}
		
		if (updatedMeta == EnumPhasingProgress.transparent.getMeta()
				&& phasingOut)
		{
			// set the phasing to in.
			phasingOut = false;
		}
		
		if (updatedMeta == EnumPhasingProgress.twenty_percent.getMeta()
				&& !phasingOut)
		{
			// Phasing in for this delay, set the tick delay to -1 and stop the phasing. Reset the phasing out property.
			tickDelay = -1;
		}
		
		updatedMeta = phasingOut ? updatedMeta + 2 : updatedMeta - 2;
		progress = EnumPhasingProgress.ValueOf(updatedMeta);
		
		// Update the state in the world, update the world and (possibly) schedule a state update.
		state = state.withProperty(Phasing_Out, phasingOut).withProperty(Phasing_Progress, progress);
		worldIn.setBlockState(pos, state);
		worldIn.markBlockRangeForRenderUpdate(pos, pos);
		
		if (tickDelay > 0)
		{
			worldIn.scheduleUpdate(pos, this, tickDelay);
		}
	}
	
    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    @Override
	public int damageDropped(IBlockState state)
    {
        return EnumPhasingProgress.base.getMeta();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
    	if (meta % 2 == 0)
    	{
    		// Not phasing
    		return this.getDefaultState().withProperty(Phasing_Out, false).withProperty(Phasing_Progress, EnumPhasingProgress.ValueOf(meta));
    	}
    	
        return this.getDefaultState().withProperty(Phasing_Out,  true).withProperty(Phasing_Progress, EnumPhasingProgress.ValueOf(meta - 1));
    }
    
    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
    	int phasing = state.getValue(Phasing_Out) ? 1 : 0;
        return ((EnumPhasingProgress)state.getValue(Phasing_Progress)).getMeta() + phasing;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {Phasing_Out, Phasing_Progress});
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
	/**
	* Queries if this block should render in a given layer.
<<<<<<< HEAD
	* ISmartBlockModel can use {@link MinecraftForgeClient#getRenderLayer()} to alter their model based on layer.
=======
	* ISmartBlockModel can use MinecraftForgeClient#getRenderLayer() to alter their model based on layer.
>>>>>>> refs/remotes/origin/MC-1.10.2
	*/
    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
    	EnumPhasingProgress progress = state.getValue(Phasing_Progress);
    	
    	if ((layer == BlockRenderLayer.TRANSLUCENT && progress != EnumPhasingProgress.base)
    			|| (layer == BlockRenderLayer.SOLID && progress == EnumPhasingProgress.base))
    	{
    		return true;
    	}

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
    	EnumPhasingProgress progress = blockState.getValue(Phasing_Progress);
    	
    	if (progress == EnumPhasingProgress.transparent)
    	{
    		return Empty_AABB;
    	}
        
    	return super.getCollisionBoundingBox(blockState, worldIn, pos);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
    	EnumPhasingProgress progress = state.getValue(Phasing_Progress);
    	
    	if (progress == EnumPhasingProgress.transparent)
    	{
    		return Empty_AABB;
    	}
    	
    	AxisAlignedBB aabb = super.getBoundingBox(state, source, pos);
    	
    	//aabb = new AxisAlignedBB(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX + 0.001, aabb.maxY + 0.001, aabb.maxZ + 0.001);
    	
    	return aabb;
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
    
    protected void updateNeighborPhasicBlocks(boolean setToTransparent, World worldIn, BlockPos pos, IBlockState phasicBlockState, boolean setCurrentBlock, boolean triggeredByRedstone)
    {
    	ArrayList<BlockPos> blocksToUpdate = new ArrayList<BlockPos>();
    	IBlockState updatedBlockState = phasicBlockState
    			.withProperty(Phasing_Out, setToTransparent)
    			.withProperty(Phasing_Progress, setToTransparent ? EnumPhasingProgress.transparent : EnumPhasingProgress.base);
    	
    	// Set this block and all neighbor Phasic Blocks to transparent. This will cascade to all touching Phasic blocks.
    	this.findNeighborPhasicBlocks(setToTransparent, worldIn, pos, updatedBlockState, 0, blocksToUpdate, setCurrentBlock);
    	
    	for (BlockPos positionToUpdate : blocksToUpdate)
    	{
    		worldIn.setBlockState(positionToUpdate, updatedBlockState);
    		
    		if (triggeredByRedstone)
    		{
    			if (ModEventHandler.RedstoneAffectedBlockPositions.contains(positionToUpdate) && !setToTransparent)
    			{
    				ModEventHandler.RedstoneAffectedBlockPositions.remove(positionToUpdate);
    			}
    			else if (!ModEventHandler.RedstoneAffectedBlockPositions.contains(positionToUpdate) && setToTransparent)
    			{
    				ModEventHandler.RedstoneAffectedBlockPositions.add(positionToUpdate);
    			}
    		}
    	}
    }
    
    /**
     * Sets the powered status and updates the block's neighbor.
     * @param setToTransparent Determines if the block should be turned transparent.
     * @param worldIn The world where the block resides.
     * @param pos The position of the block.
     * @param currentBlockPosState The current state of the block at the position.
     * @param cascadeCount The number of times it has cascaded.
     * @param cascadedBlockPos The list of cascaded block positions, this is used to determine if this block should be processed again.
     * @param setCurrentBlock Determines if the current block should be set.
     */
    protected int findNeighborPhasicBlocks(boolean setToTransparent, World worldIn, BlockPos pos, IBlockState desiredBlockState, int cascadeCount, ArrayList<BlockPos> cascadedBlockPos, boolean setCurrentBlock)
    {
    	cascadeCount++;
    	
    	if (cascadeCount > 100)
    	{
    		return cascadeCount;
    	}
    	
    	if (setCurrentBlock)
    	{
    		cascadedBlockPos.add(pos);
    	}
    	
    	for (EnumFacing facing : EnumFacing.values())
    	{
    		Block neighborBlock = worldIn.getBlockState(pos.offset(facing)).getBlock();
    		
    		if (neighborBlock instanceof BlockPhasing)
    		{
    			IBlockState blockState = worldIn.getBlockState(pos.offset(facing));
    			
    			// If the block is already in the correct state or was already checked, there is no need to cascade to it's neighbors.
    			EnumPhasingProgress progress = blockState.getValue(Phasing_Progress);
    			
    			if (cascadedBlockPos.contains(pos.offset(facing)) || progress == desiredBlockState.getValue(Phasing_Progress))
    			{
    				continue;
    			}
    			
    			setCurrentBlock = true;
    			cascadeCount = this.findNeighborPhasicBlocks(setToTransparent, worldIn, pos.offset(facing), desiredBlockState, cascadeCount, cascadedBlockPos, setCurrentBlock);
    			
    			if (cascadeCount > 100)
    			{
    				break;
    			}
    		}
    	}
    	
    	return cascadeCount;
    }

    /**
     * The enum used to determine the meta data for this block. 
     * @author WuestMan
     *
     */
	public enum EnumPhasingProgress implements IStringSerializable
	{
		base(0, "base"),
		twenty_percent(2, "twenty_percent"),
		forty_percent(4, "forty_percent"),
		sixty_percent(6, "sixty_percent"),
		eighty_percent(8, "eighty_percent"),
		transparent(10, "transparent");
		
		private int meta;
		private String name;
		
		private EnumPhasingProgress (int meta, String name)
		{
			this.meta = meta;
			this.name = name;
		}
		
		public int getMeta()
		{
			return this.meta;
		}
		
		/**
		 * Gets a instance based on the meta data.
		 * @param meta The meta data value to get the enum value for.
		 * @return An instance of the enum.
		 */
		public static EnumPhasingProgress ValueOf(int meta)
		{
			for (EnumPhasingProgress progress : EnumPhasingProgress.values())
			{
				if (progress.meta == meta)
				{
					return progress;
				}
			}
			
			return EnumPhasingProgress.base;
		}

		@Override
		public String getName()
		{
			return this.name;
		}
	}
}
