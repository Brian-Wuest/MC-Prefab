package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.base.BuildClear;
import com.wuest.prefab.structures.base.BuildingMethods;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.BulldozerConfiguration;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.TierSortingRegistry;

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
        if (!configuration.creativeMode && Prefab.proxy.getServerConfiguration().allowBulldozerToCreateDrops
                && TierSortingRegistry.isCorrectTierForDrops(Tiers.DIAMOND, state) && state.getDestroySpeed(world, pos) >= 0.0f) {
            Block.dropResources(state, this.world, pos);
        }

        if (configuration.creativeMode && state.getBlock() instanceof LiquidBlock) {
            // This is a fluid block, replace it with stone so it can be cleared.
            BuildingMethods.ReplaceBlock(this.world, pos, Blocks.STONE);
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