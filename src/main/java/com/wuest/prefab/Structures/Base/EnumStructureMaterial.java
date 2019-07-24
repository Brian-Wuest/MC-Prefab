package com.wuest.prefab.Structures.Base;

import com.wuest.prefab.Gui.GuiLangKeys;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

/**
 * This enum is used to list the names of the structure materials.
 *
 * @author WuestMan
 */
public enum EnumStructureMaterial {
    Cobblestone("prefab.gui.material.cobble_stone", Blocks.COBBLESTONE.getDefaultState(), Blocks.STONE_STAIRS.getDefaultState(), 0),
    Stone("prefab.gui.material.stone", Blocks.STONE.getDefaultState(), Blocks.STONE_STAIRS.getDefaultState(), 1),
    StoneBrick("prefab.gui.material.stone_brick", Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICK_STAIRS.getDefaultState(), 2),
    Brick("prefab.gui.material.brick", Blocks.BRICKS.getDefaultState(), Blocks.BRICK_STAIRS.getDefaultState(), 3),
    ChiseledStone("prefab.gui.material.chiseled_stone", Blocks.CHISELED_STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICK_STAIRS.getDefaultState(), 4),
    Granite("prefab.gui.material.granite", Blocks.GRANITE.getDefaultState(), Blocks.GRANITE_STAIRS.getDefaultState(), 5),
    SmoothGranite("prefab.gui.material.smooth_granite", Blocks.POLISHED_GRANITE.getDefaultState(), Blocks.POLISHED_GRANITE_STAIRS.getDefaultState(), 6),
    Andesite("prefab.gui.material.andesite", Blocks.ANDESITE.getDefaultState(), Blocks.ANDESITE_STAIRS.getDefaultState(), 7),
    SmoothAndesite("prefab.gui.material.smooth_andesite", Blocks.POLISHED_ANDESITE.getDefaultState(), Blocks.POLISHED_ANDESITE_STAIRS.getDefaultState(), 8),
    Diorite("prefab.gui.material.diorite", Blocks.DIORITE.getDefaultState(), Blocks.DIORITE_STAIRS.getDefaultState(), 9),
    SmoothDiorite("prefab.gui.material.smooth_diorite", Blocks.POLISHED_DIORITE.getDefaultState(), Blocks.POLISHED_DIORITE_STAIRS.getDefaultState(), 10),
    Oak("prefab.wall.block.type.oak", Blocks.OAK_PLANKS.getDefaultState(), Blocks.OAK_STAIRS.getDefaultState(), 11),
    Spruce("prefab.wall.block.type.spruce", Blocks.SPRUCE_PLANKS.getDefaultState(), Blocks.SPRUCE_STAIRS.getDefaultState(), 12),
    Birch("prefab.wall.block.type.birch", Blocks.BIRCH_PLANKS.getDefaultState(), Blocks.BIRCH_STAIRS.getDefaultState(), 13),
    Jungle("prefab.wall.block.type.jungle", Blocks.JUNGLE_PLANKS.getDefaultState(), Blocks.JUNGLE_STAIRS.getDefaultState(), 14),
    Acacia("prefab.wall.block.type.acacia", Blocks.ACACIA_PLANKS.getDefaultState(), Blocks.ACACIA_STAIRS.getDefaultState(), 15),
    DarkOak("prefab.wall.block.type.darkoak", Blocks.DARK_OAK_PLANKS.getDefaultState(), Blocks.DARK_OAK_STAIRS.getDefaultState(), 16);

    private String name;
    private BlockState blockType;
    private int number;
    private BlockState stairsState;

    EnumStructureMaterial(String name, BlockState blockType, BlockState stairsState, int number) {
        this.name = name;
        this.blockType = blockType;
        this.number = number;
        this.stairsState = stairsState;
    }

    public static EnumStructureMaterial getMaterialByNumber(int number) {
        for (EnumStructureMaterial material : EnumStructureMaterial.values()) {
            if (material.getNumber() == number) {
                return material;
            }
        }

        return EnumStructureMaterial.Cobblestone;
    }

    public String getName() {
        return this.name;
    }

    public BlockState getBlockType() {
        return this.blockType;
    }

    public int getNumber() {
        return this.number;
    }

    public String getTranslatedName() {
        return GuiLangKeys.translateString(this.name);
    }

    public BlockState getStairsBlock() {
        return this.stairsState;
    }
}
