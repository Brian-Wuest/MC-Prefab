package com.wuest.prefab.base;

import com.wuest.prefab.capabilities.ITransferable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;

/**
 * This class transfers the capabilities from the item to the tile entity associated with the block when it's placed.
 * 
 * @author WuestMan
 *
 */
public class ItemBlockCapability extends ItemBlock
{
	/**
	 * The allowed capabilities for this ItemBlock.
	 */
	protected ArrayList<Capability> allowedCapabilities;

	/**
	 * Initializes a new instance of the ItemBlockCapability class.
	 * 
	 * @param block The block associated with this ItemBlock.
	 * @param allowedCapabilities The capabilities which are allowed for this block.
	 */
	public ItemBlockCapability(Block block, ArrayList<Capability> allowedCapabilities)
	{
		super(block);
		this.allowedCapabilities = allowedCapabilities;
	}

	/**
	 * Gets the allowed capabilities for this item block.
	 * 
	 * @return The allowed capabilities for this item block.
	 */
	public ArrayList<Capability> getAllowedCapabilities()
	{
		if (this.allowedCapabilities == null)
		{
			this.allowedCapabilities = new ArrayList<Capability>();
			this.addAllowedCapabilities();
		}

		return this.allowedCapabilities;
	}

	/**
	 * Sets the allowed capabilities for this Item Block.
	 * 
	 * @param value The list of allowed capabilities.
	 * @return This instance.
	 */
	public ItemBlockCapability setAllowedCapabilities(ArrayList<Capability> value)
	{
		this.allowedCapabilities = value;

		return this;
	}

	/**
	 * Called to actually place the block, after the location is determined and all permission checks have been made.
	 *
	 * @param stack The item stack that was used to place the block. This can be changed inside the method.
	 * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
	 * @param side The side the player (or machine) right-clicked on.
	 */
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ,
		IBlockState newState)
	{
		if (!world.setBlockState(pos, newState, 3))
			return false;

		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == this.block)
		{
			setTileEntityNBT(world, player, pos, stack);
			this.block.onBlockPlacedBy(world, pos, state, player, stack);
		}

		return true;
	}

	/**
	 * Transfers the capability data from this item to the tile entity.
	 * 
	 * @param world The world this is affecting.
	 * @param player The controlling player.
	 * @param pos The block pos of the placed block.
	 * @param stack The item stack to set the tile entity capability for.
	 * @param side The side for which to get the capability for.
	 */
	protected void setTileEntityCapabilities(World world, EntityPlayer player, BlockPos pos, ItemStack stack, EnumFacing side)
	{
		if (world.isRemote)
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity != null)
			{
				for (Capability capability : this.allowedCapabilities)
				{
					// Get the interfaces for this capability.
					Object stackCapability = stack.getCapability(capability, side);
					Object tileEntityCapability = tileEntity.getCapability(capability, side);

					if (stackCapability != null && tileEntityCapability != null && stackCapability instanceof ITransferable
						&& tileEntityCapability instanceof ITransferable)
					{
						// transfer the capability data, it's up to the capability to transfer the data.
						((ITransferable) tileEntityCapability).Transfer((ITransferable) stackCapability);
					}
				}
			}
		}
	}

	/**
	 * Adds the allowed capabilities during item initialization.
	 */
	protected void addAllowedCapabilities()
	{
	}
}
