package com.wuest.prefab.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public class BlockGrassSlab extends SlabBlock {
    public BlockGrassSlab() {
        super(AbstractBlock.Properties.of(Material.GRASS, MaterialColor.GRASS).sound(SoundType.GRASS)
                .strength(0.5f, 0.5f)
                .harvestTool(ToolType.SHOVEL)
                .harvestLevel(0));
    }
}
