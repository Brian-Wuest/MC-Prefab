package com.wuest.prefab;

import java.util.*;
import java.util.stream.*;

import com.wuest.prefab.Items.*;
import com.wuest.prefab.Blocks.*;
import com.wuest.prefab.Proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This is the mod registry so there is a way to get to all instances of the blocks/items created by this mod.
 * @author WuestMan
 *
 */
public class ModRegistry
{
	public static HashMap<String, Item> ModItems = new HashMap<String, Item>();
	public static HashMap<String, Block> ModBlocks = new HashMap<String, Block>();
	
	public static ItemStartHouse StartHouse()
	{
		return (ItemStartHouse) ModRegistry.ModItems.get("item.prefab:itemStartHouse");
	}
	
	public static ItemBlock CompressedStoneItem()
	{
		return (ItemBlock) ModRegistry.ModItems.get("tile.prefab:blockCompressedStone");
	}
	
	public static Block CompressedStoneBlock()
	{
		return (Block) ModRegistry.ModBlocks.get("tile.prefab:blockCompressedStone");
	}
	
	/**
	 * This is where all in-game mod components (Items, Blocks) will be registered.
	 */
	public static void RegisterModComponents()
	{
		ModRegistry.registerItem(new ItemStartHouse("itemStartHouse"));
		
		// Create/register the item block with this block as it's needed due to this being a meta data block.
		BlockCompressedStone stone = new BlockCompressedStone();
		ItemBlockMeta meta = new ItemBlockMeta(stone);
		ModRegistry.setItemName(meta, "blockCompressedStone");
		ModRegistry.registerBlock(stone, meta);
	}
	
	/**
	 * This is where the mod recipes are registered.
	 */
	public static void RegisterRecipes()
	{
		// Compressed Stone.
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.COMPRESSED_STONE.getMetadata()),
				"xxx",
				"xxx",
				"xxx",
				'x', Item.getItemFromBlock(Blocks.STONE));
		
		GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(Blocks.STONE), 9), 
				"x",
				'x', new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.COMPRESSED_STONE.getMetadata()));
		
		// Double Compressed Stone.
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE.getMetadata()),
				"xxx",
				"xxx",
				"xxx",
				'x', new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.COMPRESSED_STONE.getMetadata()));
		
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedStoneItem(), 9, BlockCompressedStone.EnumType.COMPRESSED_STONE.getMetadata()),
				"x",
				'x', new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE.getMetadata()));
		
		// Triple Compressed Stone.
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE.getMetadata()),
				"xxx",
				"xxx",
				"xxx",
				'x', new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE.getMetadata()));
		
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedStoneItem(), 9, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE.getMetadata()),
				"x",
				'x', new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE.getMetadata()));
	}
	
	/**
	 * Register an Item
	 *
	 * @param item The Item instance
	 * @param <T> The Item type
	 * @return The Item instance
	 */
	public static <T extends Item> T registerItem(T item)
	{
		GameRegistry.register(item);
		ModRegistry.ModItems.put(item.getUnlocalizedName(), item);

		return item;
	}
	
	public static <T extends Block> T registerBlock(T block)
	{
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		ModRegistry.ModBlocks.put(block.getUnlocalizedName(), block);
		
		return block;
	}
	
	public static <T extends Block, I extends ItemBlock> T registerBlock(T block, I itemBlock)
	{
		GameRegistry.register(block);
		ModRegistry.ModBlocks.put(block.getUnlocalizedName(), block);
		
		if (itemBlock != null)
		{
			GameRegistry.register(itemBlock);
			ModRegistry.ModItems.put(itemBlock.getUnlocalizedName(), itemBlock);
		}
		
		return block;
	}
	
	/**
	 * Set the registry name of {@code item} to {@code itemName} and the un-localised name to the full registry name.
	 *
	 * @param item     The item
	 * @param itemName The item's name
	 */
	public static void setItemName(Item item, String itemName) 
	{
		item.setRegistryName(itemName);
		item.setUnlocalizedName(item.getRegistryName().toString());
	}
	
	/**
	 * Set the registry name of {@code block} to {@code blockName} and the un-localised name to the full registry name.
	 *
	 * @param block     The block
	 * @param blockName The block's name
	 */
	public static void setBlockName(Block block, String blockName) 
	{
		block.setRegistryName(blockName);
		block.setUnlocalizedName(block.getRegistryName().toString());
	}
}
