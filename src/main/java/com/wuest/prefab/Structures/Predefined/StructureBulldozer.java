package com.wuest.prefab.Structures.Predefined;

import com.wuest.prefab.Structures.Base.BuildBlock;
import com.wuest.prefab.Structures.Base.BuildClear;
import com.wuest.prefab.Structures.Base.Structure;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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

        // Only harvest up to diamond level and non-indestructable blocks.
        if (state.getBlock().getHarvestLevel(state) < 4 && state.getBlockHardness(world, pos) >= 0.0f) {
            Block.spawnDrops(state, this.world, pos);
        }
    }
}