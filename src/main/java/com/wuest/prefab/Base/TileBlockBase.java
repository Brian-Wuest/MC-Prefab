package com.wuest.prefab.Base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * The base block for any block associated with a tile entity.
 * 
 * @author WuestMan
 */
public abstract class TileBlockBase<T extends TileEntityBase> extends Block implements ITileEntityProvider
{
	/**
	 * Initializes a new instance of the TileBlockBase class.
	 * 
	 * @param properties The material associated with this block.
	 */
	public TileBlockBase(Block.Properties properties)
	{
		super(properties);
	}

	/**
	 * This allows the class to get an generic class.
	 * 
	 * @return The generic type class for this block.
	 */
	public Class<T> getTypeParameterClass()
	{
		Type type = this.getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) type;
		return (Class<T>) paramType.getActualTypeArguments()[0];
	}

	/**
	 * Determines if this block can provide power.
	 * 
	 * @param state The block state (not used, can be null).
	 */
	@Override
	public boolean canProvidePower(BlockState state)
	{
		return true;
	}

	/**
	 * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
	 *
	 * @param world The world were the block resides.
	 * @param pos The position of the current block.
	 * @param player The player damaging the block, may be null
	 * @return True to spawn the drops
	 */
	@Override
	public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		return true;
	}

	/**
	 * Creates a new tile entity.
	 * 
	 * @param worldIn The world to create the tile entity in.
	 * @param meta The meta data to create the tile entity.
	 */
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn)
	{
		// System.out.println("Creating new tile entity.");
		return this.createNewTileEntity();
	}

	/**
	 * Creates a new instance of the tile entity associated with this block.
	 * 
	 * @return A new instance of the tile entity associatd with this block.
	 */
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

	/**
	 * Determines if this block has a tile entity.
	 * 
	 * @param state Not used.
	 * @return Returns True.
	 */
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

/*	*//**
	 * The break block event.
	 * 
	 * @param worldIn The world where the block resides.
	 * @param pos The position of the block.
	 * @param state the state of the block.
	 *//*
	@Override
	public void breakBlock(World worldIn, BlockPos pos, BlockState state)
	{
		T tileEntity = this.getLocalTileEntity(worldIn, pos);

		this.customBreakBlock(tileEntity, worldIn, pos, state);

		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, BlockState state)
	{
		for (Direction enumfacing : Direction.values())
		{
			worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, true);
		}

		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));

		if (worldIn.getTileEntity(pos) == null)
		{
			T tile = this.createNewTileEntity();

			worldIn.setTileEntity(pos, tile);
		}
	}*/

	@Override
	public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int eventID, int eventParam)
	{
		super.eventReceived(state, worldIn, pos, eventID, eventParam);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
	}

/*	@Override
	public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand)
	{
		this.updateState(worldIn, pos, state);
	}

	*//**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
	 * IBlockstate
	 *//*
	@Override
	public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta,
		LivingEntity placer)
	{
		return this.getDefaultState();
	}

	*//**
	 * Updates the state of this block.
	 * 
	 * @param worldIn The world where the block resides.
	 * @param pos The position of the block.
	 * @param state The current state of the block.
	 *//*
	public void updateState(World worldIn, BlockPos pos, BlockState state)
	{
		T tileEntity = this.getLocalTileEntity(worldIn, pos);

		int tickDelay = this.customUpdateState(worldIn, pos, state, tileEntity);

		if (tickDelay > 0)
		{
			worldIn.markBlockRangeForRenderUpdate(pos, pos);
			worldIn.scheduleUpdate(pos, this, tickDelay);
		}
	}*/

	/**
	 * Gets the tile entity at the current position.
	 * 
	 * @param worldIn The world to the entity for.
	 * @param pos The position in the world to get the entity for.
	 * @return Null if the tile was not found or if one was found and is not a proper tile entity. Otherwise the tile
	 *         entity instance.
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
	 * 
	 * @param worldIn The world this state is being updated in.
	 * @param pos The block position.
	 * @param state The block state.
	 * @param tileEntity The tile entity associated with this class.
	 * @return The number of ticks to delay until the next update.
	 */
	public abstract int customUpdateState(World worldIn, BlockPos pos, BlockState state, T tileEntity);

	/**
	 * The custom break block event used by derived classes.
	 * 
	 * @param tileEntity The tile entity for this block.
	 * @param worldIn The world where the block resides.
	 * @param pos THe position of the block.
	 * @param state The current state of the block.
	 */
	public abstract void customBreakBlock(T tileEntity, World worldIn, BlockPos pos, BlockState state);
}