package com.wuest.prefab.structures.base;

import com.wuest.prefab.gui.GuiLangKeys;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;

/**
 * This enum is used to list the names of the structure materials.
 *
 * @author WuestMan
 */
@SuppressWarnings("SpellCheckingInspection")
public enum EnumStairsMaterial {
    Brick("prefab.gui.material.brick", Blocks.BRICK_STAIRS.defaultBlockState()),
    Cobblestone("prefab.gui.material.cobble_stone", Blocks.COBBLESTONE_STAIRS.defaultBlockState()),
    StoneBrick("prefab.gui.material.stone_brick", Blocks.STONE_BRICK_STAIRS.defaultBlockState()),
    Granite("prefab.gui.material.granite", Blocks.POLISHED_GRANITE_STAIRS.defaultBlockState()),
    Andesite("prefab.gui.material.andesite", Blocks.POLISHED_ANDESITE_STAIRS.defaultBlockState()),
    Diorite("prefab.gui.material.diorite", Blocks.POLISHED_DIORITE_STAIRS.defaultBlockState()),
    Oak("prefab.wall.block.type.oak", Blocks.OAK_STAIRS.defaultBlockState()),
    Spruce("prefab.wall.block.type.spruce", Blocks.SPRUCE_STAIRS.defaultBlockState()),
    Birch("prefab.wall.block.type.birch", Blocks.BIRCH_STAIRS.defaultBlockState()),
    Jungle("prefab.wall.block.type.jungle", Blocks.JUNGLE_STAIRS.defaultBlockState()),
    Acacia("prefab.wall.block.type.acacia", Blocks.ACACIA_STAIRS.defaultBlockState()),
    DarkOak("prefab.wall.block.type.darkoak", Blocks.DARK_OAK_STAIRS.defaultBlockState()),
    SandStone("prefab.ceiling.block.type.sand", Blocks.SANDSTONE_STAIRS.defaultBlockState()),
    RedSandStone("prefab.gui.material.red_sandstone", Blocks.RED_SANDSTONE_STAIRS.defaultBlockState());

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
                return Blocks.ACACIA_SLAB.defaultBlockState();
            }

            case Andesite: {
                return Blocks.POLISHED_ANDESITE_SLAB.defaultBlockState();
            }

            case Birch: {
                return Blocks.BIRCH_SLAB.defaultBlockState();
            }

            case Cobblestone: {
                return Blocks.COBBLESTONE_SLAB.defaultBlockState();
            }

            case DarkOak: {
                return Blocks.DARK_OAK_SLAB.defaultBlockState();
            }

            case Diorite: {
                return Blocks.POLISHED_DIORITE_SLAB.defaultBlockState();
            }

            case Granite: {
                return Blocks.POLISHED_GRANITE_SLAB.defaultBlockState();
            }

            case Jungle: {
                return Blocks.JUNGLE_SLAB.defaultBlockState();
            }

            case Oak: {
                return Blocks.OAK_SLAB.defaultBlockState();
            }

            case Spruce: {
                return Blocks.SPRUCE_SLAB.defaultBlockState();
            }

            case StoneBrick: {
                return Blocks.STONE_BRICK_SLAB.defaultBlockState();
            }

            case Brick: {
                return Blocks.BRICK_SLAB.defaultBlockState();
            }

            case SandStone: {
                return Blocks.SANDSTONE_SLAB.defaultBlockState();
            }

            case RedSandStone: {
                return Blocks.RED_SANDSTONE_SLAB.defaultBlockState();
            }

            default: {
                return Blocks.STONE_SLAB.defaultBlockState();
            }
        }
    }
}
