package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.base.BuildClear;
import com.wuest.prefab.structures.base.BuildingMethods;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.BulldozerConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
public class StructureBulldozer extends Structure {

    /**
     * Initializes a new instance of the {@link StructureBulldozer} class.
     */
    public StructureBulldozer() {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(Direction.SOUTH);
        clearedSpace.getShape().setHeight(15);
        clearedSpace.getShape().setLength(16);
        clearedSpace.getShape().setWidth(16);
        clearedSpace.getStartingPosition().setSouthOffset(1);
        clearedSpace.getStartingPosition().setEastOffset(8);
        clearedSpace.getStartingPosition().setHeightOffset(1);

        this.setClearSpace(clearedSpace);
        this.setBlocks(new ArrayList<>());
    }

    /**
     * This method is to process before a clear space block is set to air.
     *
     * @param pos The block position being processed.
     */
    @Override
    public void BeforeClearSpaceBlockReplaced(BlockPos pos) {
        BlockState state = this.world.getBlockState(pos);
        BulldozerConfiguration configuration = (BulldozerConfiguration) this.configuration;

        // Only harvest up to diamond level and non-indestructible blocks.
        if (!configuration.creativeMode && Prefab.proxy.getServerConfiguration().allowBulldozerToCreateDrops && state.getBlock().getHarvestLevel(state) < 4 && state.getDestroySpeed(world, pos) >= 0.0f) {
            Block.dropResources(state, this.world, pos);
        }

        if (configuration.creativeMode && state.getBlock() instanceof FlowingFluidBlock) {
            // This is a fluid block. Replace it with stone, so it can be cleared.
            BuildingMethods.ReplaceBlock(this.world, pos, Blocks.STONE.defaultBlockState(), 2);
        }
    }

    @Override
    public void BeforeHangingEntityRemoved(HangingEntity hangingEntity) {
        // Only generate drops for this hanging entity if the bulldozer allows it.
        // By default, the base class doesn't allow hanging entities to generate drops.
        if (Prefab.proxy.getServerConfiguration().allowBulldozerToCreateDrops) {
            hangingEntity.dropItem(null);
        }
    }
}