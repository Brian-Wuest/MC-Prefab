package com.prefab.neoforge.blocks;

import com.prefab.neoforge.events.GameServerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BlockPhasic extends com.prefab.blocks.BlockPhasic {
    public BlockPhasic() {
        super();
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
    public @NotNull BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide()) {
            com.prefab.blocks.BlockPhasic.EnumPhasingProgress currentState = state.getValue(Phasing_Progress);

            super.playerWillDestroy(world, pos, state, player);

            GameServerEvents.RedstoneAffectedBlockPositions.remove(pos);

            boolean poweredSide = world.hasNeighborSignal(pos);

            if (poweredSide && currentState == com.prefab.blocks.BlockPhasic.EnumPhasingProgress.transparent) {
                // Set this block and all neighbor Phasic Blocks to base. This will cascade to tall touching Phasic blocks.
                this.updateNeighborPhasicBlocks(false, world, pos, state, false, false);
            }
        }

        return state;
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        int tickDelay = this.tickRate;

        if (GameServerEvents.RedstoneAffectedBlockPositions.contains(pos)) {
            return;
        }

        com.prefab.blocks.BlockPhasic.EnumPhasingProgress progress = state.getValue(Phasing_Progress);
        boolean phasingOut = state.getValue(Phasing_Out);

        // If the state is at base, progress trigger the phasing out to the neighboring blocks.
        if (progress == com.prefab.blocks.BlockPhasic.EnumPhasingProgress.base) {
            for (Direction facing : Direction.values()) {
                Block currentBlock = worldIn.getBlockState(pos.relative(facing)).getBlock();

                if (currentBlock instanceof com.prefab.blocks.BlockPhasic && !GameServerEvents.RedstoneAffectedBlockPositions.contains(pos.relative(facing))) {
                    worldIn.scheduleTick(pos.relative(facing), currentBlock, tickDelay);
                }
            }

            phasingOut = true;
        }

        int updatedMeta = progress.getMeta();

        if (updatedMeta == com.prefab.blocks.BlockPhasic.EnumPhasingProgress.eighty_percent.getMeta()
                && phasingOut) {
            // This next phase should take 100 ticks (5 seconds) since this is the phase out.
            tickDelay = 100;
        }

        if (updatedMeta == com.prefab.blocks.BlockPhasic.EnumPhasingProgress.transparent.getMeta()
                && phasingOut) {
            // set the phasing to in.
            phasingOut = false;
        }

        if (updatedMeta == com.prefab.blocks.BlockPhasic.EnumPhasingProgress.twenty_percent.getMeta()
                && !phasingOut) {
            // Phasing in for this delay, set the tick delay to -1 and stop the phasing. Reset the phasing out property.
            tickDelay = -1;
        }

        updatedMeta = phasingOut ? updatedMeta + 2 : updatedMeta - 2;
        progress = com.prefab.blocks.BlockPhasic.EnumPhasingProgress.ValueOf(updatedMeta);

        // Update the state in the world, update the world and (possibly) schedule a state update.
        state = state.setValue(Phasing_Out, phasingOut).setValue(Phasing_Progress, progress);
        worldIn.setBlock(pos, state, 3);

        if (tickDelay > 0) {
            worldIn.scheduleTick(pos, this, tickDelay);
        }
    }

    protected void updateNeighborPhasicBlocks(boolean setToTransparent, Level worldIn, BlockPos pos, BlockState phasicBlockState, boolean setCurrentBlock,
                                              boolean triggeredByRedstone) {
        ArrayList<BlockPos> blocksToUpdate = new ArrayList<BlockPos>();
        BlockState updatedBlockState = phasicBlockState
                .setValue(Phasing_Out, setToTransparent)
                .setValue(Phasing_Progress, setToTransparent ? com.prefab.blocks.BlockPhasic.EnumPhasingProgress.transparent : com.prefab.blocks.BlockPhasic.EnumPhasingProgress.base);

        // Set this block and all neighbor Phasic Blocks to transparent. This will cascade to all touching Phasic
        // blocks.
        this.findNeighborPhasicBlocks(worldIn, pos, updatedBlockState, 0, blocksToUpdate, setCurrentBlock);

        for (BlockPos positionToUpdate : blocksToUpdate) {
            worldIn.setBlock(positionToUpdate, updatedBlockState, 3);

            if (triggeredByRedstone) {
                if (GameServerEvents.RedstoneAffectedBlockPositions.contains(positionToUpdate) && !setToTransparent) {
                    GameServerEvents.RedstoneAffectedBlockPositions.remove(positionToUpdate);
                } else if (!GameServerEvents.RedstoneAffectedBlockPositions.contains(positionToUpdate) && setToTransparent) {
                    GameServerEvents.RedstoneAffectedBlockPositions.add(positionToUpdate);
                }
            }
        }
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_, boolean p_220069_6_) {
        if (!worldIn.isClientSide()) {
            // Only worry about powering blocks.
            if (blockIn.defaultBlockState().isSignalSource()) {
                boolean poweredSide = worldIn.hasNeighborSignal(pos);
                com.prefab.blocks.BlockPhasic.EnumPhasingProgress currentState = state.getValue(Phasing_Progress);
                boolean setToTransparent = false;

                if (poweredSide && currentState == com.prefab.blocks.BlockPhasic.EnumPhasingProgress.base) {
                    setToTransparent = true;
                }

                if (currentState == com.prefab.blocks.BlockPhasic.EnumPhasingProgress.base || currentState == com.prefab.blocks.BlockPhasic.EnumPhasingProgress.transparent) {
                    this.updateNeighborPhasicBlocks(setToTransparent, worldIn, pos, state, true, true);
                }
            }
        }
    }

    /**
     * Gets the {@link BlockState} to place
     *
     * @param context The {@link BlockPlaceContext}.
     * @return The state to be placed in the world
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        /*
         * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
         * BlockState
         */
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        boolean poweredSide = level.hasNeighborSignal(blockPos);

        if (!level.isClientSide() && poweredSide) {
            this.updateNeighborPhasicBlocks(true, level, blockPos,
                    this.defaultBlockState(), false, false);
        }

        return this.defaultBlockState().setValue(Phasing_Out, poweredSide).setValue(Phasing_Progress,
                com.prefab.blocks.BlockPhasic.EnumPhasingProgress.base);
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        com.prefab.blocks.BlockPhasic.EnumPhasingProgress progress = state.getValue(Phasing_Progress);

        return progress == com.prefab.blocks.BlockPhasic.EnumPhasingProgress.transparent;
    }
}
