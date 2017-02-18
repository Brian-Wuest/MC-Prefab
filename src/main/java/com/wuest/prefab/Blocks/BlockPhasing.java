package com.wuest.prefab.Blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Blocks.BlockPaperLantern.SeeThroughMaterial;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This is a phasing block.
 * @author WuestMan
 *
 */
public class BlockPhasing extends Block
{
	public static SeeThroughMaterial BlockMaterial = new SeeThroughMaterial(MapColor.AIR).setTranslucent(true);
	public static final PropertyEnum<EnumPhasingProgress> Phasing_Progress = PropertyEnum.<EnumPhasingProgress>create("phasing_progress", EnumPhasingProgress.class);
	public static final PropertyBool Phasing_Out = PropertyBool.create("phasing_out");
	
	protected int tickRate = 2;
	
	public BlockPhasing(String name)
	{
		super(BlockPhasing.BlockMaterial);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setSoundType(SoundType.SNOW);
		this.setHardness(0.6F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(Phasing_Out, false).withProperty(Phasing_Progress, EnumPhasingProgress.Base));
		
		ModRegistry.setBlockName(this, name);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote) 
		{
			EnumPhasingProgress progress = state.getValue(Phasing_Progress);
			
			if (progress == EnumPhasingProgress.Base)
			{
				// Only trigger the phasing when this block is not currently phasing.
				world.scheduleUpdate(pos, this, this.tickRate);
			}
		}

		return true;
	}

	/**
	 * How many world ticks before ticking
	 */
	@Override
	public int tickRate(World worldIn)
	{
		return this.tickRate;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		int tickDelay = this.tickRate;
		
		EnumPhasingProgress progress = state.getValue(Phasing_Progress);
		boolean phasingOut = state.getValue(Phasing_Out);
		
		// If the state is at base, progress trigger the phasing out to the neighboring blocks.
		if (progress == EnumPhasingProgress.Base)
		{
			for (EnumFacing facing : EnumFacing.VALUES)
			{
				Block currentBlock = worldIn.getBlockState(pos.offset(facing)).getBlock();
				
				if (currentBlock instanceof BlockPhasing)
				{
					worldIn.scheduleUpdate(pos.offset(facing), currentBlock, tickDelay);
				}
			}
			
			phasingOut = true;
		}
		
		int updatedMeta = progress.getMeta();
		
		if (updatedMeta == EnumPhasingProgress.Eighty_Percent.getMeta()
				&& phasingOut)
		{
			// This next phase should take 100 ticks (5 seconds) since this is the phase out.
			tickDelay = 100;
		}
		
		if (updatedMeta == EnumPhasingProgress.Transparent.getMeta()
				&& phasingOut)
		{
			// set the phasing to in.
			phasingOut = false;
		}
		
		if (updatedMeta == EnumPhasingProgress.Twenty_Percent.getMeta()
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
        return EnumPhasingProgress.Base.getMeta();
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
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
    	EnumPhasingProgress progress = blockState.getValue(Phasing_Progress);
    	
    	if (progress == EnumPhasingProgress.Transparent)
    	{
    		return NULL_AABB;
    	}
        
    	return super.getCollisionBoundingBox(blockState, worldIn, pos);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
    	EnumPhasingProgress progress = state.getValue(Phasing_Progress);
    	
    	if (progress == EnumPhasingProgress.Transparent)
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
    	
        Vec3d vec3d = start.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        Vec3d vec3d1 = end.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1);
        return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), raytraceresult.sideHit, pos);
    }
    
    @Deprecated
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
        
        switch (side)
        {
            case DOWN:

                if (axisalignedbb.minY > 0.0D)
                {
                    return true;
                }

                break;
            case UP:

                if (axisalignedbb.maxY < 1.0D)
                {
                    return true;
                }

                break;
            case NORTH:

                if (axisalignedbb.minZ > 0.0D)
                {
                    return true;
                }

                break;
            case SOUTH:

                if (axisalignedbb.maxZ < 1.0D)
                {
                    return true;
                }

                break;
            case WEST:

                if (axisalignedbb.minX > 0.0D)
                {
                    return true;
                }

                break;
            case EAST:

                if (axisalignedbb.maxX < 1.0D)
                {
                    return true;
                }
        }

        return !blockAccess.getBlockState(pos.offset(side)).doesSideBlockRendering(blockAccess, pos.offset(side), side.getOpposite());
    }
    
	public enum EnumPhasingProgress implements IStringSerializable
	{
		Base(0, "base"),
		Twenty_Percent(2, "twenty_percent"),
		Forty_Percent(4, "forty_percent"),
		Sixty_Percent(6, "sixty_percent"),
		Eighty_Percent(8, "eighty_percent"),
		Transparent(10, "transparent");
		
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
		
		public static EnumPhasingProgress ValueOf(int meta)
		{
			for (EnumPhasingProgress progress : EnumPhasingProgress.values())
			{
				if (progress.meta == meta)
				{
					return progress;
				}
			}
			
			return EnumPhasingProgress.Base;
		}

		@Override
		public String getName()
		{
			return this.name;
		}
	}
}
