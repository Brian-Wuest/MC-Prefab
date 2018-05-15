package com.wuest.prefab.Structures.Base;

import com.wuest.prefab.Gui.GuiLangKeys;

import net.minecraft.block.BlockPlanks;
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
	Cobblestone("prefab.gui.material.cobble_stone", Blocks.COBBLESTONE.getDefaultState(), 0),
	Stone("prefab.gui.material.stone", Blocks.STONE.getDefaultState(), 1),
	StoneBrick("prefab.gui.material.stone_brick", Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.EnumType.DEFAULT.getMetadata()), 2),
	Brick("prefab.gui.material.brick", Blocks.BRICK_BLOCK.getDefaultState(), 3),
	ChiseledStone("prefab.gui.material.chiseled_stone", Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.EnumType.CHISELED.getMetadata()), 4),
	Granite("prefab.gui.material.granite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.GRANITE.getMetadata()), 5),
	SmoothGranite("prefab.gui.material.smooth_granite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.GRANITE_SMOOTH.getMetadata()), 6),
	Andesite("prefab.gui.material.andesite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.ANDESITE.getMetadata()), 7),
	SmoothAndesite("prefab.gui.material.smooth_andesite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata()), 8),
	Diorite("prefab.gui.material.diorite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.DIORITE.getMetadata()), 9),
	SmoothDiorite("prefab.gui.material.smooth_diorite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.DIORITE_SMOOTH.getMetadata()), 10),
	Oak("prefab.wall.block.type.oak", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.OAK.getMetadata()), 11),
	Spruce("prefab.wall.block.type.spruce", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), 12),
	Birch("prefab.wall.block.type.birch", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.BIRCH.getMetadata()), 13),
	Jungle("prefab.wall.block.type.jungle", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.JUNGLE.getMetadata()), 14),
	Acacia("prefab.wall.block.type.acacia", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.ACACIA.getMetadata()), 15),
	DarkOak("prefab.wall.block.type.darkoak", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.DARK_OAK.getMetadata()), 16),;
	
	private String name;
	private IBlockState blockType;
	private int number;
	
	private EnumStructureMaterial(String name, IBlockState blockType, int number)
	{
		this.name = name;
		this.blockType = blockType;
		this.number = number;
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
