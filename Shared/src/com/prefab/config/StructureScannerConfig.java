package com.prefab.config;

import com.prefab.base.BaseConfig;
import com.prefab.base.NbtUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

public class StructureScannerConfig extends BaseConfig<StructureScannerConfig> {
    public int blocksToTheLeft = 0;
    public int blocksParallel = 1;
    public int blocksDown = 0;
    public int blocksWide = 1;
    public int blocksLong = 1;
    public int blocksTall = 1;
    public String structureZipName = "";
    public Direction direction;

    public BlockPos blockPos = null;

    public StructureScannerConfig() {
        this.direction = Direction.NORTH;
        this.blockPos = new BlockPos(0, 0, 0);
    }

    @Override
    public void WriteToNBTCompound(ValueOutput valueOutput) {
        valueOutput.putInt("blocksToTheLeft", this.blocksToTheLeft);
        valueOutput.putInt("blocksDown", this.blocksDown);
        valueOutput.putInt("blocksWide", this.blocksWide);
        valueOutput.putInt("blocksLong", this.blocksLong);
        valueOutput.putInt("blocksTall", this.blocksTall);
        valueOutput.putString("structureZipName", this.structureZipName);
        valueOutput.putInt("direction", this.direction.get3DDataValue());
        valueOutput.putInt("blocksParallel", this.blocksParallel);

        if (this.blockPos != null) {
            valueOutput.storeNullable("pos", BlockPos.CODEC, this.blockPos);
        }
    }

    @Override
    public StructureScannerConfig ReadFromCompoundNBT(@NotNull CompoundTag compound) {
        this.blocksToTheLeft = compound.getInt("blocksToTheLeft").orElse(0);
        this.blocksDown = compound.getInt("blocksDown").orElse(0);
        this.blocksWide = compound.getInt("blocksWide").orElse(0);
        this.blocksLong = compound.getInt("blocksLong").orElse(0);
        this.blocksTall = compound.getInt("blocksTall").orElse(0);
        this.structureZipName = compound.getString("structureZipName").orElse("");
        this.direction = Direction.from3DDataValue(compound.getInt("direction").orElse(0));
        this.blocksParallel = compound.getInt("blocksParallel").orElse(0);

        if (compound.contains("pos")) {
            this.blockPos = NbtUtilities.readBlockPos(compound, "pos").orElse(new BlockPos(0, 0, 0));
        }

        return this;
    }
}
