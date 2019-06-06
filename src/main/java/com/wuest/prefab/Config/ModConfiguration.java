package com.wuest.prefab.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Gui.GuiLangKeys;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

/**
 * This class is used to hold the mod configuration.
 * 
 * @author WuestMan
 *
 */
public class ModConfiguration
{
	public static String OPTIONS = "general.";
	public static String ChestContentOptions = "general.chest contents.";
	public static String RecipeOptions = "general.recipes.";
	public static String starterHouseOptions = "general.starter house.";
	public static String tagKey = "PrefabConfig";

	// Config file option names.
	private static String maximumHouseSizeName = "Maximum Starting House Size";
	private static String enableVersionCheckMessageName = "Enable Version Checking";
	private static String enableLoftHouseName = "Enable Loft House";
	private static String includeSpawnersInMasherName = "Include Spawners in Monster Masher";
	private static String enableStructurePreviewName = "Include Structure Previews";
	private static String includeMineshaftChestName = "Include Mineshaft Chest";

	// Chest content option names.
	private static String addSwordName = "Add Sword";
	private static String addAxeName = "Add Axe";
	private static String addHoeName = "Add Hoe";
	private static String addShovelName = "Add Shovel";
	private static String addPickAxeName = "Add Pickaxe";
	private static String addArmorName = "Add Armor";
	private static String addFoodName = "Add Food";
	private static String addCropsName = "Add Crops";
	private static String addDirtName = "Add Dirt";
	private static String addCobbleName = "Add Cobblestone";
	private static String addSaplingsName = "Add Saplings";
	private static String addTorchesName = "Add Torches";
	private static String startingItemName = "Starting Item";

	// Starter House option names.
	private static String addBedName = "Add Bed";
	private static String addCraftingTableName = "Add Crafting Table";
	private static String addFurnaceName = "Add Furnace";
	private static String addChestsName = "Add Chests";
	private static String addChestContentsName = "Add Chest Contents";
	private static String addFarmName = "Add Farm";
	private static String addMineshaftName = "Add Mineshaft";

	private static String versionMessageName = "Version Message";
	private static String showMessageName = "Show Message";

	// Configuration Options.
	public IntValue maximumStartingHouseSize;
	public BooleanValue enableVersionCheckMessage;
	public BooleanValue enableLoftHouse;
	public BooleanValue includeSpawnersInMasher;
	public BooleanValue enableStructurePreview;
	public BooleanValue includeMineshaftChest;

	// Chest content options.
	public BooleanValue addSword;
	public BooleanValue addAxe;
	public BooleanValue addHoe;
	public BooleanValue addShovel;
	public BooleanValue addPickAxe;
	public BooleanValue addArmor;
	public BooleanValue addFood;
	public BooleanValue addCrops;
	public BooleanValue addDirt;
	public BooleanValue addCobble;
	public BooleanValue addSaplings;
	public BooleanValue addTorches;

	// Start House options.
	public BooleanValue addBed;
	public BooleanValue addCraftingTable;
	public BooleanValue addFurnace;
	public BooleanValue addChests;
	public BooleanValue addChestContents;
	public BooleanValue addFarm;
	public BooleanValue addMineshaft;

	public ConfigValue<String> startingItem;
	public static ArrayList<String> validStartingItems = new ArrayList<String>(Arrays.asList("Starter House", "Moderate House", "Structure Part", "Nothing"));

	public HashMap<String, BooleanValue> recipeConfiguration;

	// Recipe Options
	public static String compressedStoneKey = "Compressed Stone";
	public static String compressedGlowStoneKey = "Compressed Glowstone";
	public static String compressedDirteKey = "Compressed Dirt";
	public static String compressedChestKey = "Compressed Chest";
	public static String pileOfBricksKey = "Pile of Bricks";
	public static String warehouseKey = "Warehouse";
	public static String produceFarmKey = "Produce Farm";
	public static String treeFarmKey = "Tree Farm";
	public static String chickenCoopKey = "Chicken Coop";
	public static String fishFarmKey = "Fish Farm";
	public static String warehouseUpgradeKey = "Warehouse Upgrade";
	public static String advancedWarehouseKey = "Advanced Warehouse";
	public static String monsterMasherKey = "Monster Masher";
	public static String bundleofTimberKey = "Bundle of Timber";
	public static String horseStableKey = "Horse Stable";
	public static String netherGateKey = "Nether Gate";
	public static String advancedChickenCoopKey = "Advanced Chicken Coop";
	public static String advancedHorseStableKey = "Advanced Horse Stable";
	public static String barnKey = "Barn";
	public static String machineryTowerKey = "Machinery Tower";
	public static String defenseBunkerKey = "Defense Bunker";
	public static String mineshaftEntranceKey = "Mineshaft Entrance";
	public static String enderGatewayKey = "Ender Gateway";
	public static String aquaBaseKey = "Aqua Base";
	public static String grassyPlainsKey = "Grassy Plains";
	public static String magicTempleKey = "Magic Temple";
	public static String instantBridgeKey = "Instant Bridge";
	public static String paperLanternKey = "Paper Lantern";
	public static String compressedObsidianKey = "Compressed Obsidian";
	public static String villagerHousesKey = "Villager Houses";
	public static String phasicBlockKey = "Phasic Block";
	public static String smartGlassKey = "Smart Glass";
	public static String greenHouseKey = "Green House";
	public static String startingHouseKey = "Starting House";
	public static String glassStairsKey = "Glass Stairs";
	public static String andesiteStairsKey = "Andesite Stairs";
	public static String dioriteStairsKey = "Diorite Stairs";
	public static String graniteStairsKey = "Granite Stairs";
	public static String glassSlabsKey = "Glass Slabs";
	public static String andesiteSlabsKey = "Andesite Slabs";
	public static String dioriteSlabKey = "Diorite Slabs";
	public static String grantiteSlabKey = "Granite Slabs";
	public static String moderateHouseKey = "Moderate House";
	public static String watchTowerKey = "Watch Tower";
	public static String bulldozerKey = "Bulldozer";
	public static String structurePartKey = "StructurePart";
	public static String jailKey = "Jail";
	public static String saloonKey = "Saloon";

	public static String[] recipeKeys = new String[]
	{ compressedStoneKey, compressedGlowStoneKey, compressedDirteKey, compressedChestKey, pileOfBricksKey, warehouseKey, produceFarmKey, treeFarmKey, chickenCoopKey, fishFarmKey,
		warehouseUpgradeKey, advancedWarehouseKey, monsterMasherKey, bundleofTimberKey, horseStableKey, netherGateKey, advancedChickenCoopKey, advancedHorseStableKey, barnKey,
		machineryTowerKey, defenseBunkerKey, mineshaftEntranceKey, enderGatewayKey, magicTempleKey, instantBridgeKey, paperLanternKey, compressedObsidianKey, villagerHousesKey,
		phasicBlockKey, smartGlassKey, greenHouseKey, startingHouseKey, glassStairsKey, glassSlabsKey, andesiteStairsKey, andesiteSlabsKey, dioriteStairsKey, dioriteSlabKey,
		graniteStairsKey, grantiteSlabKey, moderateHouseKey, grassyPlainsKey, aquaBaseKey, watchTowerKey, bulldozerKey, structurePartKey, jailKey, saloonKey };

	// Version Check Message Info
	public String versionMessage = "";
	public boolean showMessage = false;
	
	private static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ModConfiguration ConfigInstance = new ModConfiguration();
	

	static
	{
		
	}
	
	public ModConfiguration()
	{
		this.recipeConfiguration = new HashMap<String, BooleanValue>();
	}
	
	public ModConfiguration(ForgeConfigSpec.Builder builder)
	{
		this.recipeConfiguration = new HashMap<String, BooleanValue>();
		ModConfiguration.BUILDER = builder;
		ModConfiguration.buildOptions(this, builder);
	}

	public static void buildOptions(ModConfiguration config, ForgeConfigSpec.Builder builder)
	{
		Prefab.proxy.proxyConfiguration = config;
		builder.comment("General");
		
		Prefab.proxy.proxyConfiguration.startingItem = builder
			.comment("Determines which starting item a player gets on first world join. Server configuration overrides client.")
			.defineInList(OPTIONS + ModConfiguration.startingItemName, "Starting House", validStartingItems);

		Prefab.proxy.proxyConfiguration.maximumStartingHouseSize = builder
			.comment("Determines the maximum size the starting house can be generated as. Server configuration overrides client.")
			.defineInRange(OPTIONS + ModConfiguration.maximumHouseSizeName, 16, 5, 16);
		
		Prefab.proxy.proxyConfiguration.enableLoftHouse = builder
			.comment("Determines if the loft starter house is enabled. This house contains Nether materials in it's construction. Server configuration overrides client.")
			.define(OPTIONS + ModConfiguration.enableLoftHouseName, false);
		
		Prefab.proxy.proxyConfiguration.includeSpawnersInMasher = builder
			.comment("Determines if the spawners for the Monster Masher building are included. Server configuration overrides client.")
			.define(OPTIONS + ModConfiguration.includeSpawnersInMasherName, true);
		
		Prefab.proxy.proxyConfiguration.enableStructurePreview = builder
			.comment("Determines if the Preview buttons in structure GUIs and other structure previews functions are enabled. Client side only.")
			.define(OPTIONS + ModConfiguration.enableStructurePreviewName, true);
		
		Prefab.proxy.proxyConfiguration.includeMineshaftChest = builder
			.comment("Determines if the mineshaft chest is included when building mineshafts for various structures.")
			.define(OPTIONS + ModConfiguration.includeMineshaftChestName, true);

		builder.comment("Chest Options");
		
		Prefab.proxy.proxyConfiguration.addSword = builder.comment("Determines if a Stone Sword is added the the chest when the house is created.")
			.define(ModConfiguration.ChestContentOptions + ModConfiguration.addSwordName, true);
		
		Prefab.proxy.proxyConfiguration.addAxe = builder.comment("Determines if a Stone Axe is added the the chest when the house is created.")
			.define(ModConfiguration.ChestContentOptions + ModConfiguration.addAxeName, true);
		
		Prefab.proxy.proxyConfiguration.addShovel = builder.comment("Determines if a Stone Shovel is added the the chest when the house is created.")
			.define(ModConfiguration.ChestContentOptions + ModConfiguration.addShovelName, true);
		
		Prefab.proxy.proxyConfiguration.addHoe = builder.comment("Determines if a Stone Hoe is added the the chest when the house is created.")
			.define(ModConfiguration.ChestContentOptions + ModConfiguration.addHoeName, true);
		
		Prefab.proxy.proxyConfiguration.addPickAxe = builder.comment("Determines if a Stone Pickaxe is added the the chest when the house is created.")
			.define(ModConfiguration.ChestContentOptions + ModConfiguration.addPickAxeName, true);
		
		Prefab.proxy.proxyConfiguration.addArmor = builder.comment("Determines if Leather Armor is added the the chest when the house is created.")
			.define(ModConfiguration.ChestContentOptions + ModConfiguration.addArmorName, true);
		
		Prefab.proxy.proxyConfiguration.addFood = builder.comment("Determines if Bread is added the the chest when the house is created.")
			.define(ModConfiguration.ChestContentOptions + ModConfiguration.addFoodName, true);
		
		Prefab.proxy.proxyConfiguration.addCrops = builder.comment("Determines if seeds, potatoes and carrots are added the the chest when the house is created.")
			.define(ModConfiguration.ChestContentOptions + ModConfiguration.addCropsName, true);
		
		Prefab.proxy.proxyConfiguration.addDirt = builder.comment("Determines if a stack of dirt is added the the chest when the house is created.")
			.define(ModConfiguration.ChestContentOptions + ModConfiguration.addDirtName, true);
		
		Prefab.proxy.proxyConfiguration.addCobble = builder.comment("Determines if a stack of cobble is added the the chest when the house is created.")
			.define(ModConfiguration.ChestContentOptions + ModConfiguration.addCobbleName, true);
		
		Prefab.proxy.proxyConfiguration.addSaplings = builder.comment("Determines if a set of oak saplings are added the the chest when the house is created.")
			.define(ModConfiguration.ChestContentOptions + ModConfiguration.addSaplingsName, true);
		
		Prefab.proxy.proxyConfiguration.addTorches = builder.comment("Determines if a set of torches are added the the chest when the house is created.")
			.define(ModConfiguration.ChestContentOptions + ModConfiguration.addTorchesName, true);
		
		builder.comment("Starter House Options");
		
		Prefab.proxy.proxyConfiguration.addBed = builder.comment("Determines if the bed is included in the starter house. When playing on a server, the server configuration is used")
			.define(ModConfiguration.starterHouseOptions + ModConfiguration.addBedName, true);
		
		Prefab.proxy.proxyConfiguration.addCraftingTable = builder.comment("Determines if the crafting table is included in the starter house. When playing on a server, the server configuration is used")
			.define(ModConfiguration.starterHouseOptions + ModConfiguration.addCraftingTableName, true);
		
		Prefab.proxy.proxyConfiguration.addFurnace = builder.comment("Determines if the furnace is included in the starter house. When playing on a server, the server configuration is used")
			.define(ModConfiguration.starterHouseOptions + ModConfiguration.addFurnaceName, true);
		
		Prefab.proxy.proxyConfiguration.addChests = builder.comment("Determines if chests are included in the starter house. When playing on a server, the server configuration is used")
			.define(ModConfiguration.starterHouseOptions + ModConfiguration.addChestsName, true);
		
		Prefab.proxy.proxyConfiguration.addChestContents = builder.comment("Determines if the chest contents is included in the starter house. When playing on a server, the server configuration is used")
			.define(ModConfiguration.starterHouseOptions + ModConfiguration.addChestContentsName, true);
		
		Prefab.proxy.proxyConfiguration.addFarm = builder.comment("Determines if the farm is included in the basic starter house. When playing on a server, the server configuration is used")
			.define(ModConfiguration.starterHouseOptions + ModConfiguration.addFarmName, true);
		
		Prefab.proxy.proxyConfiguration.addMineshaft = builder.comment("Determines if the mineshaft is included in the starter house. When playing on a server, the server configuration is used")
			.define(ModConfiguration.starterHouseOptions + ModConfiguration.addMineshaftName, true);
		
		// Recipe configuration.
		for (String key : ModConfiguration.recipeKeys)
		{
			BooleanValue value = builder
				.comment("Determines if the recipe(s) associated with the " + key + " are enabled.")
				.define(ModConfiguration.RecipeOptions + key, true);
			
			Prefab.proxy.proxyConfiguration.recipeConfiguration.put(key, value);
		}
		
		builder.worldRestart();
	}
	
	public enum CeilingFloorBlockType
	{
		StoneBrick(0, GuiLangKeys.CEILING_BLOCK_TYPE_STONE), Brick(1, GuiLangKeys.CEILING_BLOCK_TYPE_BRICK), SandStone(2, GuiLangKeys.CEILING_BLOCK_TYPE_SAND);

		private final int value;
		private final String langKey;

		CeilingFloorBlockType(int newValue, String langKey)
		{
			this.value = newValue;
			this.langKey = langKey;
		}

		public int getValue()
		{
			return value;
		}

		public String getName()
		{
			return GuiLangKeys.translateString(this.langKey);
		}

		public static CeilingFloorBlockType ValueOf(int value)
		{
			switch (value)
			{
				case 1:
				{
					return CeilingFloorBlockType.Brick;
				}

				case 2:
				{
					return CeilingFloorBlockType.SandStone;
				}

				default:
				{
					return CeilingFloorBlockType.StoneBrick;
				}
			}
		}
	}

	public enum WallBlockType
	{
		Oak(0, GuiLangKeys.WALL_BLOCK_TYPE_OAK), Spruce(1, GuiLangKeys.WALL_BLOCK_TYPE_SPRUCE), Birch(2, GuiLangKeys.WALL_BLOCK_TYPE_BIRCH), Jungle(3,
			GuiLangKeys.WALL_BLOCK_TYPE_JUNGLE), Acacia(4, GuiLangKeys.WALL_BLOCK_TYPE_ACACIA), DarkOak(5, GuiLangKeys.WALL_BLOCK_TYPE_DARK_OAK);

		private final int value;
		private final String langKey;

		WallBlockType(final int newValue, String langKey)
		{
			value = newValue;
			this.langKey = langKey;
		}

		public int getValue()
		{
			return value;
		}

		public String getName()
		{
			return GuiLangKeys.translateString(this.langKey);
		}

		public static WallBlockType ValueOf(int value)
		{
			switch (value)
			{
				case 1:
				{
					return WallBlockType.Spruce;
				}

				case 2:
				{
					return WallBlockType.Birch;
				}

				case 3:
				{
					return WallBlockType.Jungle;
				}

				case 4:
				{
					return WallBlockType.Acacia;
				}

				case 5:
				{
					return WallBlockType.DarkOak;
				}

				default:
				{
					return WallBlockType.Oak;
				}
			}
		}
	}
}
