package com.prefab.fabric.blocks;

import com.prefab.ModRegistryBase;
import com.prefab.fabric.events.ServerEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockPhasic extends com.prefab.blocks.BlockPhasic {
    public BlockPhasic(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
    public @NotNull BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide()) {
            ModRegistryBase.serverModRegistries.getPhasicBlockRegistry().remove(world, pos);
        }

        super.playerWillDestroy(world, pos, state, player);

        return state;
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        int tickDelay = this.tickRate;

        if (ServerEvents.RedstoneAffectedBlockPositions.contains(pos)) {
            return;
        }

        EnumPhasingProgress progress = state.getValue(Phasing_Progress);
        boolean phasingOut = state.getValue(Phasing_Out);

        // If the state is at base, progress trigger the phasing out to the neighboring blocks.
        if (progress == EnumPhasingProgress.base) {
            for (Direction facing : Direction.values()) {
                Block currentBlock = worldIn.getBlockState(pos.relative(facing)).getBlock();

                if (currentBlock instanceof com.prefab.blocks.BlockPhasic
                        && !ServerEvents.RedstoneAffectedBlockPositions.contains(pos.relative(facing))) {
                    worldIn.scheduleTick(pos.relative(facing), currentBlock, tickDelay);
                }
            }

            phasingOut = true;
        }

        int updatedMeta = progress.getMeta();

        if (updatedMeta == EnumPhasingProgress.eighty_percent.getMeta()
                && phasingOut) {
            // This next phase should take 100 ticks (5 seconds) since this is the phase out.
            tickDelay = 100;
        }

        if (updatedMeta == EnumPhasingProgress.transparent.getMeta()
                && phasingOut) {
            // set the phasing to in.
            phasingOut = false;
        }

        if (updatedMeta == EnumPhasingProgress.twenty_percent.getMeta()
                && !phasingOut) {
            // Phasing in for this delay, set the tick delay to -1 and stop the phasing. Reset the phasing out property.
            tickDelay = -1;
        }

        updatedMeta = phasingOut ? updatedMeta + 2 : updatedMeta - 2;
        progress = EnumPhasingProgress.ValueOf(updatedMeta);

        // Update the state in the world, update the world and (possibly) schedule a state update.
        state = state.setValue(Phasing_Out, phasingOut).setValue(Phasing_Progress, progress);
        worldIn.setBlock(pos, state, 3);

        if (tickDelay > 0) {
            worldIn.scheduleTick(pos, this, tickDelay);
        }
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos,
                                Block block, @Nullable Orientation orientation, boolean bl) {
        if (!level.isClientSide()) {
            // Only worry about powering blocks.
            if (block.defaultBlockState().isSignalSource()) {
                boolean poweredSide = level.hasNeighborSignal(blockPos);
                EnumPhasingProgress currentState = blockState.getValue(Phasing_Progress);
                boolean setToTransparent = poweredSide && currentState == EnumPhasingProgress.base;

                if (currentState == EnumPhasingProgress.base || currentState == EnumPhasingProgress.transparent) {
                    ModRegistryBase.serverModRegistries.getPhasicBlockRegistry()
                            .updateNeighborPhasicBlocks(setToTransparent, level, blockPos,
                                    blockState, true, true);
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

        if (level.isClientSide() && poweredSide) {
            ModRegistryBase.serverModRegistries.getPhasicBlockRegistry().updateNeighborPhasicBlocks(
                    true, level, blockPos,
                    this.defaultBlockState(), false, false);
        }

        return this.defaultBlockState().setValue(Phasing_Out, poweredSide)
                .setValue(Phasing_Progress, EnumPhasingProgress.base);
    }


    @Environment(EnvType.CLIENT)
    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        EnumPhasingProgress progress = state.getValue(Phasing_Progress);

        return progress == EnumPhasingProgress.transparent;
    }
}
