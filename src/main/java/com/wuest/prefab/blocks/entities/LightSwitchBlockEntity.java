package com.wuest.prefab.blocks.entities;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.base.TileEntityBase;
import com.wuest.prefab.config.LightSwitchConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LightSwitchBlockEntity extends TileEntityBase<LightSwitchConfig> {
    public LightSwitchBlockEntity(BlockPos pos, BlockState state) {
        super(ModRegistry.LightSwitchEntityType, pos, state);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);

        if (!level.isClientSide) {
            ModRegistry.serverModRegistries.getLightSwitchRegistry().register(level, this.worldPosition);
        }
    }
}
