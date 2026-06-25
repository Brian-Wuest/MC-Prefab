package com.prefab.config;

import com.prefab.base.BaseConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueOutput;

public class LightSwitchConfig extends BaseConfig<LightSwitchConfig> {
    @Override
    public void WriteToNBTCompound(ValueOutput valueOutput) {

    }

    @Override
    public LightSwitchConfig ReadFromCompoundNBT(CompoundTag compound) {
        return this;
    }
}
