package com.wuest.prefab.Structures.Base;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Gui.GuiLangKeys;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

/**
 * This enum is used to list the names of the structure materials.
 * @author WuestMan
 *
 */
public enum EnumStructureMaterial
{
	Cobblestone("prefab.gui.material.cobble_stone", Blocks.COBBLESTONE.getDefaultState(), Blocks.STONE_STAIRS.getDefaultState(), 0),
	Stone("prefab.gui.material.stone", Blocks.STONE.getDefaultState(), Blocks.STONE_STAIRS.getDefaultState(), 1),
	StoneBrick("prefab.gui.material.stone_brick", Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.EnumType.DEFAULT.getMetadata()), Blocks.STONE_BRICK_STAIRS.getDefaultState(), 2),
	Brick("prefab.gui.material.brick", Blocks.BRICK_BLOCK.getDefaultState(), Blocks.BRICK_STAIRS.getDefaultState(), 3),
	ChiseledStone("prefab.gui.material.chiseled_stone", Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.EnumType.CHISELED.getMetadata()), Blocks.STONE_BRICK_STAIRS.getDefaultState(), 4),
	Granite("prefab.gui.material.granite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.GRANITE.getMetadata()), ModRegistry.GraniteStairs().getDefaultState(), 5),
	SmoothGranite("prefab.gui.material.smooth_granite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.GRANITE_SMOOTH.getMetadata()), ModRegistry.GraniteStairs().getDefaultState(), 6),
	Andesite("prefab.gui.material.andesite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.ANDESITE.getMetadata()), ModRegistry.AndesiteStairs().getDefaultState(), 7),
	SmoothAndesite("prefab.gui.material.smooth_andesite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata()), ModRegistry.AndesiteStairs().getDefaultState(), 8),
	Diorite("prefab.gui.material.diorite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.DIORITE.getMetadata()), ModRegistry.DioriteStairs().getDefaultState(), 9),
	SmoothDiorite("prefab.gui.material.smooth_diorite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.DIORITE_SMOOTH.getMetadata()), ModRegistry.DioriteStairs().getDefaultState(), 10),
	Oak("prefab.wall.block.type.oak", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.OAK.getMetadata()), Blocks.OAK_STAIRS.getDefaultState(), 11),
	Spruce("prefab.wall.block.type.spruce", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.SPRUCE_STAIRS.getDefaultState(), 12),
	Birch("prefab.wall.block.type.birch", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.BIRCH.getMetadata()), Blocks.BIRCH_STAIRS.getDefaultState(), 13),
	Jungle("prefab.wall.block.type.jungle", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.JUNGLE.getMetadata()), Blocks.JUNGLE_STAIRS.getDefaultState(), 14),
	Acacia("prefab.wall.block.type.acacia", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.ACACIA.getMetadata()), Blocks.ACACIA_STAIRS.getDefaultState(), 15),
	DarkOak("prefab.wall.block.type.darkoak", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.DARK_OAK.getMetadata()), Blocks.DARK_OAK_STAIRS.getDefaultState(), 16);
	
	private String name;
	private IBlockState blockType;
	private int number;
	private IBlockState stairsState;
	
	private EnumStructureMaterial(String name, IBlockState blockType, IBlockState stairsState, int number)
	{
		this.name = name;
		this.blockType = blockType;
		this.number = number;
		this.stairsState = stairsState;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public IBlockState getBlockType()
	{
		return this.blockType;
	}
	
	public int getNumber()
	{
		return this.number;
	}
	
	public String getTranslatedName()
	{
		return GuiLangKeys.translateString(this.name);
	}
	
	public IBlockState getStairsBlock()
	{
		return this.stairsState;
	}
	
	public static EnumStructureMaterial getMaterialByNumber(int number)
	{
		for (EnumStructureMaterial material : EnumStructureMaterial.values())
		{
			if (material.getNumber() == number)
			{
				return material;
			}
		}
		
		return EnumStructureMaterial.Cobblestone;
	}
}
