package com.prefab.blocks.entities;

import com.prefab.ModRegistryBase;
import com.prefab.base.TileEntityBase;
import com.prefab.config.LightSwitchConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LightSwitchBlockEntity extends TileEntityBase<LightSwitchConfig> {
    public LightSwitchBlockEntity(BlockPos pos, BlockState state) {
        super(ModRegistryBase.LightSwitchEntityType, pos, state);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);

        if (!level.isClientSide()) {
            ModRegistryBase.serverModRegistries.getLightSwitchRegistry().register(level, this.worldPosition);
        }
    }
}
