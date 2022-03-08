package com.wuest.prefab.config.block_entities;
import com.wuest.prefab.base.BaseConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

public class DraftingTableConfiguration extends BaseConfig {
    public BlockPos blockPos = null;

    public DraftingTableConfiguration() {
        this.blockPos = new BlockPos(0, 0, 0);
    }

    @Override
    public void WriteToNBTCompound(CompoundTag compound) {
        if (this.blockPos != null) {
            compound.put("pos", NbtUtils.writeBlockPos(this.blockPos));
        }
    }

    @Override
    public DraftingTableConfiguration ReadFromCompoundTag(CompoundTag compound) {
        if (compound.contains("pos")) {
            this.blockPos = NbtUtils.readBlockPos(compound.getCompound("pos"));
        }
        return this;
    }
}