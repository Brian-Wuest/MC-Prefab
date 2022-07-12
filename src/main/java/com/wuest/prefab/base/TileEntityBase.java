package com.wuest.prefab.base;

import com.wuest.prefab.Prefab;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This is the base tile entity used by the mod.
 *
 * @param <T> The base configuration used by this tile entity.
 * @author WuestMan
 */
public abstract class TileEntityBase<T extends BaseConfig> extends BlockEntity {
	protected T config;

	protected TileEntityBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
		super(tileEntityTypeIn, pos, state);
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
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		// Don't send the packet until the position has been set.
		if (this.worldPosition.getX() == 0 && this.worldPosition.getY() == 0 && this.worldPosition.getZ() == 0) {
			return null;
		}

		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		return true;
	}

	@Override
	public @NotNull CompoundTag getUpdateTag() {
		// This is overwritten so our custom save event is done.
		CompoundTag updateTag = new CompoundTag();
		this.saveAdditional(updateTag);
		return updateTag;
	}

	@Override
	public void load(@NotNull CompoundTag compound) {
		super.load(compound);

		if (this.config != null) {
			this.config.WriteToNBTCompound(compound);
		}
	}

	@Override
	public void saveAdditional(@NotNull CompoundTag compound) {
		super.saveAdditional(compound);

		this.config = this.createConfigInstance().ReadFromCompoundTag(compound);
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
