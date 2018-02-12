package com.wuest.prefab;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wuest.prefab.Blocks.BlockAndesiteStairs;
import com.wuest.prefab.Blocks.BlockBoundary;
import com.wuest.prefab.Blocks.BlockCompressedObsidian;
import com.wuest.prefab.Blocks.BlockCompressedStone;
import com.wuest.prefab.Blocks.BlockDioriteStairs;
import com.wuest.prefab.Blocks.BlockDoubleAndesiteSlab;
import com.wuest.prefab.Blocks.BlockDoubleDioriteSlab;
import com.wuest.prefab.Blocks.BlockDoubleGlassSlab;
import com.wuest.prefab.Blocks.BlockDoubleGraniteSlab;
import com.wuest.prefab.Blocks.BlockDrafter;
import com.wuest.prefab.Blocks.BlockGlassSlab;
import com.wuest.prefab.Blocks.BlockGlassStairs;
import com.wuest.prefab.Blocks.BlockGraniteStairs;
import com.wuest.prefab.Blocks.BlockHalfAndesiteSlab;
import com.wuest.prefab.Blocks.BlockHalfDioriteSlab;
import com.wuest.prefab.Blocks.BlockHalfGlassSlab;
import com.wuest.prefab.Blocks.BlockHalfGraniteSlab;
import com.wuest.prefab.Blocks.BlockPaperLantern;
import com.wuest.prefab.Blocks.BlockPhasing;
import com.wuest.prefab.Capabilities.IStructureConfigurationCapability;
import com.wuest.prefab.Capabilities.StructureConfigurationCapability;
import com.wuest.prefab.Capabilities.Storage.StructureConfigurationStorage;
import com.wuest.prefab.Config.Structures.ChickenCoopConfiguration;
import com.wuest.prefab.Config.Structures.StructureConfiguration;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Config.Structures.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.Gui.GuiDrafter;
import com.wuest.prefab.Gui.Structures.GuiAdvancedWareHouse;
import com.wuest.prefab.Gui.Structures.GuiBasicStructure;
import com.wuest.prefab.Gui.Structures.GuiBulldozer;
import com.wuest.prefab.Gui.Structures.GuiChickenCoop;
import com.wuest.prefab.Gui.Structures.GuiFishPond;
import com.wuest.prefab.Gui.Structures.GuiHorseStable;
import com.wuest.prefab.Gui.Structures.GuiInstantBridge;
import com.wuest.prefab.Gui.Structures.GuiModerateHouse;
import com.wuest.prefab.Gui.Structures.GuiModularHouse;
import com.wuest.prefab.Gui.Structures.GuiMonsterMasher;
import com.wuest.prefab.Gui.Structures.GuiNetherGate;
import com.wuest.prefab.Gui.Structures.GuiProduceFarm;
import com.wuest.prefab.Gui.Structures.GuiStartHouseChooser;
import com.wuest.prefab.Gui.Structures.GuiTreeFarm;
import com.wuest.prefab.Gui.Structures.GuiVillaerHouses;
import com.wuest.prefab.Gui.Structures.GuiWareHouse;
import com.wuest.prefab.Items.ItemBlockAndesiteSlab;
import com.wuest.prefab.Items.ItemBlockDioriteSlab;
import com.wuest.prefab.Items.ItemBlockGlassSlab;
import com.wuest.prefab.Items.ItemBlockGraniteSlab;
import com.wuest.prefab.Items.ItemBlockMeta;
import com.wuest.prefab.Items.ItemBogus;
import com.wuest.prefab.Items.ItemBundleOfTimber;
import com.wuest.prefab.Items.ItemCoilOfLanterns;
import com.wuest.prefab.Items.ItemCompressedChest;
import com.wuest.prefab.Items.ItemPalletOfBricks;
import com.wuest.prefab.Items.ItemPileOfBricks;
import com.wuest.prefab.Items.ItemStringOfLanterns;
import com.wuest.prefab.Items.ItemWarehouseUpgrade;
import com.wuest.prefab.Items.Structures.ItemAdvancedWareHouse;
import com.wuest.prefab.Items.Structures.ItemBasicStructure;
import com.wuest.prefab.Items.Structures.ItemBulldozer;
import com.wuest.prefab.Items.Structures.ItemChickenCoop;
import com.wuest.prefab.Items.Structures.ItemFishPond;
import com.wuest.prefab.Items.Structures.ItemHorseStable;
import com.wuest.prefab.Items.Structures.ItemInstantBridge;
import com.wuest.prefab.Items.Structures.ItemModerateHouse;
import com.wuest.prefab.Items.Structures.ItemModularHouse;
import com.wuest.prefab.Items.Structures.ItemMonsterMasher;
import com.wuest.prefab.Items.Structures.ItemNetherGate;
import com.wuest.prefab.Items.Structures.ItemProduceFarm;
import com.wuest.prefab.Items.Structures.ItemStartHouse;
import com.wuest.prefab.Items.Structures.ItemTreeFarm;
import com.wuest.prefab.Items.Structures.ItemVillagerHouses;
import com.wuest.prefab.Items.Structures.ItemWareHouse;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;
import com.wuest.prefab.Proxy.Messages.PlayerEntityTagMessage;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage;
import com.wuest.prefab.Proxy.Messages.Handlers.ConfigSyncHandler;
import com.wuest.prefab.Proxy.Messages.Handlers.PlayerEntityHandler;
import com.wuest.prefab.Proxy.Messages.Handlers.StructureHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStone;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import scala.Tuple2;

/**
 * This is the mod registry so there is a way to get to all instances of the blocks/items created by this mod.
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
	public static HashMap<Integer, Class> ModGuis = new HashMap<Integer, Class>();

	/**
	 * The identifier for the WareHouse GUI.
	 */
	public static final int GuiWareHouse = 1;

	/**
	 * The identifier for the Chicken coop GUI.
	 */
	public static final int GuiChickenCoop = 2;

	/**
	 * The identifier for the Produce Farm GUI.
	 */
	public static final int GuiProduceFarm = 3;

	/**
	 * The identifier for the Tree Farm GUI.
	 */
	public static final int GuiTreeFarm = 4;

	/**
	 * The identifier for the FishPond GUI.
	 */
	public static final int GuiFishPond = 5;

	/**
	 * The identifier for the Starting House GUI.
	 */
	public static final int GuiStartHouseChooser = 6;

	/**
	 * The identifier for the Advanced WareHouse GUI.
	 */
	public static final int GuiAdvancedWareHouse = 7;

	/**
	 * The identifier for the Monster Masher GUI.
	 */
	public static final int GuiMonsterMasher = 8;

	/**
	 * The identifier for the Horse Stable GUI.
	 */
	public static final int GuiHorseStable = 9;

	/**
	 * The identifier for the Nether Gate GUI.
	 */
	public static final int GuiNetherGate = 10;

	/**
	 * The identifier for the Modular House GUI.
	 */
	public static final int GuiModularHouse = 11;

	/**
	 * The identifier for the Drafter GUI.
	 */
	public static final int GuiDrafter = 12;

	/**
	 * The identifier for the Basic structure GUI.
	 */
	public static final int GuiBasicStructure = 13;

	/**
	 * The identifier for the Villaer Houses GUI.
	 */
	public static final int GuiVillagerHouses = 14;

	/**
	 * The identifier for the moderate house GUI.
	 */
	public static final int GuiModerateHouse = 15;
	
	 /** 
	  * The identifier for the bulldozer GUI.
	 */
	public static final int GuiBulldozer = 16;
	
	/**
	 * The identifier for the instant bridge gui.
	 */
	public static final int GuiInstantBridge = 17;
	
	/**
	 * This capability is used to save the locations where a player spawns when transferring dimensions.
	 */
	@CapabilityInject(IStructureConfigurationCapability.class)
	public static Capability<IStructureConfigurationCapability> StructureConfiguration = null;

	/**
	 * The starting house registered item.
	 * @return An instance of {@link ItemStartHouse}.
	 */
	public static ItemStartHouse StartHouse()
	{
		return ModRegistry.GetItem(ItemStartHouse.class);
	}

	/**
	 * The warehouse registered item.
	 * @return An instance of {@link ItemWareHouse}.
	 */
	public static ItemWareHouse WareHouse()
	{
		return ModRegistry.GetItem(ItemWareHouse.class);
	}

	/**
	 * The advanced ware house registered item.
	 * @return An instance of {@link ItemAdvancedWareHouse}.
	 */
	public static ItemAdvancedWareHouse AdvancedWareHouse()
	{
		return ModRegistry.GetItem(ItemAdvancedWareHouse.class);
	}

	/**
	 * The chicken coop registered item.
	 * @return An instance of {@link ItemAdvancedWareHouse}.
	 */
	public static ItemChickenCoop ChickenCoop()
	{
		return ModRegistry.GetItem(ItemChickenCoop.class);
	}

	/**
	 * The CompressedStone registered item.
	 * @return An instance of {@link ItemBlockMeta}.
	 */
	public static ItemBlockMeta CompressedStoneItem()
	{
		return ModRegistry.GetItem(ItemBlockMeta.class);
	}

	/**
	 * The CompressedStone registered Block.
	 * @return An instance of {@link BlockCompressedStone}.
	 */
	public static BlockCompressedStone CompressedStoneBlock()
	{
		return ModRegistry.GetBlock(BlockCompressedStone.class);
	}

	/**
	 * The Compressed Chest registered item.
	 * @return An instance of {@link ItemCompressedChest}.
	 */
	public static ItemCompressedChest CompressedChestItem() 
	{
		return ModRegistry.GetItem(ItemCompressedChest.class);
	}

	/**
	 * The Produce Farm registered item.
	 * @return An instance of {@link ItemProduceFarm}.
	 */
	public static ItemProduceFarm ProduceFarm() 
	{
		return ModRegistry.GetItem(ItemProduceFarm.class);
	}

	/**
	 * The Tree Farm registered item.
	 * @return An instance of {@link ItemTreeFarm}.
	 */
	public static ItemTreeFarm TreeFarm() 
	{
		return ModRegistry.GetItem(ItemTreeFarm.class);
	}

	/**
	 * The Fish Pond registered item.
	 * @return An instance of {@link ItemFishPond}.
	 */
	public static ItemFishPond FishPond()
	{
		return ModRegistry.GetItem(ItemFishPond.class);
	}

	/**
	 * The Pile of Bricks registered item.
	 * @return An instance of {@link ItemPileOfBricks}.
	 */
	public static ItemPileOfBricks PileOfBricks() 
	{
		return ModRegistry.GetItem(ItemPileOfBricks.class);
	}

	/**
	 * The Pallet of Bricks registered item.
	 * @return An instance of {@link ItemPalletOfBricks}.
	 */
	public static ItemPalletOfBricks PalletOfBricks()
	{
		return ModRegistry.GetItem(ItemPalletOfBricks.class);
	}

	/**
	 * The Monster Masher registered item.
	 * @return An instance of {@link ItemMonsterMasher}.
	 */
	public static ItemMonsterMasher MonsterMasher()
	{
		return ModRegistry.GetItem(ItemMonsterMasher.class);
	}

	/**
	 * The Warehouse Upgrade registered item.
	 * @return An instance of {@link ItemWarehouseUpgrade}.
	 */
	public static ItemWarehouseUpgrade WareHouseUpgrade()
	{
		return ModRegistry.GetItem(ItemWarehouseUpgrade.class);
	}

	/**
	 * The Bundle of Timber registered item.
	 * @return An instance of {@link ItemBundleOfTimber}.
	 */
	public static ItemBundleOfTimber BundleOfTimber()
	{
		return ModRegistry.GetItem(ItemBundleOfTimber.class);
	}

	/**
	 * The Horse Stable registered item.
	 * @return An instance of {@link ItemHorseStable}.
	 */
	public static ItemHorseStable HorseStable()
	{
		return ModRegistry.GetItem(ItemHorseStable.class);
	}

	/**
	 * The Nether Gate registered item.
	 * @return An instance of {@link ItemNetherGate}.
	 */
	public static ItemNetherGate NetherGate()
	{
		return ModRegistry.GetItem(ItemNetherGate.class);
	}

	/**
	 * The Modular House registered item.
	 * @return An instance of {@link ItemModularHouse}.
	 */
	public static ItemModularHouse ModularHouse()
	{
		return ModRegistry.GetItem(ItemModularHouse.class);
	}

	/**
	 * The Drafter registered block.
	 * @return An instance of {@link BlockDrafter}.
	 */
	public static BlockDrafter Drafter()
	{
		return ModRegistry.GetBlock(BlockDrafter.class);
	}

	/**
	 * The Basic Structure registered item.
	 * @return An instance of {@link ItemBasicStructure}.
	 */
	public static ItemBasicStructure BasicStructure()
	{
		return ModRegistry.GetItem(ItemBasicStructure.class);
	}

	/**
	 * The Instant Bridge registered item.
	 * @return An instance of {@link ItemInstantBridge}.
	 */
	public static ItemInstantBridge InstantBridge()
	{
		return ModRegistry.GetItem(ItemInstantBridge.class);
	}

	/**
	 * This method is used to get an ItemStack for compressed stone.
	 * @param enumType The type of compressed stone.
	 * @return An item stack with the appropriate meta data with 1 item in the stack
	 */
	public static ItemStack GetCompressedStoneType(BlockCompressedStone.EnumType enumType)
	{
		return ModRegistry.GetCompressedStoneType(enumType, 1);
	}

	/**
	 * This method is used to get an ItemStack for compressed stone.
	 * @param enumType The type of compressed stone.
	 * @param count The number to have in the returned stack.
	 * @return An item stack with the appropriate meta data with 1 item in the stack
	 */
	public static ItemStack GetCompressedStoneType(BlockCompressedStone.EnumType enumType, int count)
	{
		return new ItemStack(Item.getItemFromBlock(ModRegistry.CompressedStoneBlock()), count, enumType.getMetadata());
	}

	/**
	 * The Paper Lantern registered block.
	 * @return An instance of {@link BlockPaperLantern}.
	 */
	public static BlockPaperLantern PaperLantern()
	{
		return ModRegistry.GetBlock(BlockPaperLantern.class);
	}

	/**
	 * The String of Lanterns registered item.
	 * @return An instance of {@link ItemStringOfLanterns}.
	 */
	public static ItemStringOfLanterns StringOfLanterns()
	{
		return ModRegistry.GetItem(ItemStringOfLanterns.class);
	}

	/**
	 * The Coil of Lanterns registered item.
	 * @return An instance of {@link ItemCoilOfLanterns}.
	 */
	public static ItemCoilOfLanterns CoilOfLanterns()
	{
		return ModRegistry.GetItem(ItemCoilOfLanterns.class);
	}

	/**
	 * The {@link ItemModerateHouse} registered item.
	 * @return An instance of the registered item.
	 */
	public static ItemModerateHouse ModerateHouse()
	{
		return ModRegistry.GetItem(ItemModerateHouse.class);
	}
	
	/**
	 * The Compressed Obsidian registered block.
	 * @return An instance of {@link CompressedObsidianBlock}.
	 */
	public static BlockCompressedObsidian CompressedObsidianBlock()
	{
		return ModRegistry.GetBlock(BlockCompressedObsidian.class);
	}

	/**
	 * The Villager Houses registered item.
	 * @return An instance of {@link ItemVillagerHouses}.
	 */
	public static ItemVillagerHouses VillagerHouses()
	{
		return ModRegistry.GetItem(ItemVillagerHouses.class);
	}

	/**
	 * The Phasing Block registered Block.
	 * @return An instance of {@link BlockPhasing}.
	 */
	public static BlockPhasing PhasingBlock()
	{
		return ModRegistry.GetBlock(BlockPhasing.class);
	}

	/**
	 * The Glass Stairs registered Block.
	 * @return An instance of {@link BlockGlassStairs}.
	 */
	public static BlockGlassStairs GlassStairs()
	{
		return ModRegistry.GetBlock(BlockGlassStairs.class);
	}
	
	/**
	 * The Andesite Stairs registered Block.
	 * @return An instance of {@link BlockAndesiteStairs}.
	 */
	public static BlockAndesiteStairs AndesiteStairs()
	{
		return ModRegistry.GetBlock(BlockAndesiteStairs.class);
	}
	
	/**
	 * The Diorite Stairs registered Block.
	 * @return An instance of {@link BlockDioriteStairs}.
	 */
	public static BlockDioriteStairs DioriteStairs()
	{
		return ModRegistry.GetBlock(BlockDioriteStairs.class);
	}
	
	/**
	 * The Granite Stairs registered Block.
	 * @return An instance of {@link BlockGraniteStairs}.
	 */
	public static BlockGraniteStairs GraniteStairs()
	{
		return ModRegistry.GetBlock(BlockGraniteStairs.class);
	}
	
	/**
	 * The Glass Slab registered Block.
	 * @return An instance of {@link BlockHalfGlassSlab}.
	 */
	public static BlockHalfGlassSlab GlassSlab()
	{
		return ModRegistry.GetBlock(BlockHalfGlassSlab.class);
	}
	
	/**
	 * The Andesite Slab registered Block.
	 * @return An instance of {@link BlockHalfAndesiteSlab}.
	 */
	public static BlockHalfAndesiteSlab AndesiteSlab()
	{
		return ModRegistry.GetBlock(BlockHalfAndesiteSlab.class);
	}
	
	/**
	 * The Diorite Slab registered Block.
	 * @return An instance of {@link BlockHalfDioriteSlab}.
	 */
	public static BlockHalfDioriteSlab DioriteSlab()
	{
		return ModRegistry.GetBlock(BlockHalfDioriteSlab.class);
	}
	
	/**
	 * The Granite Slab registered Block.
	 * @return An instance of {@link BlockHalfGraniteSlab}.
	 */
	public static BlockHalfGraniteSlab GraniteSlab()
	{
		return ModRegistry.GetBlock(BlockHalfGraniteSlab.class);
	}
	
	/**
	 * The Boundary Block registered Block.
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
	 * Static constructor for the mod registry.
	 */
	static
	{
		ModRegistry.RegisterModComponents();
	}

	/**
	 * Gets the item from the ModItems collections.
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
				return (T)entry;
			}
		}

		return null;
	}

	/**
	 * Gets the block from the ModBlockss collections.
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
				return (T)entry;
			}
		}

		return null;
	}

	/**
	 * Gets the gui screen for the ID and passes position data to it.
	 * @param id The ID of the screen to get.
	 * @param x The X-Axis of where this screen was created from, this is used to create a BlockPos.
	 * @param y The Y-Axis of where this screen was created from, this is used to create a BlockPos.
	 * @param z The Z-Axis of where this screen was created from, this is used to create a BlockPos.
	 * @return Null if the screen wasn't found, otherwise the screen found.
	 */
	public static GuiScreen GetModGuiByID(int id, int x, int y, int z)
	{
		for (Entry<Integer, Class> entry : ModRegistry.ModGuis.entrySet())
		{
			if (entry.getKey() == id)
			{
				try
				{
					return (GuiScreen)entry.getValue().getConstructor(int.class, int.class, int.class).newInstance(x, y, z);
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
		//ModRegistry.registerItem(new ItemModularHouse("item_modular_house"));
		ModRegistry.registerItem(new ItemStringOfLanterns("item_string_of_lanterns"));
		ModRegistry.registerItem(new ItemCoilOfLanterns("item_coil_of_lanterns"));
		ModRegistry.registerItem(new ItemModerateHouse("item_moderate_house"));
		ModRegistry.registerItem(new ItemBulldozer("item_bulldozer"));
		//ModRegistry.registerItem(new ItemBogus("item_bogus"));

		// Register all the basic structures here. The resource location is used for the item models and textures.
		// Only the first one in this list should have the last variable set to true.
		ModRegistry.registerItem(new ItemBasicStructure("item_basic_structure"));

		// Create/register the item block with this block as it's needed due to this being a meta data block.
		BlockCompressedStone stone = new BlockCompressedStone();
		ItemBlockMeta meta = new ItemBlockMeta(stone);
		ModRegistry.setItemName(meta, "block_compressed_stone");
		ModRegistry.registerBlock(stone, meta);

		ModRegistry.registerBlock(new BlockPaperLantern("block_paper_lantern"));

		BlockCompressedObsidian obsidian = new BlockCompressedObsidian();
		ItemBlockMeta metaObsidian = new ItemBlockMeta(obsidian);
		ModRegistry.setItemName(metaObsidian, "block_compressed_obsidian");
		ModRegistry.registerBlock(obsidian, metaObsidian);

		ModRegistry.registerItem(new ItemVillagerHouses("item_villager_houses"));

		ModRegistry.registerBlock(new BlockPhasing("block_phasing"));

		ModRegistry.registerBlock(new BlockBoundary("block_boundary"));
	
		ModRegistry.registerBlock(new BlockGlassStairs("block_glass_stairs"));
		
		ModRegistry.registerBlock(new BlockAndesiteStairs("block_andesite_stairs"));
		
		ModRegistry.registerBlock(new BlockDioriteStairs("block_diorite_stairs"));
		
		ModRegistry.registerBlock(new BlockGraniteStairs("block_granite_stairs"));
		
		// Glass Slab.
		BlockHalfGlassSlab registeredHalfGlassBlock = new BlockHalfGlassSlab();
		BlockDoubleGlassSlab registeredDoubleGlassSlab = new BlockDoubleGlassSlab();

		ItemBlockGlassSlab itemHalfGlassSlab = new ItemBlockGlassSlab(registeredHalfGlassBlock, registeredHalfGlassBlock, registeredDoubleGlassSlab, true);

		itemHalfGlassSlab = (ItemBlockGlassSlab) itemHalfGlassSlab.setRegistryName("block_half_glass_slab");

		ModRegistry.registerBlock(registeredHalfGlassBlock, itemHalfGlassSlab);
		ModRegistry.registerBlock(registeredDoubleGlassSlab, false);
		
		// Andesite slab.
		BlockHalfAndesiteSlab registeredHalfAndesiteBlock = new BlockHalfAndesiteSlab();
		BlockDoubleAndesiteSlab registeredDoubleAndesiteSlab = new BlockDoubleAndesiteSlab();

		ItemBlockAndesiteSlab itemHalfAndesiteSlab = new ItemBlockAndesiteSlab(registeredHalfAndesiteBlock, registeredHalfAndesiteBlock, registeredDoubleAndesiteSlab, true);

		itemHalfAndesiteSlab = (ItemBlockAndesiteSlab) itemHalfAndesiteSlab.setRegistryName("block_half_andesite_slab");

		ModRegistry.registerBlock(registeredHalfAndesiteBlock, itemHalfAndesiteSlab);
		ModRegistry.registerBlock(registeredDoubleAndesiteSlab, false);
		
		// Diorite slab.
		BlockHalfDioriteSlab registeredHalfDioriteBlock = new BlockHalfDioriteSlab();
		BlockDoubleDioriteSlab registeredDoubleDioriteSlab = new BlockDoubleDioriteSlab();

		ItemBlockDioriteSlab itemHalfDioriteSlab = new ItemBlockDioriteSlab(registeredHalfDioriteBlock, registeredHalfDioriteBlock, registeredDoubleDioriteSlab, true);

		itemHalfDioriteSlab = (ItemBlockDioriteSlab) itemHalfDioriteSlab.setRegistryName("block_half_diorite_slab");

		ModRegistry.registerBlock(registeredHalfDioriteBlock, itemHalfDioriteSlab);
		ModRegistry.registerBlock(registeredDoubleDioriteSlab, false);
		
		// Granite slab.
		BlockHalfGraniteSlab registeredHalfGraniteBlock = new BlockHalfGraniteSlab();
		BlockDoubleGraniteSlab registeredDoubleGraniteSlab = new BlockDoubleGraniteSlab();

		ItemBlockGraniteSlab itemHalfGraniteSlab = new ItemBlockGraniteSlab(registeredHalfGraniteBlock, registeredHalfGraniteBlock, registeredDoubleGraniteSlab, true);

		itemHalfGraniteSlab = (ItemBlockGraniteSlab) itemHalfGraniteSlab.setRegistryName("block_half_granite_slab");

		ModRegistry.registerBlock(registeredHalfGraniteBlock, itemHalfGraniteSlab);
		ModRegistry.registerBlock(registeredDoubleGraniteSlab, false);
		
		Blocks.STRUCTURE_BLOCK.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		
		//BlockDrafter drafter = new BlockDrafter();
		//ModRegistry.registerBlock(drafter);
		//GameRegistry.registerTileEntity(TileEntityDrafter.class, "Drafter");
	}

	/**
	 * Registers records into the ore dictionary.
	 */
	public static void RegisterOreDictionaryRecords()
	{
		// Register certain blocks into the ore dictionary.
		OreDictionary.registerOre("compressedDirt1", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_DIRT));
		OreDictionary.registerOre("compressedDirt2", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT));
		OreDictionary.registerOre("compressedStone1", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_STONE));
		OreDictionary.registerOre("compressedStone2", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE));
		OreDictionary.registerOre("compressedStone3", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE));
		OreDictionary.registerOre("compressedGlowstone1", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE));
		OreDictionary.registerOre("compressedGlowstone2", ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE));
		OreDictionary.registerOre(
				"compressedObsidian1", 
				new ItemStack(Item.getItemFromBlock(ModRegistry.CompressedObsidianBlock()), 1, BlockCompressedObsidian.EnumType.COMPRESSED_OBSIDIAN.getMetadata()));
		OreDictionary.registerOre(
				"compressedObsidian2", 
				new ItemStack(Item.getItemFromBlock(ModRegistry.CompressedObsidianBlock()), 1, BlockCompressedObsidian.EnumType.DOUBLE_COMPRESSED_OBSIDIAN.getMetadata()));
	}

	/**
	 * This is where the mod messages are registered.
	 */
	public static void RegisterMessages()
	{
		Prefab.network.registerMessage(ConfigSyncHandler.class, ConfigSyncMessage.class, 1, Side.CLIENT);

		Prefab.network.registerMessage(StructureHandler.class, StructureTagMessage.class, 2, Side.SERVER);
		
		Prefab.network.registerMessage(PlayerEntityHandler.class, PlayerEntityTagMessage.class, 3, Side.CLIENT);
	}

	/**
	 * This is where mod capabilities are registered.
	 */
	public static void RegisterCapabilities()
	{
		// Register the dimension home capability.
		CapabilityManager.INSTANCE.register(IStructureConfigurationCapability.class, new StructureConfigurationStorage(), StructureConfigurationCapability.class);
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
	 * @param <T> The type of block to register.
	 * @param block The block to register.
	 * @param includeItemBlock True to include a default item block.
	 * @return The block which was registered.
	 */
	public static <T extends Block> T registerBlock(T block, boolean includeItemBlock)
	{
		if (includeItemBlock)
		{
			ModItems.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}

		ModRegistry.ModBlocks.add(block);

		return block;
	}

	/**
	 * Registers a block in the game registry.
	 * @param <T> The type of block to register.
	 * @param <I> The type of item block to register.
	 * @param block The block to register.
	 * @param itemBlock The item block to register with the block.
	 * @return The block which was registered.
	 */
	public static <T extends Block, I extends ItemBlock> T registerBlock(T block, I itemBlock)
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
	 * @param item     The item
	 * @param itemName The item's name
	 */
	public static void setItemName(Item item, String itemName) 
	{
		if (itemName != null)
		{
			item.setRegistryName(itemName);
			item.setUnlocalizedName(item.getRegistryName().toString());
		}
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
		ModRegistry.ModGuis.put(ModRegistry.GuiNetherGate,  GuiNetherGate.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiModularHouse, GuiModularHouse.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiDrafter, GuiDrafter.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiBasicStructure, GuiBasicStructure.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiVillagerHouses, GuiVillaerHouses.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiModerateHouse, GuiModerateHouse.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiBulldozer, GuiBulldozer.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiInstantBridge, GuiInstantBridge.class);
	}

	/**
	 * This should only be used for registering recipes for vanilla objects and not mod-specific objects.
	 * @param name The name of the recipe. ModID is pre-pended to it.
	 * @param stack The output of the recipe.
	 * @param recipeComponents The recipe components.
	 */
	public static ShapedRecipes AddShapedRecipe(String name, String groupName, ItemStack stack, Object... recipeComponents)
	{	
		name = Prefab.MODID.toLowerCase().replace(' ', '_') + ":" + name;

		ShapedPrimer primer = CraftingHelper.parseShaped(recipeComponents);
		ShapedRecipes shapedrecipes = new ShapedRecipes(groupName, primer.width, primer.height, primer.input, stack);
		shapedrecipes.setRegistryName(name);
		ForgeRegistries.RECIPES.register(shapedrecipes);

		return shapedrecipes;
	}

	/**
	 * This should only be used for registering recipes for vanilla objects and not mod-specific objects.
	 * @param name The name of the recipe.
	 * @param stack The output stack.
	 * @param recipeComponents The recipe components.
	 */
	public static ShapelessRecipes AddShapelessRecipe(String name, String groupName, ItemStack stack, Object... recipeComponents)
	{
		name = Prefab.MODID.toLowerCase().replace(' ', '_') + ":" + name;
		NonNullList<Ingredient> list = NonNullList.create();

		for (Object object : recipeComponents)
		{
			if (object instanceof ItemStack)
			{
				list.add(Ingredient.fromStacks(((ItemStack)object).copy()));
			}
			else if (object instanceof Item)
			{
				list.add(Ingredient.fromStacks(new ItemStack((Item)object)));
			}
			else
			{
				if (!(object instanceof Block))
				{
					throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + object.getClass().getName() + "!");
				}

				list.add(Ingredient.fromStacks(new ItemStack((Block)object)));
			}
		}

		ShapelessRecipes shapelessRecipes = new ShapelessRecipes(groupName, stack, list);
		shapelessRecipes.setRegistryName(name);
		ForgeRegistries.RECIPES.register(shapelessRecipes);

		return shapelessRecipes;
	}
}