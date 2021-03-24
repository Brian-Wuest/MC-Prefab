package com.wuest.prefab.Structures.Base;

import com.wuest.prefab.Gui.GuiLangKeys;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

/**
 * This enum is used to list the names of the structure materials.
 *
 * @author WuestMan
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public enum EnumStructureMaterial {
	Cobblestone("prefab.gui.material.cobble_stone", Blocks.COBBLESTONE.defaultBlockState(), Blocks.STONE_STAIRS.defaultBlockState(), 0),
	Stone("prefab.gui.material.stone", Blocks.STONE.defaultBlockState(), Blocks.STONE_STAIRS.defaultBlockState(), 1),
	StoneBrick("prefab.gui.material.stone_brick", Blocks.STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICK_STAIRS.defaultBlockState(), 2),
	Brick("prefab.gui.material.brick", Blocks.BRICKS.defaultBlockState(), Blocks.BRICK_STAIRS.defaultBlockState(), 3),
	ChiseledStone("prefab.gui.material.chiseled_stone", Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICK_STAIRS.defaultBlockState(), 4),
	Granite("prefab.gui.material.granite", Blocks.GRANITE.defaultBlockState(), Blocks.GRANITE_STAIRS.defaultBlockState(), 5),
	SmoothGranite("prefab.gui.material.smooth_granite", Blocks.POLISHED_GRANITE.defaultBlockState(), Blocks.POLISHED_GRANITE_STAIRS.defaultBlockState(), 6),
	Andesite("prefab.gui.material.andesite", Blocks.ANDESITE.defaultBlockState(), Blocks.ANDESITE_STAIRS.defaultBlockState(), 7),
	SmoothAndesite("prefab.gui.material.smooth_andesite", Blocks.POLISHED_ANDESITE.defaultBlockState(), Blocks.POLISHED_ANDESITE_STAIRS.defaultBlockState(), 8),
	Diorite("prefab.gui.material.diorite", Blocks.DIORITE.defaultBlockState(), Blocks.DIORITE_STAIRS.defaultBlockState(), 9),
	SmoothDiorite("prefab.gui.material.smooth_diorite", Blocks.POLISHED_DIORITE.defaultBlockState(), Blocks.POLISHED_DIORITE_STAIRS.defaultBlockState(), 10),
	Oak("prefab.wall.block.type.oak", Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_STAIRS.defaultBlockState(), 11),
	Spruce("prefab.wall.block.type.spruce", Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_STAIRS.defaultBlockState(), 12),
	Birch("prefab.wall.block.type.birch", Blocks.BIRCH_PLANKS.defaultBlockState(), Blocks.BIRCH_STAIRS.defaultBlockState(), 13),
	Jungle("prefab.wall.block.type.jungle", Blocks.JUNGLE_PLANKS.defaultBlockState(), Blocks.JUNGLE_STAIRS.defaultBlockState(), 14),
	Acacia("prefab.wall.block.type.acacia", Blocks.ACACIA_PLANKS.defaultBlockState(), Blocks.ACACIA_STAIRS.defaultBlockState(), 15),
	DarkOak("prefab.wall.block.type.darkoak", Blocks.DARK_OAK_PLANKS.defaultBlockState(), Blocks.DARK_OAK_STAIRS.defaultBlockState(), 16);

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
