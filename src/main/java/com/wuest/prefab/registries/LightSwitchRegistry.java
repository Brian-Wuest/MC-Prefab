package com.wuest.prefab.registries;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.BlockDarkLamp;
import com.wuest.prefab.blocks.BlockLightSwitch;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Vector;

public class LightSwitchRegistry extends ILevelBasedRegistry<BlockPos> {
    private static final int SearchBlockRadius = 24;

    public LightSwitchRegistry() {
        super();
    }

    @Override
    protected void onElementRemoved(Level level, BlockPos element) {
        this.setNearbyLights(element, level, false);
    }

    @Override
    protected void onElementRegistered(Level level, BlockPos element) {
        // Do nothing here as it's not necessary.
    }

    public void flipSwitch(Level level, BlockPos incomingBlockPos, boolean turnOn) {
        // Don't do anything client-side.
        if (!level.isClientSide && this.internalRegistry.containsKey(level)) {
            Vector<BlockPos> blockPositions = this.internalRegistry.get(level);

            // Make sure to check for null in-case the key was removed between the contains check and the get.
            if (blockPositions != null) {
                for (BlockPos blockPos : blockPositions) {
                    if (blockPos.hashCode() == incomingBlockPos.hashCode()) {
                        this.setNearbyLights(blockPos, level, turnOn);
                        break;
                    }
                }
            }
        }
    }

    public boolean checkForNearbyOnSwitch(Level level, BlockPos blockPos) {
        Tuple<BlockPos, BlockPos> searchPositions = this.getSearchStartAndEnd(blockPos);

        for (BlockPos worldPos: BlockPos.betweenClosed(searchPositions.getFirst(), searchPositions.getSecond())) {
            BlockState blockState = level.getBlockState(worldPos);

            if (blockState.getBlock() == ModRegistry.LightSwitch.get()) {
                // Always use the first one found.
                return blockState.getValue(BlockLightSwitch.POWERED);
            }
        }

        return false;
    }

    private void setNearbyLights(BlockPos blockPos, Level level, boolean turnOn) {
        Tuple<BlockPos, BlockPos> searchPositions = this.getSearchStartAndEnd(blockPos);

        for (BlockPos worldPos: BlockPos.betweenClosed(searchPositions.getFirst(), searchPositions.getSecond())) {
            BlockState blockState = level.getBlockState(worldPos);

            if (blockState.getBlock() == ModRegistry.DarkLamp.get()) {
                blockState = blockState.setValue(BlockDarkLamp.LIT, turnOn);
                level.setBlock(worldPos, blockState, 3);
            }
        }
    }

    private Tuple<BlockPos, BlockPos> getSearchStartAndEnd(BlockPos startingPos) {
        BlockPos startPos = startingPos
                .below(LightSwitchRegistry.SearchBlockRadius)
                .south(LightSwitchRegistry.SearchBlockRadius)
                .west(LightSwitchRegistry.SearchBlockRadius);

        BlockPos endPos = startingPos
                .above(LightSwitchRegistry.SearchBlockRadius)
                .north(LightSwitchRegistry.SearchBlockRadius)
                .east(LightSwitchRegistry.SearchBlockRadius);

        return new Tuple<>(startPos, endPos);
    }
}
