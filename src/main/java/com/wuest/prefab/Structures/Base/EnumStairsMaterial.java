package com.wuest.prefab.Structures.Base;

import com.wuest.prefab.Gui.GuiLangKeys;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

/**
 * This enum is used to list the names of the structure materials.
 *
 * @author WuestMan
 */
public enum EnumStairsMaterial {
    Brick("prefab.gui.material.brick", Blocks.BRICK_STAIRS.getDefaultState()),
    Cobblestone("prefab.gui.material.cobble_stone", Blocks.STONE_STAIRS.getDefaultState()),
    StoneBrick("prefab.gui.material.stone_brick", Blocks.STONE_BRICK_STAIRS.getDefaultState()),
    Granite("prefab.gui.material.granite", Blocks.POLISHED_GRANITE_STAIRS.getDefaultState()),
    Andesite("prefab.gui.material.andesite", Blocks.POLISHED_ANDESITE_STAIRS.getDefaultState()),
    Diorite("prefab.gui.material.diorite", Blocks.POLISHED_DIORITE_STAIRS.getDefaultState()),
    Oak("prefab.wall.block.type.oak", Blocks.OAK_STAIRS.getDefaultState()),
    Spruce("prefab.wall.block.type.spruce", Blocks.SPRUCE_STAIRS.getDefaultState()),
    Birch("prefab.wall.block.type.birch", Blocks.BIRCH_STAIRS.getDefaultState()),
    Jungle("prefab.wall.block.type.jungle", Blocks.JUNGLE_STAIRS.getDefaultState()),
    Acacia("prefab.wall.block.type.acacia", Blocks.ACACIA_STAIRS.getDefaultState()),
    DarkOak("prefab.wall.block.type.darkoak", Blocks.DARK_OAK_STAIRS.getDefaultState());

    public final BlockState stairsState;
    private String name;

    EnumStairsMaterial(String name, BlockState stairsState) {
        this.name = name;
        this.stairsState = stairsState;
    }

    public static EnumStairsMaterial getByOrdinal(int ordinal) {
        for (EnumStairsMaterial value : EnumStairsMaterial.values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }

        return EnumStairsMaterial.Cobblestone;
    }

    public String getTranslatedName() {
        return GuiLangKeys.translateString(this.name);
    }

    public BlockState getFullBlock() {
        switch (this) {
            case Acacia: {
                return Blocks.ACACIA_SLAB.getDefaultState();
            }

            case Andesite: {
                return Blocks.POLISHED_ANDESITE_SLAB.getDefaultState();
            }

            case Birch: {
                return Blocks.BIRCH_SLAB.getDefaultState();
            }

            case Cobblestone: {
                return Blocks.COBBLESTONE_SLAB.getDefaultState();
            }

            case DarkOak: {
                return Blocks.DARK_OAK_SLAB.getDefaultState();
            }

            case Diorite: {
                return Blocks.POLISHED_DIORITE_SLAB.getDefaultState();
            }

            case Granite: {
                return Blocks.POLISHED_ANDESITE_SLAB.getDefaultState();
            }

            case Jungle: {
                return Blocks.JUNGLE_SLAB.getDefaultState();
            }

            case Oak: {
                return Blocks.OAK_SLAB.getDefaultState();
            }

            case Spruce: {
                return Blocks.SPRUCE_SLAB.getDefaultState();
            }

            case StoneBrick: {
                return Blocks.STONE_BRICK_SLAB.getDefaultState();
            }

            case Brick: {
                return Blocks.BRICK_SLAB.getDefaultState();
            }

            default: {
                return Blocks.STONE_SLAB.getDefaultState();
            }
        }
    }
}
