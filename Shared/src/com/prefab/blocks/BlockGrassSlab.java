package com.prefab.blocks;

import com.prefab.PrefabBase;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class BlockGrassSlab extends SlabBlock {
    public BlockGrassSlab() {
        super(BlockBehaviour.Properties
                .ofFullCopy(Blocks.GRASS_BLOCK)
                .setId(ResourceKey.create(Registries.BLOCK,
                        Identifier.fromNamespaceAndPath(PrefabBase.MODID,
                                "block_grass_slab")))
                .mapColor(MapColor.GRASS)
                .sound(SoundType.GRASS)
                .strength(0.5f, 0.5f));

    }
}
