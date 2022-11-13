package com.wuest.prefab.structures.base;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.gui.GuiLangKeys;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;

/**
 * This enum is used to list the names of the structure materials.
 *
 * @author WuestMan
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public enum EnumStructureMaterial {
    Cobblestone("prefab.gui.material.cobble_stone", Blocks.COBBLESTONE.defaultBlockState(), Blocks.COBBLESTONE_STAIRS.defaultBlockState(), Blocks.COBBLESTONE_WALL.defaultBlockState(), 0),
    Stone("prefab.gui.material.stone", Blocks.STONE.defaultBlockState(), Blocks.STONE_STAIRS.defaultBlockState(), Blocks.COBBLESTONE_WALL.defaultBlockState(), 1),
    StoneBrick("prefab.gui.material.stone_brick", Blocks.STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICK_STAIRS.defaultBlockState(), Blocks.STONE_BRICK_WALL.defaultBlockState(), 2),
    Brick("prefab.gui.material.brick", Blocks.BRICKS.defaultBlockState(), Blocks.BRICK_STAIRS.defaultBlockState(), Blocks.BRICK_WALL.defaultBlockState(), 3),
    ChiseledStone("prefab.gui.material.chiseled_stone", Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICK_STAIRS.defaultBlockState(), Blocks.STONE_BRICK_WALL.defaultBlockState(), 4),
    Granite("prefab.gui.material.granite", Blocks.GRANITE.defaultBlockState(), Blocks.GRANITE_STAIRS.defaultBlockState(), Blocks.GRANITE_WALL.defaultBlockState(), 5),
    SmoothGranite("prefab.gui.material.smooth_granite", Blocks.POLISHED_GRANITE.defaultBlockState(), Blocks.POLISHED_GRANITE_STAIRS.defaultBlockState(), Blocks.GRANITE_WALL.defaultBlockState(), 6),
    Andesite("prefab.gui.material.andesite", Blocks.ANDESITE.defaultBlockState(), Blocks.ANDESITE_STAIRS.defaultBlockState(), Blocks.ANDESITE_WALL.defaultBlockState(), 7),
    SmoothAndesite("prefab.gui.material.smooth_andesite", Blocks.POLISHED_ANDESITE.defaultBlockState(), Blocks.POLISHED_ANDESITE_STAIRS.defaultBlockState(), Blocks.ANDESITE_WALL.defaultBlockState(), 8),
    Diorite("prefab.gui.material.diorite", Blocks.DIORITE.defaultBlockState(), Blocks.DIORITE_STAIRS.defaultBlockState(), Blocks.DIORITE_WALL.defaultBlockState(), 9),
    SmoothDiorite("prefab.gui.material.smooth_diorite", Blocks.POLISHED_DIORITE.defaultBlockState(), Blocks.POLISHED_DIORITE_STAIRS.defaultBlockState(), Blocks.DIORITE_WALL.defaultBlockState(), 10),
    Oak("prefab.wall.block.type.oak", Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_STAIRS.defaultBlockState(), Blocks.OAK_FENCE.defaultBlockState(), 11),
    Spruce("prefab.wall.block.type.spruce", Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_STAIRS.defaultBlockState(), Blocks.SPRUCE_FENCE.defaultBlockState(), 12),
    Birch("prefab.wall.block.type.birch", Blocks.BIRCH_PLANKS.defaultBlockState(), Blocks.BIRCH_STAIRS.defaultBlockState(), Blocks.BIRCH_FENCE.defaultBlockState(), 13),
    Jungle("prefab.wall.block.type.jungle", Blocks.JUNGLE_PLANKS.defaultBlockState(), Blocks.JUNGLE_STAIRS.defaultBlockState(), Blocks.JUNGLE_FENCE.defaultBlockState(), 14),
    Acacia("prefab.wall.block.type.acacia", Blocks.ACACIA_PLANKS.defaultBlockState(), Blocks.ACACIA_STAIRS.defaultBlockState(), Blocks.ACACIA_FENCE.defaultBlockState(), 15),
    DarkOak("prefab.wall.block.type.darkoak", Blocks.DARK_OAK_PLANKS.defaultBlockState(), Blocks.DARK_OAK_STAIRS.defaultBlockState(), Blocks.DARK_OAK_FENCE.defaultBlockState(), 16),
    SandStone("prefab.ceiling.block.type.sand", Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE_STAIRS.defaultBlockState(), Blocks.SANDSTONE_WALL.defaultBlockState(), 17),
    RedSandStone("prefab.gui.material.red_sandstone", Blocks.RED_SANDSTONE.defaultBlockState(), Blocks.RED_SANDSTONE_STAIRS.defaultBlockState(), Blocks.RED_SANDSTONE_WALL.defaultBlockState(), 18),
    Glass("block.minecraft.glass", Blocks.GLASS.defaultBlockState(), ModRegistry.GlassStairs.get().defaultBlockState(), Blocks.GLASS_PANE.defaultBlockState(), 19);

    private final String name;
    private final BlockState blockType;
    private final int number;
    private final BlockState stairsState;

    private final BlockState wallState;

    EnumStructureMaterial(String name, BlockState blockType, BlockState stairsState, BlockState wallState, int number) {
        this.name = name;
        this.blockType = blockType;
        this.number = number;
        this.stairsState = stairsState;
        this.wallState = wallState;
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

    public BlockState getWallBlock() {
        return this.wallState;
    }
}
