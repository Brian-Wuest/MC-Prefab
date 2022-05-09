package com.wuest.prefab.blocks.entities;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.base.TileEntityBase;
import com.wuest.prefab.config.block_entities.DraftingTableConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DraftingTableBlockEntity extends TileEntityBase<DraftingTableConfiguration> {
    public DraftingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModRegistry.DraftingTableEntityType, pos, state);

        this.config = new DraftingTableConfiguration();
        this.config.blockPos = pos;
    }
}
