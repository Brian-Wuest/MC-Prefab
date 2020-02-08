package com.wuest.prefab.Config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.wuest.prefab.Prefab;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class is used to hold the mod configuration.
 *
 * @author WuestMan
 */
@SuppressWarnings({"AccessStaticViaInstance", "FieldCanBeLocal", "SpellCheckingInspection"})
public class ModConfiguration {
	public static String tagKey = "PrefabConfig";
	// Config file option names.
	static String maximumHouseSizeName = "Maximum Starting House Size";
	static String enableVersionCheckMessageName = "Enable Version Checking";
	static String enableLoftHouseName = "Enable Loft House";
	static String includeSpawnersInMasherName = "Include Spawners in Monster Masher";
	static String enableStructurePreviewName = "Include Structure Previews";
	static String includeMineshaftChestName = "Include Mineshaft Chest";
	// Chest content option names.
	static String addSwordName = "Add Sword";
	static String addAxeName = "Add Axe";
	static String addHoeName = "Add Hoe";
	static String addShovelName = "Add Shovel";
	static String addPickAxeName = "Add Pickaxe";
	static String addArmorName = "Add Armor";
	static String addFoodName = "Add Food";
	static String addCropsName = "Add Crops";
	static String addDirtName = "Add Dirt";
	static String addCobbleName = "Add Cobblestone";
	static String addSaplingsName = "Add Saplings";
	static String addTorchesName = "Add Torches";
	static String startingItemName = "Starting Item";
	// Starter House option names.
	static String addBedName = "Add Bed";
	static String addCraftingTableName = "Add Crafting Table";
	static String addFurnaceName = "Add Furnace";
	static String addChestsName = "Add Chests";
	static String addChestContentsName = "Add Chest Contents";
	static String addMineshaftName = "Add Mineshaft";
	private static String OPTIONS = "general.";
	private static String ChestContentOptions = "general.chest contents.";
	private static String RecipeOptions = "general.recipes.";
	private static String starterHouseOptions = "general.starter house.";
	private static ArrayList<String> validStartingItems = new ArrayList<String>(Arrays.asList("Starting House", "Moderate House", "Structure Part", "Nothing"));
	// Recipe Options
	private static String compressedStoneKey = "Compressed Stone";
	private static String compressedGlowStoneKey = "Compressed Glowstone";
	private static String compressedDirteKey = "Compressed Dirt";
	private static String compressedChestKey = "Compressed Chest";
	private static String pileOfBricksKey = "Pile of Bricks";
	private static String warehouseKey = "Warehouse";
	private static String produceFarmKey = "Produce Farm";
	private static String treeFarmKey = "Tree Farm";
	private static String chickenCoopKey = "Chicken Coop";
	private static String fishFarmKey = "Fish Farm";
	private static String warehouseUpgradeKey = "Warehouse Upgrade";
	private static String advancedWarehouseKey = "Advanced Warehouse";
	private static String monsterMasherKey = "Monster Masher";
	private static String bundleofTimberKey = "Bundle of Timber";
	private static String horseStableKey = "Horse Stable";
	private static String netherGateKey = "Nether Gate";
	private static String advancedChickenCoopKey = "Advanced Chicken Coop";
	private static String advancedHorseStableKey = "Advanced Horse Stable";
	private static String barnKey = "Barn";
	private static String machineryTowerKey = "Machinery Tower";
	private static String defenseBunkerKey = "Defense Bunker";
	private static String mineshaftEntranceKey = "Mineshaft Entrance";
	private static String enderGatewayKey = "Ender Gateway";
	private static String aquaBaseKey = "Aqua Base";
	private static String grassyPlainsKey = "Grassy Plains";
	private static String magicTempleKey = "Magic Temple";
	private static String instantBridgeKey = "Instant Bridge";
	private static String paperLanternKey = "Paper Lantern";
	private static String compressedObsidianKey = "Compressed Obsidian";
	private static String villagerHousesKey = "Villager Houses";
	private static String phasicBlockKey = "Phasic Block";
	private static String smartGlassKey = "Smart Glass";
	private static String greenHouseKey = "Green House";
	private static String startingHouseKey = "Starting House";
	private static String glassStairsKey = "Glass Stairs";
	private static String andesiteStairsKey = "Andesite Stairs";
	private static String dioriteStairsKey = "Diorite Stairs";
	private static String graniteStairsKey = "Granite Stairs";
	private static String glassSlabsKey = "Glass Slabs";
	private static String andesiteSlabsKey = "Andesite Slabs";
	private static String dioriteSlabKey = "Diorite Slabs";
	private static String grantiteSlabKey = "Granite Slabs";
	private static String moderateHouseKey = "Moderate House";
	private static String watchTowerKey = "Watch Tower";
	private static String bulldozerKey = "Bulldozer";
	private static String structurePartKey = "StructurePart";
	private static String jailKey = "Jail";
	private static String saloonKey = "Saloon";
	private static String skiLodgeKey = "Ski Lodge";
	static String[] recipeKeys = new String[]
			{compressedStoneKey, compressedGlowStoneKey, compressedDirteKey, compressedChestKey, pileOfBricksKey, warehouseKey, produceFarmKey, treeFarmKey, chickenCoopKey, fishFarmKey,
					warehouseUpgradeKey, advancedWarehouseKey, monsterMasherKey, bundleofTimberKey, horseStableKey, netherGateKey, advancedChickenCoopKey, advancedHorseStableKey, barnKey,
					machineryTowerKey, defenseBunkerKey, mineshaftEntranceKey, enderGatewayKey, magicTempleKey, instantBridgeKey, paperLanternKey, compressedObsidianKey, villagerHousesKey,
					phasicBlockKey, smartGlassKey, greenHouseKey, startingHouseKey, glassStairsKey, glassSlabsKey, andesiteStairsKey, andesiteSlabsKey, dioriteStairsKey, dioriteSlabKey,
					graniteStairsKey, grantiteSlabKey, moderateHouseKey, grassyPlainsKey, aquaBaseKey, watchTowerKey, bulldozerKey, structurePartKey, jailKey, saloonKey, skiLodgeKey};
	public ServerModConfiguration serverConfiguration;
	// Configuration Options.
	private IntValue maximumStartingHouseSize;
	private BooleanValue enableLoftHouse;
	private BooleanValue includeSpawnersInMasher;
	private BooleanValue enableStructurePreview;
	// Chest content options.
	private BooleanValue includeMineshaftChest;
	private BooleanValue addSword;
	private BooleanValue addAxe;
	private BooleanValue addHoe;
	private BooleanValue addShovel;
	private BooleanValue addPickAxe;
	private BooleanValue addArmor;
	private BooleanValue addFood;
	private BooleanValue addCrops;
	private BooleanValue addDirt;
	private BooleanValue addCobble;
	private BooleanValue addSaplings;
	private BooleanValue addTorches;
	// Start House options.
	private BooleanValue addBed;
	private BooleanValue addCraftingTable;
	private BooleanValue addFurnace;
	private BooleanValue addChests;
	private BooleanValue addChestContents;
	private BooleanValue addMineshaft;
	private ConfigValue<String> startingItem;
	private HashMap<String, BooleanValue> recipeConfiguration;

	public ModConfiguration(ForgeConfigSpec.Builder builder) {
		this.recipeConfiguration = new HashMap<>();
		this.serverConfiguration = new ServerModConfiguration();
		ModConfiguration.buildOptions(this, builder);
	}

	private static void buildOptions(ModConfiguration config, ForgeConfigSpec.Builder builder) {
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

		Prefab.proxy.proxyConfiguration.addBed = builder
				.comment("Determines if the bed is included in the starter house. When playing on a server, the server configuration is used")
				.define(ModConfiguration.starterHouseOptions + ModConfiguration.addBedName, true);

		Prefab.proxy.proxyConfiguration.addCraftingTable = builder
				.comment("Determines if the crafting table is included in the starter house. When playing on a server, the server configuration is used")
				.define(ModConfiguration.starterHouseOptions + ModConfiguration.addCraftingTableName, true);

		Prefab.proxy.proxyConfiguration.addFurnace = builder
				.comment("Determines if the furnace is included in the starter house. When playing on a server, the server configuration is used")
				.define(ModConfiguration.starterHouseOptions + ModConfiguration.addFurnaceName, true);

		Prefab.proxy.proxyConfiguration.addChests = builder
				.comment("Determines if chests are included in the starter house. When playing on a server, the server configuration is used")
				.define(ModConfiguration.starterHouseOptions + ModConfiguration.addChestsName, true);

		Prefab.proxy.proxyConfiguration.addChestContents = builder
				.comment("Determines if the chest contents is included in the starter house. When playing on a server, the server configuration is used")
				.define(ModConfiguration.starterHouseOptions + ModConfiguration.addChestContentsName, true);

		Prefab.proxy.proxyConfiguration.addMineshaft = builder
				.comment("Determines if the mineshaft is included in the starter house. When playing on a server, the server configuration is used")
				.define(ModConfiguration.starterHouseOptions + ModConfiguration.addMineshaftName, true);

		// Recipe configuration.
		for (String key : ModConfiguration.recipeKeys) {
			BooleanValue value = builder
					.comment("Determines if the recipe(s) associated with the " + key + " are enabled.")
					.define(ModConfiguration.RecipeOptions + key, true);

			Prefab.proxy.proxyConfiguration.recipeConfiguration.put(key, value);
		}
	}

	public static void loadConfig(ForgeConfigSpec spec, Path path) {
		Prefab.LOGGER.debug("Loading config file {}", path);

		final CommentedFileConfig configData = CommentedFileConfig.builder(path)
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();

		Prefab.LOGGER.debug("Built TOML config for {}", path.toString());
		configData.load();
		Prefab.LOGGER.debug("Loaded TOML config file {}", path.toString());
		spec.setConfig(configData);

		ModConfiguration.UpdateServerConfig();
	}

	private static void UpdateServerConfig() {
		Prefab.proxy.proxyConfiguration.serverConfiguration.startingItem = Prefab.proxy.proxyConfiguration.startingItem.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.maximumStartingHouseSize = Prefab.proxy.proxyConfiguration.maximumStartingHouseSize.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.enableLoftHouse = Prefab.proxy.proxyConfiguration.enableLoftHouse.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.includeSpawnersInMasher = Prefab.proxy.proxyConfiguration.includeSpawnersInMasher.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.enableStructurePreview = Prefab.proxy.proxyConfiguration.enableStructurePreview.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.includeMineshaftChest = Prefab.proxy.proxyConfiguration.includeMineshaftChest.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addSword = Prefab.proxy.proxyConfiguration.addSword.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addAxe = Prefab.proxy.proxyConfiguration.addAxe.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addShovel = Prefab.proxy.proxyConfiguration.addShovel.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addHoe = Prefab.proxy.proxyConfiguration.addHoe.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addPickAxe = Prefab.proxy.proxyConfiguration.addPickAxe.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addArmor = Prefab.proxy.proxyConfiguration.addArmor.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addFood = Prefab.proxy.proxyConfiguration.addFood.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addCrops = Prefab.proxy.proxyConfiguration.addCrops.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addDirt = Prefab.proxy.proxyConfiguration.addDirt.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addCobble = Prefab.proxy.proxyConfiguration.addCobble.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addSaplings = Prefab.proxy.proxyConfiguration.addSaplings.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addTorches = Prefab.proxy.proxyConfiguration.addTorches.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addBed = Prefab.proxy.proxyConfiguration.addBed.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addCraftingTable = Prefab.proxy.proxyConfiguration.addCraftingTable.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addFurnace = Prefab.proxy.proxyConfiguration.addFurnace.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addChests = Prefab.proxy.proxyConfiguration.addChests.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addChestContents = Prefab.proxy.proxyConfiguration.addChestContents.get();
		Prefab.proxy.proxyConfiguration.serverConfiguration.addMineshaft = Prefab.proxy.proxyConfiguration.addMineshaft.get();

		// Recipe configuration.
		for (String key : ModConfiguration.recipeKeys) {
			Prefab.LOGGER.debug("Setting recipe configuration for key: " + key);
			Prefab.proxy.proxyConfiguration.serverConfiguration.recipeConfiguration.put(key, Prefab.proxy.proxyConfiguration.recipeConfiguration.get(key).get());
		}
	}
}
