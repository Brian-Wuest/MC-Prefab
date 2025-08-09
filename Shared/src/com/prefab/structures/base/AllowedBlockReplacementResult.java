package com.prefab.structures.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AllowedBlockReplacementResult {
    public ReplacementResultType allowedToReplace;
    public BlockState protectedBlock;
    public BlockPos protectedBlockPos;

    public AllowedBlockReplacementResult (ReplacementResultType resultType, BlockState protectedBlock, BlockPos protectedBlockPos) {
        this.allowedToReplace = resultType;
        this.protectedBlock = protectedBlock;
        this.protectedBlockPos = protectedBlockPos;
    }
}

