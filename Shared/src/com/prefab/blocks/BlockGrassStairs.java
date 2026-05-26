package com.prefab.blocks;

import com.prefab.PrefabBase;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * This class defines a set of grass stairs.
 *
 * @author WuestMan
 */
public class BlockGrassStairs extends StairBlock {

    public BlockGrassStairs() {
        super(Blocks.GRASS_BLOCK.defaultBlockState(),
                BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
                        .setId(ResourceKey.create(Registries.BLOCK,
                                ResourceLocation.fromNamespaceAndPath(PrefabBase.MODID,
                                        "block_grass_stairs")))
        );
    }
}
