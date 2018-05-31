package com.wuest.prefab.Structures.Base;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Config.StructurePartConfiguration.EnumStyle;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

/**
 * This enum is used to list the names of the structure materials.
 * 
 * @author WuestMan
 *
 */
public enum EnumStairsMaterial
{
	Brick("prefab.gui.material.brick", Blocks.BRICK_STAIRS.getDefaultState()),
	Cobblestone("prefab.gui.material.cobble_stone", Blocks.STONE_STAIRS.getDefaultState()),
	StoneBrick("prefab.gui.material.stone_brick", Blocks.STONE_BRICK_STAIRS.getDefaultState()),
	Granite("prefab.gui.material.granite", ModRegistry.GraniteStairs().getDefaultState()),
	Andesite("prefab.gui.material.andesite", ModRegistry.AndesiteStairs().getDefaultState()),
	Diorite("prefab.gui.material.diorite", ModRegistry.DioriteStairs().getDefaultState()),
	Oak("prefab.wall.block.type.oak", Blocks.OAK_STAIRS.getDefaultState()),
	Spruce("prefab.wall.block.type.spruce", Blocks.SPRUCE_STAIRS.getDefaultState()),
	Birch("prefab.wall.block.type.birch", Blocks.BIRCH_STAIRS.getDefaultState()),
	Jungle("prefab.wall.block.type.jungle", Blocks.JUNGLE_STAIRS.getDefaultState()),
	Acacia("prefab.wall.block.type.acacia", Blocks.ACACIA_STAIRS.getDefaultState()),
	DarkOak("prefab.wall.block.type.darkoak", Blocks.DARK_OAK_STAIRS.getDefaultState());

	private String name;
	public final IBlockState stairsState;

	private EnumStairsMaterial(String name, IBlockState stairsState)
	{
		this.name = name;
		this.stairsState = stairsState;
	}

	public String getTranslatedName()
	{
		return GuiLangKeys.translateString(this.name);
	}

	public IBlockState getFullBlock()
	{
		switch (this)
		{
			case Acacia:
			{
				return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}

			case Andesite:
			{
				return ModRegistry.AndesiteSlab().getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}

			case Birch:
			{
				return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}

			case Cobblestone:
			{
				return Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}
			
			case DarkOak:
			{
				return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}
			
			case Diorite:
			{
				return ModRegistry.DioriteSlab().getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}
			
			case Granite:
			{
				return ModRegistry.GraniteSlab().getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}
			
			case Jungle:
			{
				return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}
			
			case Oak:
			{
				return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}
			
			case Spruce:
			{
				return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}
			
			case StoneBrick:
			{
				return Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SMOOTHBRICK).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}
			
			case Brick:
			{
				return Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.BRICK).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}
			
			default:
			{
				return Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE).withProperty(BlockWoodSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			}
		}
	}

	public static EnumStairsMaterial getByOrdinal(int ordinal)
	{
		for (EnumStairsMaterial value : EnumStairsMaterial.values())
		{
			if (value.ordinal() == ordinal)
			{
				return value;
			}
		}

		return EnumStairsMaterial.Cobblestone;
	}
}
