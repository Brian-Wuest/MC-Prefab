package com.wuest.prefab.Base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

/**
 * The base block for any block associated with a tile entity.
 * @author WuestMan
 */
public abstract class TileBlockBase<T extends TileEntityBase> extends Block implements ITileEntityProvider
{
	/**
	 * Initializes a new instance of the TileBlockBase class.
	 * @param materialIn The material associated with this block.
	 */
	public TileBlockBase(Material materialIn)
	{
		super(materialIn);
	}
	
	public TileBlockBase(Material materialIn, MapColor color)
	{
		super(materialIn, color);
	}
	
    @SuppressWarnings ("unchecked")
    public Class<T> getTypeParameterClass()
    {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class<T>) paramType.getActualTypeArguments()[0];
    }
    
	/**
	 * Determine if this block can make a redstone connection on the side provided,
	 * Useful to control which sides are inputs and outputs for redstone wires.
	 *
	 * @param world The current world
	 * @param pos Block position in world
	 * @param side The side that is trying to make the connection, CAN BE NULL
	 * @return True to make the connection
	 */
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		/**
		 * Can this block provide power. Only wire currently seems to have this change based on its state.
		 */
		return this.canProvidePower(state) && side != null;
	}
	
	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}
	
	/**
     * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
     *
     * @param player The player damaging the block, may be null
     * @param meta The block's current metadata
     * @return True to spawn the drops
     */
    @Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return true;
    }
    
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		//System.out.println("Creating new tile entity.");
		return this.createNewTileEntity();
	}
	
    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    @Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
    	// Do not drop items while restoring blockstates, prevents item dupe.
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) 
        {
            List<ItemStack> items = this.getDrops(worldIn, pos, state, fortune);
            chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, chance, false, harvesters.get());

            T tileEntity = this.getLocalTileEntity(worldIn, pos);
            
            for (ItemStack item : items)
            {
                if (worldIn.rand.nextFloat() <= chance)
                {
                	if (tileEntity != null)
                	{
                		item = tileEntity.transferCapabilities(item);
                	}
                	
                    this.spawnAsEntity(worldIn, pos, item);
                }
            }
        }
    }
	
	public T createNewTileEntity()
	{
		try
		{
			return this.getTypeParameterClass().newInstance();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		T tileEntity = this.getLocalTileEntity(worldIn, pos);
		
		this.customBreakBlock(tileEntity, worldIn, pos, state);

		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		for (EnumFacing enumfacing : EnumFacing.values())
		{
			worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
		}

		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));

		if (worldIn.getTileEntity(pos) == null)
		{
			T tile = this.createNewTileEntity();
			
			worldIn.setTileEntity(pos, tile);
		}
	}
	
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam) 
	{
		super.eventReceived(state, worldIn, pos, eventID, eventParam);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		this.updateState(worldIn, pos, state);
	}
	
	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
	 * IBlockstate
	 */
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState();
	}
	
	public void updateState(World worldIn, BlockPos pos, IBlockState state)
	{
		T tileEntity = this.getLocalTileEntity(worldIn, pos);
		
		int tickDelay = this.customUpdateState(worldIn, pos, state, tileEntity);
		
		if (tickDelay > 0)
		{
			worldIn.markBlockRangeForRenderUpdate(pos, pos);
			worldIn.scheduleUpdate(pos, this, tickDelay);
		}
	}
	
	/**
	 * Gets the tile entity at the current position.
	 * @param worldIn The world to the entity for.
	 * @param pos The position in the world to get the entity for.
	 * @return Null if the tile was not found or if one was found and is not a proper tile entity. Otherwise the tile entity instance.
	 */
	public T getLocalTileEntity(World worldIn, BlockPos pos)
	{
		TileEntity entity = worldIn.getTileEntity(pos);

		if (entity != null && entity.getClass() == this.getTypeParameterClass())
		{
			return (T) entity;
		}
		else
		{
			T tileEntity = this.createNewTileEntity();
			
			worldIn.setTileEntity(pos, tileEntity);
			tileEntity.setPos(pos);

			return tileEntity;
		}
	}
	
	/**
	 * Processes custom update state.
	 * @param worldIn The world this state is being updated in.
	 * @param pos The block position.
	 * @param state The block state.
	 * @param tileEntity The tile entity associated with this class.
	 * @return The number of ticks to delay until the next update.
	 */
	public abstract int customUpdateState(World worldIn, BlockPos pos, IBlockState state, T tileEntity);
	
	public abstract void customBreakBlock(T tileEntity, World worldIn, BlockPos pos, IBlockState state);
}