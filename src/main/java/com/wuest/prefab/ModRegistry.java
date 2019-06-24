package com.wuest.prefab;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.wuest.prefab.Blocks.BlockBoundary;
import com.wuest.prefab.Blocks.BlockCompressedObsidian;
import com.wuest.prefab.Blocks.BlockCompressedStone;
import com.wuest.prefab.Blocks.BlockPaperLantern;
import com.wuest.prefab.Blocks.BlockPhasing;
import com.wuest.prefab.Blocks.BlockStairs;
import com.wuest.prefab.Gui.GuiDrafter;
import com.wuest.prefab.Items.ItemBlockAndesiteSlab;
import com.wuest.prefab.Items.ItemBlockDioriteSlab;
import com.wuest.prefab.Items.ItemBlockGlassSlab;
import com.wuest.prefab.Items.ItemBlockGraniteSlab;
import com.wuest.prefab.Items.ItemBlockMeta;
import com.wuest.prefab.Items.ItemBundleOfTimber;
import com.wuest.prefab.Items.ItemCoilOfLanterns;
import com.wuest.prefab.Items.ItemCompressedChest;
import com.wuest.prefab.Items.ItemPalletOfBricks;
import com.wuest.prefab.Items.ItemPileOfBricks;
import com.wuest.prefab.Items.ItemStringOfLanterns;
import com.wuest.prefab.Items.ItemWarehouseUpgrade;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;
import com.wuest.prefab.Proxy.Messages.PlayerEntityTagMessage;
import com.wuest.prefab.Proxy.Messages.Handlers.ConfigSyncHandler;
import com.wuest.prefab.Proxy.Messages.Handlers.PlayerEntityHandler;
import com.wuest.prefab.Structures.Capabilities.IStructureConfigurationCapability;
import com.wuest.prefab.Structures.Capabilities.StructureConfigurationCapability;
import com.wuest.prefab.Structures.Capabilities.Storage.StructureConfigurationStorage;
import com.wuest.prefab.Structures.Gui.GuiAdvancedWareHouse;
import com.wuest.prefab.Structures.Gui.GuiBasicStructure;
import com.wuest.prefab.Structures.Gui.GuiBulldozer;
import com.wuest.prefab.Structures.Gui.GuiChickenCoop;
import com.wuest.prefab.Structures.Gui.GuiFishPond;
import com.wuest.prefab.Structures.Gui.GuiHorseStable;
import com.wuest.prefab.Structures.Gui.GuiInstantBridge;
import com.wuest.prefab.Structures.Gui.GuiModerateHouse;
import com.wuest.prefab.Structures.Gui.GuiModularHouse;
import com.wuest.prefab.Structures.Gui.GuiMonsterMasher;
import com.wuest.prefab.Structures.Gui.GuiNetherGate;
import com.wuest.prefab.Structures.Gui.GuiProduceFarm;
import com.wuest.prefab.Structures.Gui.GuiStartHouseChooser;
import com.wuest.prefab.Structures.Gui.GuiStructurePart;
import com.wuest.prefab.Structures.Gui.GuiTreeFarm;
import com.wuest.prefab.Structures.Gui.GuiVillaerHouses;
import com.wuest.prefab.Structures.Gui.GuiWareHouse;
import com.wuest.prefab.Structures.Items.ItemAdvancedWareHouse;
import com.wuest.prefab.Structures.Items.ItemBasicStructure;
import com.wuest.prefab.Structures.Items.ItemBulldozer;
import com.wuest.prefab.Structures.Items.ItemChickenCoop;
import com.wuest.prefab.Structures.Items.ItemFishPond;
import com.wuest.prefab.Structures.Items.ItemHorseStable;
import com.wuest.prefab.Structures.Items.ItemInstantBridge;
import com.wuest.prefab.Structures.Items.ItemModerateHouse;
import com.wuest.prefab.Structures.Items.ItemModularHouse;
import com.wuest.prefab.Structures.Items.ItemMonsterMasher;
import com.wuest.prefab.Structures.Items.ItemNetherGate;
import com.wuest.prefab.Structures.Items.ItemProduceFarm;
import com.wuest.prefab.Structures.Items.ItemStartHouse;
import com.wuest.prefab.Structures.Items.ItemStructurePart;
import com.wuest.prefab.Structures.Items.ItemTreeFarm;
import com.wuest.prefab.Structures.Items.ItemVillagerHouses;
import com.wuest.prefab.Structures.Items.ItemWareHouse;
import com.wuest.prefab.Structures.Messages.StructureHandler;
import com.wuest.prefab.Structures.Messages.StructureTagMessage;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * This is the mod registry so there is a way to get to all instances of the blocks/items created by this mod.
 * 
 * @author WuestMan
 *
 */
public class ModRegistry
{
	/**
	 * The compressed dirt ore dictionary name.
	 */
	public static final String CompressedDirt = "compressedDirt1";

	/**
	 * The double compressed dirt ore dictionary name.
	 */
	public static final String DoubleCompressedDirt = "compressedDirt2";

	/**
	 * The compressed stone ore dictionary name.
	 */
	public static final String CompressedStone = "compressedStone1";

	/**
	 * The double compressed stone ore dictionary name.
	 */
	public static final String DoubleCompressedStone = "compressedStone2";

	/**
	 * The triple compressed stone ore dictionary name.
	 */
	public static final String TripleCompressedStone = "compressedStone3";

	/**
	 * The compressed glowstone ore dictionary name.
	 */
	public static final String CompressedGlowstone = "compressedGlowstone1";

	/**
	 * The double compressed glowstone ore dictionary name.
	 */
	public static final String DoubleCompressedGlowstone = "compressedGlowstone2";

	/**
	 * The compressed obsidian ore dictionary name.
	 */
	public static final String CompressedObsidian = "compressedObsidian1";

	/**
	 * The double compressed obsidian ore dictionary name
	 */
	public static final String DoubleCompressedObsidian = "compressedObsidian2";

	/**
	 * The ArrayList of mod registered items.
	 */
	public static ArrayList<Item> ModItems = new ArrayList<Item>();

	/**
	 * The ArrayList of mod registered blocks.
	 */
	public static ArrayList<Block> ModBlocks = new ArrayList<Block>();

	/**
	 * The hashmap of mod guis.
	 */
	public static HashMap<String, Class> ModGuis = new HashMap<String, Class>();

	/**
	 * The identifier for the WareHouse GUI.
	 */
	public static final String GuiWareHouse = "Warehouse";

	/**
	 * The identifier for the Chicken coop GUI.
	 */
	public static final String GuiChickenCoop = "ChickenCoop";

	/**
	 * The identifier for the Produce Farm GUI.
	 */
	public static final String GuiProduceFarm = "ProduceFarm";

	/**
	 * The identifier for the Tree Farm GUI.
	 */
	public static final String GuiTreeFarm = "TreeFarm";

	/**
	 * The identifier for the FishPond GUI.
	 */
	public static final String GuiFishPond = "FishPond";

	/**
	 * The identifier for the Starting House GUI.
	 */
	public static final String GuiStartHouseChooser = "StartHouseChooser";

	/**
	 * The identifier for the Advanced WareHouse GUI.
	 */
	public static final String GuiAdvancedWareHouse = "AdvancedWarehouse";

	/**
	 * The identifier for the Monster Masher GUI.
	 */
	public static final String GuiMonsterMasher = "MonsterMasher";

	/**
	 * The identifier for the Horse Stable GUI.
	 */
	public static final String GuiHorseStable = "HorseStable";

	/**
	 * The identifier for the Nether Gate GUI.
	 */
	public static final String GuiNetherGate = "NetherGate";

	/**
	 * The identifier for the Modular House GUI.
	 */
	public static final String GuiModularHouse = "ModularHouse";

	/**
	 * The identifier for the Drafter GUI.
	 */
	public static final String GuiDrafter = "Drafter";

	/**
	 * The identifier for the Basic structure GUI.
	 */
	public static final String GuiBasicStructure = "BasicStructure";

	/**
	 * The identifier for the Villaer Houses GUI.
	 */
	public static final String GuiVillagerHouses = "VillagerHouses";

	/**
	 * The identifier for the moderate house GUI.
	 */
	public static final String GuiModerateHouse = "ModerateHouse";

	/**
	 * The identifier for the bulldozer GUI.
	 */
	public static final String GuiBulldozer = "Bulldozer";

	/**
	 * The identifier for the instant bridge gui.
	 */
	public static final String GuiInstantBridge = "InstantBridge";
	
	/**
	 * The identifier for the structure part gui.
	 */
	public static final String GuiStructurePart = "StructurePart";

	/**
	 * This capability is used to save the locations where a player spawns when transferring dimensions.
	 */
	@CapabilityInject(IStructureConfigurationCapability.class)
	public static Capability<IStructureConfigurationCapability> StructureConfiguration = null;
	
	private static ArrayList<BlockCompressedStone> CompressedStones = new ArrayList<BlockCompressedStone>();
	private static ArrayList<BlockCompressedObsidian> CompressedObsidians = new ArrayList<BlockCompressedObsidian>();

	/**
	 * The starting house registered item.
	 * 
	 * @return An instance of {@link ItemStartHouse}.
	 */
	public static ItemStartHouse StartHouse()
	{
		return ModRegistry.GetItem(ItemStartHouse.class);
	}

	/**
	 * The warehouse registered item.
	 * 
	 * @return An instance of {@link ItemWareHouse}.
	 */
	public static ItemWareHouse WareHouse()
	{
		return ModRegistry.GetItem(ItemWareHouse.class);
	}

	/**
	 * The advanced ware house registered item.
	 * 
	 * @return An instance of {@link ItemAdvancedWareHouse}.
	 */
	public static ItemAdvancedWareHouse AdvancedWareHouse()
	{
		return ModRegistry.GetItem(ItemAdvancedWareHouse.class);
	}

	/**
	 * The chicken coop registered item.
	 * 
	 * @return An instance of {@link ItemAdvancedWareHouse}.
	 */
	public static ItemChickenCoop ChickenCoop()
	{
		return ModRegistry.GetItem(ItemChickenCoop.class);
	}

	/**
	 * The Compressed Chest registered item.
	 * 
	 * @return An instance of {@link ItemCompressedChest}.
	 */
	public static ItemCompressedChest CompressedChestItem()
	{
		return ModRegistry.GetItem(ItemCompressedChest.class);
	}

	/**
	 * The Produce Farm registered item.
	 * 
	 * @return An instance of {@link ItemProduceFarm}.
	 */
	public static ItemProduceFarm ProduceFarm()
	{
		return ModRegistry.GetItem(ItemProduceFarm.class);
	}

	/**
	 * The Tree Farm registered item.
	 * 
	 * @return An instance of {@link ItemTreeFarm}.
	 */
	public static ItemTreeFarm TreeFarm()
	{
		return ModRegistry.GetItem(ItemTreeFarm.class);
	}

	/**
	 * The Fish Pond registered item.
	 * 
	 * @return An instance of {@link ItemFishPond}.
	 */
	public static ItemFishPond FishPond()
	{
		return ModRegistry.GetItem(ItemFishPond.class);
	}

	/**
	 * The Pile of Bricks registered item.
	 * 
	 * @return An instance of {@link ItemPileOfBricks}.
	 */
	public static ItemPileOfBricks PileOfBricks()
	{
		return ModRegistry.GetItem(ItemPileOfBricks.class);
	}

	/**
	 * The Pallet of Bricks registered item.
	 * 
	 * @return An instance of {@link ItemPalletOfBricks}.
	 */
	public static ItemPalletOfBricks PalletOfBricks()
	{
		return ModRegistry.GetItem(ItemPalletOfBricks.class);
	}

	/**
	 * The Monster Masher registered item.
	 * 
	 * @return An instance of {@link ItemMonsterMasher}.
	 */
	public static ItemMonsterMasher MonsterMasher()
	{
		return ModRegistry.GetItem(ItemMonsterMasher.class);
	}

	/**
	 * The Warehouse Upgrade registered item.
	 * 
	 * @return An instance of {@link ItemWarehouseUpgrade}.
	 */
	public static ItemWarehouseUpgrade WareHouseUpgrade()
	{
		return ModRegistry.GetItem(ItemWarehouseUpgrade.class);
	}

	/**
	 * The Bundle of Timber registered item.
	 * 
	 * @return An instance of {@link ItemBundleOfTimber}.
	 */
	public static ItemBundleOfTimber BundleOfTimber()
	{
		return ModRegistry.GetItem(ItemBundleOfTimber.class);
	}

	/**
	 * The Horse Stable registered item.
	 * 
	 * @return An instance of {@link ItemHorseStable}.
	 */
	public static ItemHorseStable HorseStable()
	{
		return ModRegistry.GetItem(ItemHorseStable.class);
	}

	/**
	 * The Nether Gate registered item.
	 * 
	 * @return An instance of {@link ItemNetherGate}.
	 */
	public static ItemNetherGate NetherGate()
	{
		return ModRegistry.GetItem(ItemNetherGate.class);
	}

	/**
	 * The Modular House registered item.
	 * 
	 * @return An instance of {@link ItemModularHouse}.
	 */
	public static ItemModularHouse ModularHouse()
	{
		return ModRegistry.GetItem(ItemModularHouse.class);
	}

	/**
	 * The Basic Structure registered item.
	 * 
	 * @return An instance of {@link ItemBasicStructure}.
	 */
	public static ItemBasicStructure BasicStructure()
	{
		return ModRegistry.GetItem(ItemBasicStructure.class);
	}

	/**
	 * The Instant Bridge registered item.
	 * 
	 * @return An instance of {@link ItemInstantBridge}.
	 */
	public static ItemInstantBridge InstantBridge()
	{
		return ModRegistry.GetItem(ItemInstantBridge.class);
	}
	
	/**
	 * The structure part registered item.
	 * @return An instance of {@link ItemStructurePart}.
	 */
	public static ItemStructurePart StructurePart()
	{
		return ModRegistry.GetItem(ItemStructurePart.class);
	}

	/**
	 * This method is used to get an ItemStack for compressed stone.
	 * 
	 * @param enumType The type of compressed stone.
	 * @return An item stack with the appropriate meta data with 1 item in the stack
	 */
	public static ItemStack GetCompressedStoneType(BlockCompressedStone.EnumType enumType)
	{
		return ModRegistry.GetCompressedStoneType(enumType, 1);
	}

	/**
	 * This method is used to get an ItemStack for compressed stone.
	 * 
	 * @param enumType The type of compressed stone.
	 * @param count The number to have in the returned stack.
	 * @return An item stack with the appropriate meta data with 1 item in the stack
	 */
	public static ItemStack GetCompressedStoneType(BlockCompressedStone.EnumType enumType, int count)
	{
		for (BlockCompressedStone stoneType : ModRegistry.CompressedStones)
		{
			if (enumType.getMetadata() == stoneType.typeofStone.getMetadata())
			{
				return new ItemStack(Item.getItemFromBlock(stoneType), count);
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	/**
	 * Gets a compressed stone block based on the stype.
	 * @param enumType The type of block to get.
	 * @return The appropriate stone block type or if none was found, air.
	 */
	public static Block GetCompressedStoneBlock(BlockCompressedStone.EnumType enumType)
	{
		for (BlockCompressedStone stoneType : ModRegistry.CompressedStones)
		{
			if (enumType.getMetadata() == stoneType.typeofStone.getMetadata())
			{
				return stoneType;
			}
		}
		
		return Blocks.AIR;
	}
	
	/**
	 * This method is used to get an ItemStack for compressed stone.
	 * 
	 * @param enumType The type of compressed stone.
	 * @return An item stack with the appropriate meta data with 1 item in the stack
	 */
	public static ItemStack GetCompressedObsidianType(BlockCompressedObsidian.EnumType enumType)
	{
		return ModRegistry.GetCompressedObsidianType(enumType, 1);
	}

	/**
	 * This method is used to get an ItemStack for compressed stone.
	 * 
	 * @param enumType The type of compressed stone.
	 * @param count The number to have in the returned stack.
	 * @return An item stack with the appropriate meta data with 1 item in the stack
	 */
	public static ItemStack GetCompressedObsidianType(BlockCompressedObsidian.EnumType enumType, int count)
	{
		for (BlockCompressedObsidian stoneType : ModRegistry.CompressedObsidians)
		{
			if (enumType.getMetadata() == stoneType.typeofStone.getMetadata())
			{
				return new ItemStack(Item.getItemFromBlock(stoneType), count);
			}
		}
		
		return ItemStack.EMPTY;
	}

	/**
	 * The Paper Lantern registered block.
	 * 
	 * @return An instance of {@link BlockPaperLantern}.
	 */
	public static BlockPaperLantern PaperLantern()
	{
		return ModRegistry.GetBlock(BlockPaperLantern.class);
	}

	/**
	 * The String of Lanterns registered item.
	 * 
	 * @return An instance of {@link ItemStringOfLanterns}.
	 */
	public static ItemStringOfLanterns StringOfLanterns()
	{
		return ModRegistry.GetItem(ItemStringOfLanterns.class);
	}

	/**
	 * The Coil of Lanterns registered item.
	 * 
	 * @return An instance of {@link ItemCoilOfLanterns}.
	 */
	public static ItemCoilOfLanterns CoilOfLanterns()
	{
		return ModRegistry.GetItem(ItemCoilOfLanterns.class);
	}

	/**
	 * The {@link ItemModerateHouse} registered item.
	 * 
	 * @return An instance of the registered item.
	 */
	public static ItemModerateHouse ModerateHouse()
	{
		return ModRegistry.GetItem(ItemModerateHouse.class);
	}

	/**
	 * The Villager Houses registered item.
	 * 
	 * @return An instance of {@link ItemVillagerHouses}.
	 */
	public static ItemVillagerHouses VillagerHouses()
	{
		return ModRegistry.GetItem(ItemVillagerHouses.class);
	}

	/**
	 * The Phasing Block registered Block.
	 * 
	 * @return An instance of {@link BlockPhasing}.
	 */
	public static BlockPhasing PhasingBlock()
	{
		return ModRegistry.GetBlock(BlockPhasing.class);
	}

	/**
	 * The Boundary Block registered Block.
	 * 
	 * @return An instance of {@link BlockBoundary}.
	 */
	public static BlockBoundary BoundaryBlock()
	{
		return ModRegistry.GetBlock(BlockBoundary.class);
	}

	public static ItemBulldozer Bulldozer()
	{
		return ModRegistry.GetItem(ItemBulldozer.class);
	}

	/**
	 * Gets the item from the ModItems collections.
	 * 
	 * @param <T> The type which extends item.
	 * @param genericClass The class of item to get from the collection.
	 * @return Null if the item could not be found otherwise the item found.
	 */
	public static <T extends Item> T GetItem(Class<T> genericClass)
	{
		for (Item entry : ModRegistry.ModItems)
		{
			if (entry.getClass() == genericClass)
			{
				return (T) entry;
			}
		}

		return null;
	}

	/**
	 * Gets the block from the ModBlockss collections.
	 * 
	 * @param <T> The type which extends Block.
	 * @param genericClass The class of block to get from the collection.
	 * @return Null if the block could not be found otherwise the block found.
	 */
	public static <T extends Block> T GetBlock(Class<T> genericClass)
	{
		for (Block entry : ModRegistry.ModBlocks)
		{
			if (entry.getClass() == genericClass)
			{
				return (T) entry;
			}
		}

		return null;
	}

	/**
	 * Gets the gui screen for the ID and passes position data to it.
	 * 
	 * @param id The ID of the screen to get.
	 * @param x The X-Axis of where this screen was created from, this is used to create a BlockPos.
	 * @param y The Y-Axis of where this screen was created from, this is used to create a BlockPos.
	 * @param z The Z-Axis of where this screen was created from, this is used to create a BlockPos.
	 * @return Null if the screen wasn't found, otherwise the screen found.
	 */
	public static Screen GetModGuiByID(String id, int x, int y, int z)
	{
		for (Entry<String, Class> entry : ModRegistry.ModGuis.entrySet())
		{
			if (entry.getKey().equals(id))
			{
				try
				{
					return (Screen) entry.getValue().getConstructor(int.class, int.class, int.class).newInstance(x, y, z);
				}
				catch (InstantiationException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (InvocationTargetException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (NoSuchMethodException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (SecurityException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	/**
	 * This is where all in-game mod components (Items, Blocks) will be registered.
	 */
	public static void RegisterModComponents()
	{
		ModRegistry.registerItem(new ItemStartHouse("item_start_house"));
		ModRegistry.registerItem(new ItemWareHouse("item_warehouse"));
		ModRegistry.registerItem(new ItemChickenCoop("item_chicken_coop"));
		ModRegistry.registerItem(new ItemProduceFarm("item_produce_farm"));
		ModRegistry.registerItem(new ItemTreeFarm("item_tree_farm"));
		ModRegistry.registerItem(new ItemCompressedChest("item_compressed_chest"));
		ModRegistry.registerItem(new ItemPileOfBricks("item_pile_of_bricks"));
		ModRegistry.registerItem(new ItemPalletOfBricks("item_pallet_of_bricks"));
		ModRegistry.registerItem(new ItemFishPond("item_fish_pond"));
		ModRegistry.registerItem(new ItemAdvancedWareHouse("item_advanced_warehouse"));
		ModRegistry.registerItem(new ItemMonsterMasher("item_monster_masher"));
		ModRegistry.registerItem(new ItemWarehouseUpgrade("item_warehouse_upgrade"));
		ModRegistry.registerItem(new ItemBundleOfTimber("item_bundle_of_timber"));
		ModRegistry.registerItem(new ItemHorseStable("item_horse_stable"));
		ModRegistry.registerItem(new ItemNetherGate("item_nether_gate"));
		ModRegistry.registerItem(new ItemInstantBridge("item_instant_bridge"));
		ModRegistry.registerItem(new ItemStringOfLanterns("item_string_of_lanterns"));
		ModRegistry.registerItem(new ItemCoilOfLanterns("item_coil_of_lanterns"));
		ModRegistry.registerItem(new ItemModerateHouse("item_moderate_house"));
		ModRegistry.registerItem(new ItemBulldozer("item_bulldozer"));
		ModRegistry.registerItem(new ItemStructurePart("item_structure_part"));
		// ModRegistry.registerItem(new ItemBogus("item_bogus"));

		// Register all the basic structures here. The resource location is used for the item models and textures.
		// Only the first one in this list should have the last variable set to true.
		ModRegistry.registerItem(new ItemBasicStructure("item_basic_structure"));

		for (BlockCompressedStone.EnumType stoneType : BlockCompressedStone.EnumType.values())
		{
			BlockCompressedStone stone = new BlockCompressedStone(stoneType);
			ModRegistry.CompressedStones.add(ModRegistry.registerBlock(stone));
		}

		ModRegistry.registerBlock(new BlockPaperLantern("block_paper_lantern"));

		for (BlockCompressedObsidian.EnumType stoneType : BlockCompressedObsidian.EnumType.values())
		{
			BlockCompressedObsidian stone = new BlockCompressedObsidian(stoneType);
			ModRegistry.CompressedObsidians.add(ModRegistry.registerBlock(stone));
		}

		ModRegistry.registerItem(new ItemVillagerHouses("item_villager_houses"));

		ModRegistry.registerBlock(new BlockPhasing("block_phasing"));

		ModRegistry.registerBlock(new BlockBoundary("block_boundary"));

		ModRegistry.registerBlock(new BlockStairs(Blocks.GLASS.getDefaultState(), Block.Properties.from(Blocks.GLASS)), true, "block_glass_stairs");
		
		ModRegistry.registerBlock(new SlabBlock(Block.Properties.from(Blocks.GLASS)), true, "block_glass_slab");
	}

	/**
	 * Registers records into the ore dictionary.
	 */
	public static void RegisterOreDictionaryRecords()
	{
		// Register certain blocks into the ore dictionary.
/*		OreDictionary.registerOre("compressedDirt1", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_DIRT));
		OreDictionary.registerOre("compressedDirt2", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT));
		OreDictionary.registerOre("compressedStone1", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_STONE));
		OreDictionary.registerOre("compressedStone2", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE));
		OreDictionary.registerOre("compressedStone3", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE));
		OreDictionary.registerOre("compressedGlowstone1", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE));
		OreDictionary.registerOre("compressedGlowstone2", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE));
		OreDictionary.registerOre("compressedObsidian1", ModRegistry.GetCompressedObsidianType(BlockCompressedObsidian.EnumType.COMPRESSED_OBSIDIAN));
		OreDictionary.registerOre("compressedObsidian2", ModRegistry.GetCompressedObsidianType(BlockCompressedObsidian.EnumType.DOUBLE_COMPRESSED_OBSIDIAN));*/
	}

	/**
	 * This is where the mod messages are registered.
	 */
	public static void RegisterMessages()
	{
		int index = 0;
		Prefab.network.messageBuilder(ConfigSyncMessage.class, index++)
			.encoder(ConfigSyncMessage::encode)
			.decoder(ConfigSyncMessage::decode)
			.consumer(ConfigSyncHandler::handle)
			.add();
		
		Prefab.network.messageBuilder(PlayerEntityTagMessage.class, index++)
			.encoder(PlayerEntityTagMessage::encode)
			.decoder(PlayerEntityTagMessage::decode)
			.consumer(PlayerEntityHandler::handle)
			.add();
			
		Prefab.network.messageBuilder(StructureTagMessage.class, index++)
			.encoder(StructureTagMessage::encode)
			.decoder(StructureTagMessage::decode)
			.consumer(StructureHandler::handle)
			.add();
	}

	/**
	 * This is where mod capabilities are registered.
	 */
	public static void RegisterCapabilities()
	{
		// Register the dimension home capability.
		CapabilityManager.INSTANCE.register(IStructureConfigurationCapability.class, new StructureConfigurationStorage(),
			() -> new StructureConfigurationCapability());
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
		ModRegistry.ModItems.add(item);

		return item;
	}

	/**
	 * Registers a block in the game registry.
	 * 
	 * @param <T> The type of block to register.
	 * @param block The block to register.
	 * @return The block which was registered.
	 */
	public static <T extends Block> T registerBlock(T block)
	{
		return ModRegistry.registerBlock(block, true);
	}

	/**
	 * Registers a block in the game registry.
	 * 
	 * @param <T> The type of block to register.
	 * @param block The block to register.
	 * @param includeItemBlock True to include a default item block.
	 * @return The block which was registered.
	 */
	public static <T extends Block> T registerBlock(T block, boolean includeItemBlock)
	{
		return ModRegistry.registerBlock(block, includeItemBlock, block.getRegistryName().getPath());
	}
	
	/**
	 * Registers a block in the game registry.
	 * 
	 * @param <T> The type of block to register.
	 * @param block The block to register.
	 * @param includeItemBlock True to include a default item block.
	 * @return The block which was registered.
	 */
	public static <T extends Block> T registerBlock(T block, boolean includeItemBlock, String name)
	{
		if (includeItemBlock)
		{
			ModItems.add(new BlockItem(block, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(name));
		}

		ModRegistry.ModBlocks.add(block);

		return block;
	}

	/**
	 * Registers a block in the game registry.
	 * 
	 * @param <T> The type of block to register.
	 * @param <I> The type of item block to register.
	 * @param block The block to register.
	 * @param itemBlock The item block to register with the block.
	 * @return The block which was registered.
	 */
	public static <T extends Block, I extends BlockItem> T registerBlock(T block, I itemBlock)
	{
		ModRegistry.ModBlocks.add(block);

		if (itemBlock != null)
		{
			ModRegistry.ModItems.add(itemBlock);
		}

		return block;
	}

	/**
	 * Set the registry name of {@code item} to {@code itemName} and the un-localised name to the full registry name.
	 *
	 * @param item The item
	 * @param itemName The item's name
	 */
	public static void setItemName(Item item, String itemName)
	{
		if (itemName != null)
		{
			item.setRegistryName(itemName);
		}
	}

	/**
	 * Set the registry name of {@code block} to {@code blockName} and the un-localised name to the full registry name.
	 *
	 * @param block The block
	 * @param blockName The block's name
	 */
	public static void setBlockName(Block block, String blockName)
	{
		block.setRegistryName(blockName);
	}

	/**
	 * Adds all of the Mod Guis to the HasMap.
	 */
	public static void AddGuis()
	{
		ModRegistry.ModGuis.put(ModRegistry.GuiWareHouse, GuiWareHouse.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiChickenCoop, GuiChickenCoop.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiProduceFarm, GuiProduceFarm.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiTreeFarm, GuiTreeFarm.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiFishPond, GuiFishPond.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiStartHouseChooser, GuiStartHouseChooser.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiAdvancedWareHouse, GuiAdvancedWareHouse.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiMonsterMasher, GuiMonsterMasher.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiHorseStable, GuiHorseStable.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiNetherGate, GuiNetherGate.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiModularHouse, GuiModularHouse.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiDrafter, GuiDrafter.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiBasicStructure, GuiBasicStructure.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiVillagerHouses, GuiVillaerHouses.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiModerateHouse, GuiModerateHouse.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiBulldozer, GuiBulldozer.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiInstantBridge, GuiInstantBridge.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiStructurePart,  GuiStructurePart.class);
	}
}