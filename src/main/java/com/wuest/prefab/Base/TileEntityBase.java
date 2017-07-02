package com.wuest.prefab.Base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.wuest.prefab.Capabilities.ITransferable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

/**
 * This is the base tile entity used by the mod.
 * @author WuestMan
 *
 * @param <T> The base configuration used by this tile entity.
 */
public abstract class TileEntityBase<T extends BaseConfig> extends TileEntity
{
	protected T config;
	protected ArrayList<Capability> allowedCapabilities;
	
	/**
	 * Initializes a new instance of the TileEntityBase class.
	 */
	protected TileEntityBase()
	{
	}
	
	/**
	 * @return Gets the configuration class used by this tile entity.
	 */
	public T getConfig()
	{
		return this.config;
	}
	
	/**
	 * Sets the configuration class used by this tile entity.
	 * @param value The updated tile entity.
	 */
	public void setConfig(T value)
	{
		this.config = value;
		this.markDirty();
	}
	
	/**
	 * Gets the list of allowed capabilities.
	 * @return The list of allowed capabilities if any.
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
	 * Sets the allowed capabilities for this TileEntity.
	 * @param allowedCapabilities The list of allowed capabilities.
	 */
	public void setAllowedCapabilities(ArrayList<Capability> allowedCapabilities)
	{
		this.allowedCapabilities = allowedCapabilities;
		this.markDirty();
	}
	
	/**
	 * Transfers capabilities available for transferring to the supplied itemstack.
	 * @param stack The item stack to copy capabilities for.
	 * @return The updated item stack with the original ItemStack's capabilities.
	 */
	public ItemStack transferCapabilities(ItemStack stack)
	{
		// Transfer each transferable capability to the itemstack.
		for(Capability allowedCapability : this.getAllowedCapabilities())
		{
			// Get the interfaces for this capability.
			Object stackCapability = stack.getCapability(allowedCapability, EnumFacing.NORTH);
			Object tileEntityCapability = this.getCapability(allowedCapability, EnumFacing.NORTH);
			
			if (stackCapability != null && tileEntityCapability != null
					&& stackCapability instanceof ITransferable && tileEntityCapability instanceof ITransferable)
			{
				// transfer the capability data, it's up to the capability to transfer the data.
				((ITransferable)stackCapability).Transfer((ITransferable)tileEntityCapability);
			}
		}
		
		return stack;
	}
	
	/**
	 * Allows this class to get a class of the generic type associated with this class.
	 * @return A generic class used to create an instance of the generic class.
	 */
    public Class<T> getTypeParameterClass()
    {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class<T>) paramType.getActualTypeArguments()[0];
    }
	
	/**
	 * Allows for a specialized description packet to be created. This is often used to sync tile entity data from the
	 * server to the client easily. For example this is used by signs to synchronize the text to be displayed.
	 */
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		// Don't send the packet until the position has been set.
		if (this.pos.getX() == 0 && this.pos.getY() == 0 && this.pos.getZ() == 0)
		{
			return super.getUpdatePacket();
		}

		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);

		return new SPacketUpdateTileEntity(this.getPos(), 1, tag);
	}

	/**
	 * Called when the chunk this TileEntity is on is Unloaded.
	 */
	@Override
	public void onChunkUnload()
	{
		// Make sure to write the tile to the tag.
		this.writeToNBT(this.getTileData());
	}
	
	/**
	 * Called when you receive a TileEntityData packet for the location this
	 * TileEntity is currently in. On the client, the NetworkManager will always
	 * be the remote server. On the server, it will be whomever is responsible for
	 * sending the packet.
	 *
	 * @param net The NetworkManager the packet originated from
	 * @param pkt The data packet
	 */
	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}
	
	@Override
	public boolean receiveClientEvent(int id, int type)
	{
		return true;
	}
	
	/**
	 * Called from Chunk.setBlockIDWithMetadata and Chunk.fillChunk, determines if this tile entity should be re-created when the ID, or Metadata changes.
	 * Use with caution as this will leave straggler TileEntities, or create conflicts with other TileEntities if not used properly.
	 *
	 * @param world Current world
	 * @param pos Tile's world position
	 * @param oldState The old ID of the block
	 * @param newState The new ID of the block (May be the same)
	 * @return true forcing the invalidation of the existing TE, false not to invalidate the existing TE
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		// This tile needs to persist so the data can be saved.
		return (oldState.getBlock() != newState.getBlock());
	}
	
	@Override
	public void updateContainingBlockInfo()
	{
	}
	
	@Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		//System.out.println("Writing Tag Data.");
		super.writeToNBT(compound);

		if (this.config != null)
		{
			this.config.WriteToNBTCompound(compound);
		}
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		//System.out.println("Reading Tag Data.");
		super.readFromNBT(compound);

		this.config = this.createConfigInstance().ReadFromNBTTagCompound(compound);
	}
	
	/**
	 * Creates an instance of the configuration class for this tile entity.
	 * @return A new instance of the configuration class for this tile entity.
	 */
	public T createConfigInstance()
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
	 * Adds the allowed capabilities during tile entity initialization.
	 */
	protected void addAllowedCapabilities()
	{
	}
	
}
