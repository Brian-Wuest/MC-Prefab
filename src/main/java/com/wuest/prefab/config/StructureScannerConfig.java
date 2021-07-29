package com.wuest.prefab.config;

import com.wuest.prefab.base.BaseConfig;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class StructureScannerConfig extends BaseConfig {
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
    public void WriteToNBTCompound(CompoundNBT compound) {
        compound.putInt("blocksToTheLeft", this.blocksToTheLeft);
        compound.putInt("blocksDown", this.blocksDown);
        compound.putInt("blocksWide", this.blocksWide);
        compound.putInt("blocksLong", this.blocksLong);
        compound.putInt("blocksTall", this.blocksTall);
        compound.putString("structureZipName", this.structureZipName);
        compound.putInt("direction", this.direction.get3DDataValue());
        compound.putInt("blocksParallel", this.blocksParallel);

        if (this.blockPos != null) {
            compound.put("pos", NBTUtil.writeBlockPos(this.blockPos));
        }
    }

    @Override
    public StructureScannerConfig ReadFromCompoundNBT(CompoundNBT compound) {
        this.blocksToTheLeft = compound.getInt("blocksToTheLeft");
        this.blocksDown = compound.getInt("blocksDown");
        this.blocksWide = compound.getInt("blocksWide");
        this.blocksLong = compound.getInt("blocksLong");
        this.blocksTall = compound.getInt("blocksTall");
        this.structureZipName = compound.getString("structureZipName");
        this.direction = Direction.from3DDataValue(compound.getInt("direction"));
        this.blocksParallel = compound.getInt("blocksParallel");

        if (compound.contains("pos")) {
            this.blockPos = NBTUtil.readBlockPos(compound.getCompound("pos"));
        }

        return this;
    }
}
