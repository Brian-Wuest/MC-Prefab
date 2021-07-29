package com.wuest.prefab.base;

import com.wuest.prefab.Prefab;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This is the base tile entity used by the mod.
 *
 * @param <T> The base configuration used by this tile entity.
 * @author WuestMan
 */
public abstract class TileEntityBase<T extends BaseConfig> extends TileEntity {
	protected T config;

	protected TileEntityBase(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	/**
	 * @return Gets the configuration class used by this tile entity.
	 */
	public T getConfig() {
		return this.config;
	}

	/**
	 * Sets the configuration class used by this tile entity.
	 *
	 * @param value The updated tile entity.
	 */
	public void setConfig(T value) {
		this.config = value;
		this.setChanged();
	}

	public Class<T> getTypeParameterClass() {
		Type type = getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) type;
		return (Class<T>) paramType.getActualTypeArguments()[0];
	}

	/**
	 * Allows for a specialized description packet to be created. This is often used
	 * to sync tile entity data from the server to the client easily. For example
	 * this is used by signs to synchronize the text to be displayed.
	 */
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		// Don't send the packet until the position has been set.
		if (this.worldPosition.getX() == 0 && this.worldPosition.getY() == 0 && this.worldPosition.getZ() == 0) {
			return super.getUpdatePacket();
		}

		CompoundNBT tag = new CompoundNBT();
		this.save(tag);

		return new SUpdateTileEntityPacket(this.getBlockPos(), 1, tag);
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		return true;
	}

	@Override
	public CompoundNBT getUpdateTag() {
		// This is overwritten so our custom save event is done.
		return this.save(new CompoundNBT());
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound = super.save(compound);

		if (this.config != null) {
			this.config.WriteToNBTCompound(compound);
		}

		return compound;
	}

	@Override
	public void load(BlockState blockState, CompoundNBT compound) {
		super.load(blockState, compound);

		this.config = this.createConfigInstance().ReadFromCompoundNBT(compound);
	}

	public T createConfigInstance() {
		try {
			return this.getTypeParameterClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Prefab.LOGGER.log(Level.ERROR, e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
}
