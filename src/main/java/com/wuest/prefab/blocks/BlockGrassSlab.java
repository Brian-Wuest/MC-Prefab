package com.wuest.prefab.blocks;

import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class BlockGrassSlab extends SlabBlock {
    public BlockGrassSlab() {
        super(BlockBehaviour.Properties.of(Material.GRASS, MaterialColor.GRASS).sound(SoundType.GRASS)
                .strength(0.5f, 0.5f));
    }
}
