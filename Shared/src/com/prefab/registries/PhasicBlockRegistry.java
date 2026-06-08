package com.prefab.registries;

import com.prefab.blocks.BlockPhasic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

import static com.prefab.blocks.BlockPhasic.Phasing_Out;
import static com.prefab.blocks.BlockPhasic.Phasing_Progress;

public class PhasicBlockRegistry extends ILevelBasedRegistry<BlockPos> {

    public PhasicBlockRegistry() {
        super();
    }

    @Override
    protected void onElementRemoved(Level level, BlockPos blockPos) {
        boolean poweredSide = level.hasNeighborSignal(blockPos);

        BlockState state = level.getBlockState(blockPos);
        BlockPhasic.EnumPhasingProgress phasingProgress = state.getValue(Phasing_Progress);

        if (poweredSide && phasingProgress == BlockPhasic.EnumPhasingProgress.transparent) {
            // Set this block and all neighbor Phasic Blocks to base. This will cascade to tall touching Phasic blocks.
            this.updateNeighborPhasicBlocks(false, level, blockPos, state, false, false);
        }
    }

    @Override
    protected void onElementRegistered(Level level, BlockPos element) {

    }

    public void updateNeighborPhasicBlocks(boolean setToTransparent, Level worldIn, BlockPos pos, BlockState phasicBlockState, boolean setCurrentBlock,
                                              boolean triggeredByRedstone) {
        ArrayList<BlockPos> blocksToUpdate = new ArrayList<BlockPos>();
        BlockState updatedBlockState = phasicBlockState
                .setValue(Phasing_Out, setToTransparent)
                .setValue(Phasing_Progress, setToTransparent ? BlockPhasic.EnumPhasingProgress.transparent : BlockPhasic.EnumPhasingProgress.base);

        // Set this block and all neighbor Phasic Blocks to transparent. This will cascade to all touching Phasic
        // blocks.
        this.findNeighborPhasicBlocks(worldIn, pos, updatedBlockState, 0, blocksToUpdate, setCurrentBlock);

        for (BlockPos positionToUpdate : blocksToUpdate) {
            worldIn.setBlock(positionToUpdate, updatedBlockState, 3);

            if (triggeredByRedstone) {
                if (this.contains(worldIn, positionToUpdate) && !setToTransparent) {
                    this.remove(worldIn, positionToUpdate);
                } else if (!this.contains(worldIn, positionToUpdate) && setToTransparent) {
                    this.register(worldIn, positionToUpdate);
                }
            }
        }
    }

    public int findNeighborPhasicBlocks(Level worldIn, BlockPos pos, BlockState desiredBlockState, int cascadeCount,
                                           ArrayList<BlockPos> cascadedBlockPos, boolean setCurrentBlock) {
        cascadeCount++;

        if (cascadeCount > 100) {
            return cascadeCount;
        }

        if (setCurrentBlock) {
            cascadedBlockPos.add(pos);
        }

        for (Direction facing : Direction.values()) {
            Block neighborBlock = worldIn.getBlockState(pos.relative(facing)).getBlock();

            if (neighborBlock instanceof BlockPhasic) {
                BlockState blockState = worldIn.getBlockState(pos.relative(facing));

                // If the block is already in the correct state or was already checked, there is no need to cascade to
                // it's neighbors.
                BlockPhasic.EnumPhasingProgress progress = blockState.getValue(Phasing_Progress);

                if (cascadedBlockPos.contains(pos.relative(facing)) || progress == desiredBlockState.getValue(Phasing_Progress)) {
                    continue;
                }

                setCurrentBlock = true;
                cascadeCount = this.findNeighborPhasicBlocks(worldIn, pos.relative(facing), desiredBlockState, cascadeCount, cascadedBlockPos, setCurrentBlock);

                if (cascadeCount > 100) {
                    break;
                }
            }
        }

        return cascadeCount;
    }
}
