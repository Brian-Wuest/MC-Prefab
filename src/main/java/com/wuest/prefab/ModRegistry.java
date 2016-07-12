package com.wuest.prefab;

import java.util.*;
import java.util.Map.Entry;
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
	public static ArrayList<Item> ModItems = new ArrayList<Item>();
	public static ArrayList<Block> ModBlocks = new ArrayList<Block>();
	
	public static ItemStartHouse StartHouse()
	{
		return ModRegistry.GetItem(ItemStartHouse.class);
	}
	
	public static ItemWareHouse WareHouse()
	{
		return ModRegistry.GetItem(ItemWareHouse.class);
	}
	
	public static ItemBlockMeta CompressedStoneItem()
	{
		return ModRegistry.GetItem(ItemBlockMeta.class);
	}
	
	public static BlockCompressedStone CompressedStoneBlock()
	{
		return ModRegistry.GetBlock(BlockCompressedStone.class);
	}
	
	public static ItemCompressedChest CompressedChestItem() 
	{
		return ModRegistry.GetItem(ItemCompressedChest.class);
	}
	
	public static <T extends Item> T GetItem(Class<T> genericClass)
	{
		for (Item entry : ModRegistry.ModItems)
		{
			if (entry.getClass().isAssignableFrom(genericClass))
			{
				return (T)entry;
			}
		}
		
		return null;
	}
	
	public static <T extends Block> T GetBlock(Class<T> genericClass)
	{
		for (Block entry : ModRegistry.ModBlocks)
		{
			if (entry.getClass().isAssignableFrom(genericClass))
			{
				return (T)entry;
			}
		}
		
		return null;
	}
	
	/**
	 * This is where all in-game mod components (Items, Blocks) will be registered.
	 */
	public static void RegisterModComponents()
	{
		ModRegistry.registerItem(new ItemStartHouse("itemStartHouse"));
		ModRegistry.registerItem(new ItemWareHouse("itemWareHouse"));
		ModRegistry.registerItem(new ItemCompressedChest("itemCompressedChest"));
		
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
		
		// Compressed Glowstone.
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE.getMetadata()),
				"xxx",
				"xxx",
				"xxx",
				'x', Item.getItemFromBlock(Blocks.GLOWSTONE));
		
		GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(Blocks.GLOWSTONE), 9),
				"x",
				'x', new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE.getMetadata()));
		
		// Double Compressed Glowstone.
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE.getMetadata()),
				"xxx",
				"xxx",
				"xxx",
				'x', new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE.getMetadata()));
		
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedStoneItem(), 9, BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE.getMetadata()),
				"x",
				'x', new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE.getMetadata()));
		
		// Compressed Chest
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedChestItem()),
				"xxx",
				"xyx",
				"xxx",
				'x', Item.getItemFromBlock(Blocks.CHEST),
				'y', Item.getItemFromBlock(Blocks.ENDER_CHEST));
		
		// Warehouse
		GameRegistry.addRecipe(new ItemStack(ModRegistry.WareHouse()),
				"x x",
				"xyx",
				"zaz",
				'x', new ItemStack(Item.getItemFromBlock(ModRegistry.CompressedStoneBlock()), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE.getMetadata()),
				'y', ModRegistry.CompressedChestItem(),
				'z', new ItemStack(Item.getItemFromBlock(ModRegistry.CompressedStoneBlock()), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE.getMetadata()),
				'a', new ItemStack(Item.getItemFromBlock(ModRegistry.CompressedStoneBlock()), 1, BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE.getMetadata()));
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
		ModRegistry.ModItems.add(item);

		return item;
	}
	
	public static <T extends Block> T registerBlock(T block)
	{
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		ModRegistry.ModBlocks.add(block);
		
		return block;
	}
	
	public static <T extends Block, I extends ItemBlock> T registerBlock(T block, I itemBlock)
	{
		GameRegistry.register(block);
		ModRegistry.ModBlocks.add(block);
		
		if (itemBlock != null)
		{
			GameRegistry.register(itemBlock);
			ModRegistry.ModItems.add(itemBlock);
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
