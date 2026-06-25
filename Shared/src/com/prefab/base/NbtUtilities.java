package com.prefab.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;

import java.util.Optional;

public final class NbtUtilities {
    public static Optional<BlockPos> readBlockPos(CompoundTag tag, String key) {
        Optional<int[]> tagIntArray = tag.getIntArray(key);

        if (tagIntArray.isEmpty()) {
            return Optional.empty();
        }

        int[] ints = tagIntArray.get();

        return ints.length == 3 ? Optional.of(new BlockPos(ints[0], ints[1], ints[2])) : Optional.empty();
    }

    public static Tag writeBlockPos(BlockPos pos) {
        return new IntArrayTag(new int[]{pos.getX(), pos.getY(), pos.getZ()});
    }

}
