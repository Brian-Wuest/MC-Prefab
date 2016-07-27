package com.wuest.prefab;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.*;

import com.wuest.prefab.Items.*;
import com.wuest.prefab.Blocks.*;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Proxy.Messages.*;
import com.wuest.prefab.Proxy.Messages.Handlers.*;

import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

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
	
	public static ItemChickenCoop ChickenCoop()
	{
		return ModRegistry.GetItem(ItemChickenCoop.class);
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
	
	public static ItemProduceFarm ProduceFarm() 
	{
		return ModRegistry.GetItem(ItemProduceFarm.class);
	}
	
	public static ItemTreeFarm TreeFarm() 
	{
		return ModRegistry.GetItem(ItemTreeFarm.class);
	}
	
	public static ItemFishPond FishPond()
	{
		return ModRegistry.GetItem(ItemFishPond.class);
	}
	
	public static ItemPileOfBricks PileOfBricks() 
	{
		return ModRegistry.GetItem(ItemPileOfBricks.class);
	}
	
	public static ItemPalletOfBricks PalletOfBricks()
	{
		return ModRegistry.GetItem(ItemPalletOfBricks.class);
	}
	
	/**
	 * Gets the item from the ModItems collections.
	 * @param genericClass The class of item to get from the collection.
	 * @return Null if the item could not be found otherwise the item found.
	 */
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
	
	/**
	 * Gets the block from the ModBlockss collections.
	 * @param genericClass The class of block to get from the collection.
	 * @return Null if the block could not be found otherwise the block found.
	 */
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
		ModRegistry.registerItem(new ItemChickenCoop("itemChickenCoop"));
		ModRegistry.registerItem(new ItemProduceFarm("itemProduceFarm"));
		ModRegistry.registerItem(new ItemTreeFarm("itemTreeFarm"));
		ModRegistry.registerItem(new ItemCompressedChest("itemCompressedChest"));
		ModRegistry.registerItem(new ItemPileOfBricks("itemPileOfBricks"));
		ModRegistry.registerItem(new ItemPalletOfBricks("itemPalletOfBricks"));
		ModRegistry.registerItem(new ItemFishPond("itemFishPond"));
		
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
		
		// Compressed Dirt
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.COMPRESSED_DIRT.getMetadata()),
				"xxx",
				"xxx",
				"xxx",
				'x', Item.getItemFromBlock(Blocks.DIRT));
		
		GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(Blocks.DIRT), 9),
				"x",
				'x', new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.COMPRESSED_DIRT.getMetadata()));
		
		// Double Compressed Dirt
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT.getMetadata()),
				"xxx",
				"xxx",
				"xxx",
				'x', new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.COMPRESSED_DIRT.getMetadata()));
		
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedStoneItem(), 9, BlockCompressedStone.EnumType.COMPRESSED_DIRT.getMetadata()),
				"x",
				'x', new ItemStack(ModRegistry.CompressedStoneItem(), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT.getMetadata()));
		
		// Compressed Chest
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedChestItem()),
				"xxx",
				"xyx",
				"xxx",
				'x', Item.getItemFromBlock(Blocks.CHEST),
				'y', Item.getItemFromBlock(Blocks.ENDER_CHEST));
		
		// Pile of Bricks
		GameRegistry.addRecipe(new ItemStack(ModRegistry.PileOfBricks()),
				"xxx",
				"xxx",
				"xxx",
				'x', Items.BRICK);
		
		GameRegistry.addRecipe(new ItemStack(Items.BRICK, 9),
				"x",
				'x', ModRegistry.PileOfBricks());
		
		// Pallet of Bricks
		GameRegistry.addRecipe(new ItemStack(ModRegistry.PalletOfBricks()),
				"xxx",
				"xxx",
				"xxx",
				'x', ModRegistry.PileOfBricks());
		
		GameRegistry.addRecipe(new ItemStack(ModRegistry.PileOfBricks(), 9),
				"x",
				'x', ModRegistry.PalletOfBricks());
		
		// Warehouse
		GameRegistry.addRecipe(new ItemStack(ModRegistry.WareHouse()),
				"x x",
				"xyx",
				"zaz",
				'x', new ItemStack(Item.getItemFromBlock(ModRegistry.CompressedStoneBlock()), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE.getMetadata()),
				'y', ModRegistry.CompressedChestItem(),
				'z', new ItemStack(Item.getItemFromBlock(ModRegistry.CompressedStoneBlock()), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE.getMetadata()),
				'a', new ItemStack(Item.getItemFromBlock(ModRegistry.CompressedStoneBlock()), 1, BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE.getMetadata()));
		
		// Produce Farm.
		GameRegistry.addRecipe(new ItemStack(ModRegistry.ProduceFarm()),
				"aba",
				"cdc",
				"aba",
				'a', ModRegistry.PalletOfBricks(),
				'b', new ItemStack(ModRegistry.CompressedStoneBlock(), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT.getMetadata()),
				'c', Items.WATER_BUCKET,
				'd', new ItemStack(ModRegistry.CompressedStoneBlock(), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE.getMetadata()));
		
		// Tree Farm/Park
		GameRegistry.addRecipe(new ItemStack(ModRegistry.TreeFarm()),
				"aba",
				"cdc",
				"aba",
				'a', ModRegistry.PalletOfBricks(),
				'b', new ItemStack(ModRegistry.CompressedStoneBlock(), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT.getMetadata()),
				'c', Items.FLOWER_POT,
				'd', new ItemStack(ModRegistry.CompressedStoneBlock(), 1, BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE.getMetadata()));
		
		// Chicken Coop
		GameRegistry.addRecipe(new ItemStack(ModRegistry.ChickenCoop()),
				"eee",
				"aba",
				"cdc",
				'a', new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, BlockPlanks.EnumType.SPRUCE.getMetadata()),
				'b', Items.EGG,
				'c', new ItemStack(ModRegistry.CompressedStoneBlock(), 1, BlockCompressedStone.EnumType.COMPRESSED_DIRT.getMetadata()),
				'd', Item.getItemFromBlock(Blocks.HAY_BLOCK),
				'e', Item.getItemFromBlock(Blocks.BRICK_BLOCK));
	}

	/**
	 * This is where the mod messages are registered.
	 */
	public static void RegisterMessages()
	{
		Prefab.network.registerMessage(HouseHandler.class, HouseTagMessage.class, 1, Side.SERVER);
		Prefab.network.registerMessage(WareHouseHandler.class, WareHouseTagMessage.class, 2, Side.SERVER);
		Prefab.network.registerMessage(ConfigSyncHandler.class, ConfigSyncMessage.class, 3, Side.CLIENT);
		Prefab.network.registerMessage(ChickenCoopHandler.class, ChickenCoopTagMessage.class, 4, Side.SERVER);
		Prefab.network.registerMessage(ProduceFarmHandler.class, ProduceFarmTagMessage.class, 5, Side.SERVER);
		Prefab.network.registerMessage(TreeFarmHandler.class, TreeFarmTagMessage.class, 6, Side.SERVER);
		Prefab.network.registerMessage(FishPondHandler.class, FishPondTagMessage.class, 7, Side.SERVER);
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
