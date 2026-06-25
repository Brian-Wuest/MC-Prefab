package com.prefab.base;

import com.prefab.PrefabBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * This is the base tile entity used by the mod.
 *
 * @param <T> The base configuration used by this tile entity.
 * @author WuestMan
 */
public abstract class TileEntityBase<T extends BaseConfig<T>> extends BlockEntity {
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
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    @Override
    public void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);

        Optional<CompoundTag> components = valueInput.read("TileEntityData",  CompoundTag.CODEC);

        components.ifPresent(compoundTag ->
                this.config = this.createConfigInstance().ReadFromCompoundNBT(compoundTag));
    }

    @Override
    public void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        if (this.config != null) {
            this.config.WriteToNBTCompound(valueOutput);
        }
    }

    public T createConfigInstance() {
        try {
            return this.getTypeParameterClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            PrefabBase.logger.log(Level.ERROR, e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
