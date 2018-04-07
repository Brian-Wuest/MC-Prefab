package com.wuest.prefab.Config;

import java.util.HashMap;
import java.util.Map.Entry;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.UpdateChecker;
import com.wuest.prefab.Gui.GuiLangKeys;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;

/**
 * This class is used to hold the mod configuration.
 * 
 * @author WuestMan
 *
 */
public class ModConfiguration
{
	public static String OPTIONS = "general.options";
	public static String ChestContentOptions = "general.options.chest contents";
	public static String RecipeOptions = "general.options.recipes";
	public static String starterHouseOptions = "general.options.starter house";
	public static String tagKey = "PrefabConfig";

	// Config file option names.
	private static String addHouseItemName = "Add House Item On New Player Join";
	private static String maximumHouseSizeName = "Maximum Starting House Size";
	private static String enableVersionCheckMessageName = "Enable Version Checking";
	private static String enableLoftHouseName = "Enable Loft House";
	private static String includeSpawnersInMasherName = "Include Spawners in Monster Masher";
	private static String enableStructurePreviewName = "Include Structure Previews";
	private static String addModerateHouseInsteadName = "Add Moderate House on World Join";
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
	public boolean addHouseItem;
	public int maximumStartingHouseSize;
	public boolean enableVersionCheckMessage;
	public boolean enableLoftHouse;
	public boolean includeSpawnersInMasher;
	public boolean enableStructurePreview;
	public boolean addModerateHouseInstead;
	public boolean includeMineshaftChest;

	// Chest content options.
	public boolean addSword;
	public boolean addAxe;
	public boolean addHoe;
	public boolean addShovel;
	public boolean addPickAxe;
	public boolean addArmor;
	public boolean addFood;
	public boolean addCrops;
	public boolean addDirt;
	public boolean addCobble;
	public boolean addSaplings;
	public boolean addTorches;

	// Start House options.
	public boolean addBed;
	public boolean addCraftingTable;
	public boolean addFurnace;
	public boolean addChests;
	public boolean addChestContents;
	public boolean addFarm;
	public boolean addMineshaft;

	public HashMap<String, Boolean> recipeConfiguration;

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

	public static String[] recipeKeys = new String[]
	{ compressedStoneKey, compressedGlowStoneKey, compressedDirteKey, compressedChestKey, pileOfBricksKey, warehouseKey, produceFarmKey, treeFarmKey,
		chickenCoopKey, fishFarmKey, warehouseUpgradeKey, advancedWarehouseKey, monsterMasherKey, bundleofTimberKey, horseStableKey, netherGateKey,
		advancedChickenCoopKey, advancedHorseStableKey, barnKey, machineryTowerKey, defenseBunkerKey, mineshaftEntranceKey, enderGatewayKey, magicTempleKey,
		instantBridgeKey, paperLanternKey, compressedObsidianKey, villagerHousesKey, phasicBlockKey, smartGlassKey, greenHouseKey, startingHouseKey,
		glassStairsKey, glassSlabsKey, andesiteStairsKey, andesiteSlabsKey, dioriteStairsKey, dioriteSlabKey, graniteStairsKey, grantiteSlabKey,
		moderateHouseKey, grassyPlainsKey, aquaBaseKey, watchTowerKey, bulldozerKey };

	// Version Check Message Info
	public String versionMessage = "";
	public boolean showMessage = false;

	public ModConfiguration()
	{
		this.addHouseItem = true;
		this.maximumStartingHouseSize = 16;
		this.enableVersionCheckMessage = true;
		this.includeSpawnersInMasher = true;
		this.enableStructurePreview = true;
		this.recipeConfiguration = new HashMap<String, Boolean>();
	}

	public static void syncConfig()
	{
		Configuration config = Prefab.config;

		if (Prefab.proxy.proxyConfiguration == null)
		{
			Prefab.proxy.proxyConfiguration = new ModConfiguration();
		}

		// General settings.
		Prefab.proxy.proxyConfiguration.addHouseItem = config.getBoolean(ModConfiguration.addHouseItemName, ModConfiguration.OPTIONS, true,
			"Determines if the house item is added to player inventory when joining the world for the first time. Server configuration overrides client.");
		Prefab.proxy.proxyConfiguration.maximumStartingHouseSize = config.getInt(ModConfiguration.maximumHouseSizeName, ModConfiguration.OPTIONS, 16, 5, 16,
			"Determines the maximum size the starting house can be generated as. Server configuration overrides client.");
		Prefab.proxy.proxyConfiguration.enableVersionCheckMessage = config.getBoolean(ModConfiguration.enableVersionCheckMessageName, ModConfiguration.OPTIONS,
			true,
			"Determines if version checking is enabled when application starts. Also determines if the chat message about old versions is shown when joining a world. Server configuration overrides client.");
		Prefab.proxy.proxyConfiguration.enableLoftHouse = config.getBoolean(ModConfiguration.enableLoftHouseName, ModConfiguration.OPTIONS, false,
			"Determines if the loft starter house is enabled. This house contains Nether materials in it's construction. Server configuration overrides client.");
		Prefab.proxy.proxyConfiguration.includeSpawnersInMasher = config.getBoolean(ModConfiguration.includeSpawnersInMasherName, ModConfiguration.OPTIONS,
			true, "Determines if the spawners for the Monster Masher building are included. Server configuration overrides client.");
		Prefab.proxy.proxyConfiguration.enableStructurePreview = config.getBoolean(ModConfiguration.enableStructurePreviewName, ModConfiguration.OPTIONS, true,
			"Determines if the Preview buttons in structure GUIs and other structure previews functions are enabled. Client side only.");
		Prefab.proxy.proxyConfiguration.addModerateHouseInstead = config.getBoolean(ModConfiguration.addModerateHouseInsteadName, ModConfiguration.OPTIONS,
			false, "Determines if the moderate house item is provided to the player instead of the starting house.");
		Prefab.proxy.proxyConfiguration.includeMineshaftChest = config.getBoolean(ModConfiguration.includeMineshaftChestName, ModConfiguration.OPTIONS, true,
			"Determines if the mineshaft chest is included when building mineshafts for various structures.");

		// Make this property require a restart.
		config.get(ModConfiguration.OPTIONS, ModConfiguration.enableVersionCheckMessageName, true).setRequiresMcRestart(true);
		config.get(ModConfiguration.OPTIONS, ModConfiguration.enableLoftHouseName, false).setRequiresMcRestart(true);

		config.setCategoryComment(ModConfiguration.ChestContentOptions,
			"This category is to determine the contents of the chest created by the house item. When playing on a server, the server configuration is used.");

		Prefab.proxy.proxyConfiguration.addSword = config.getBoolean(ModConfiguration.addSwordName, ModConfiguration.ChestContentOptions, true,
			"Determines if a Stone Sword is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addAxe = config.getBoolean(ModConfiguration.addAxeName, ModConfiguration.ChestContentOptions, true,
			"Determines if a Stone Axe is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addShovel = config.getBoolean(ModConfiguration.addShovelName, ModConfiguration.ChestContentOptions, true,
			"Determines if a Stone Shovel is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addHoe = config.getBoolean(ModConfiguration.addHoeName, ModConfiguration.ChestContentOptions, true,
			"Determines if a Stone Hoe is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addPickAxe = config.getBoolean(ModConfiguration.addPickAxeName, ModConfiguration.ChestContentOptions, true,
			"Determines if a Stone Pickaxe is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addArmor = config.getBoolean(ModConfiguration.addArmorName, ModConfiguration.ChestContentOptions, true,
			"Determines if Leather Armor is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addFood = config.getBoolean(ModConfiguration.addFoodName, ModConfiguration.ChestContentOptions, true,
			"Determines if Bread is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addCrops = config.getBoolean(ModConfiguration.addCropsName, ModConfiguration.ChestContentOptions, true,
			"Determines if seeds, potatoes and carros are added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addDirt = config.getBoolean(ModConfiguration.addDirtName, ModConfiguration.ChestContentOptions, true,
			"Determines if a stack of dirt is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addCobble = config.getBoolean(ModConfiguration.addCobbleName, ModConfiguration.ChestContentOptions, true,
			"Determines if a stack of cobble is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addSaplings = config.getBoolean(ModConfiguration.addSaplingsName, ModConfiguration.ChestContentOptions, true,
			"Determines if a set of oak saplings are added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addTorches = config.getBoolean(ModConfiguration.addTorchesName, ModConfiguration.ChestContentOptions, true,
			"Determines if a set of torches are added the the chest when the house is created.");

		config.setCategoryComment(ModConfiguration.starterHouseOptions,
			"This category is to determine which starter house options are enabled in the starter house item screen. Certain options also affect the moderate house as well.");
		Prefab.proxy.proxyConfiguration.addBed = config.getBoolean(ModConfiguration.addBedName, ModConfiguration.starterHouseOptions, true,
			"Determines if the bed is included in the starter house. When playing on a server, the server configuration is used");
		Prefab.proxy.proxyConfiguration.addCraftingTable = config.getBoolean(ModConfiguration.addCraftingTableName, ModConfiguration.starterHouseOptions, true,
			"Determines if the crafting table is included in the starter house. When playing on a server, the server configuration is used");
		Prefab.proxy.proxyConfiguration.addFurnace = config.getBoolean(ModConfiguration.addFurnaceName, ModConfiguration.starterHouseOptions, true,
			"Determines if the furnace is included in the starter house. When playing on a server, the server configuration is used");
		Prefab.proxy.proxyConfiguration.addChests = config.getBoolean(ModConfiguration.addChestsName, ModConfiguration.starterHouseOptions, true,
			"Determines if chests are included in the starter house. When playing on a server, the server configuration is used");
		Prefab.proxy.proxyConfiguration.addChestContents = config.getBoolean(ModConfiguration.addChestContentsName, ModConfiguration.starterHouseOptions, true,
			"Determines if the chest contents is included in the starter house. When playing on a server, the server configuration is used");
		Prefab.proxy.proxyConfiguration.addFarm = config.getBoolean(ModConfiguration.addFarmName, ModConfiguration.starterHouseOptions, true,
			"Determines if the farm is included in the basic starter house. When playing on a server, the server configuration is used");
		Prefab.proxy.proxyConfiguration.addMineshaft = config.getBoolean(ModConfiguration.addMineshaftName, ModConfiguration.starterHouseOptions, true,
			"Determines if the mineshaft is included in the starter house. When playing on a server, the server configuration is used");

		config.setCategoryComment(ModConfiguration.RecipeOptions, "This category determines if the recipes for the blocks/items in this are enabled");
		config.setCategoryRequiresMcRestart(ModConfiguration.RecipeOptions, true);
		config.setCategoryRequiresWorldRestart(ModConfiguration.starterHouseOptions, true);

		// Recipe configuration.
		for (String key : ModConfiguration.recipeKeys)
		{
			boolean value = config.getBoolean(key, RecipeOptions, true, "Determines if the recipe(s) associated with the " + key + " are enabled.");
			Prefab.proxy.proxyConfiguration.recipeConfiguration.put(key, value);
		}

		if (config.hasChanged())
		{
			config.save();
		}
	}

	public NBTTagCompound ToNBTTagCompound()
	{
		NBTTagCompound tag = new NBTTagCompound();

		tag.setBoolean(ModConfiguration.addHouseItemName, this.addHouseItem);
		tag.setInteger(ModConfiguration.maximumHouseSizeName, this.maximumStartingHouseSize);
		tag.setBoolean(ModConfiguration.enableVersionCheckMessageName, this.enableVersionCheckMessage);
		tag.setBoolean(ModConfiguration.enableLoftHouseName, this.enableLoftHouse);
		tag.setBoolean(ModConfiguration.includeSpawnersInMasherName, this.includeSpawnersInMasher);
		tag.setBoolean(ModConfiguration.enableStructurePreviewName, this.enableStructurePreview);
		tag.setBoolean(ModConfiguration.addModerateHouseInsteadName, this.addModerateHouseInstead);
		tag.setBoolean(ModConfiguration.includeMineshaftChestName, this.includeMineshaftChest);

		tag.setBoolean(ModConfiguration.addSwordName, this.addSword);
		tag.setBoolean(ModConfiguration.addAxeName, this.addAxe);
		tag.setBoolean(ModConfiguration.addShovelName, this.addShovel);
		tag.setBoolean(ModConfiguration.addHoeName, this.addHoe);
		tag.setBoolean(ModConfiguration.addPickAxeName, this.addPickAxe);
		tag.setBoolean(ModConfiguration.addArmorName, this.addArmor);
		tag.setBoolean(ModConfiguration.addFoodName, this.addFood);
		tag.setBoolean(ModConfiguration.addCropsName, this.addCrops);
		tag.setBoolean(ModConfiguration.addDirtName, this.addDirt);
		tag.setBoolean(ModConfiguration.addCobbleName, this.addCobble);
		tag.setBoolean(ModConfiguration.addSaplingsName, this.addSaplings);
		tag.setBoolean(ModConfiguration.addTorchesName, this.addTorches);

		tag.setBoolean(ModConfiguration.addBedName, this.addBed);
		tag.setBoolean(ModConfiguration.addCraftingTableName, this.addCraftingTable);
		tag.setBoolean(ModConfiguration.addFurnaceName, this.addFurnace);
		tag.setBoolean(ModConfiguration.addChestsName, this.addChests);
		tag.setBoolean(ModConfiguration.addChestContentsName, this.addChestContents);
		tag.setBoolean(ModConfiguration.addFarmName, this.addFarm);
		tag.setBoolean(ModConfiguration.addMineshaftName, this.addMineshaft);

		tag.setString(ModConfiguration.versionMessageName, UpdateChecker.messageToShow);
		tag.setBoolean(ModConfiguration.showMessageName, UpdateChecker.showMessage);

		for (Entry<String, Boolean> entry : this.recipeConfiguration.entrySet())
		{
			tag.setBoolean(entry.getKey(), entry.getValue());
		}

		return tag;
	}

	public static ModConfiguration getFromNBTTagCompound(NBTTagCompound tag)
	{
		ModConfiguration config = new ModConfiguration();

		config.addHouseItem = tag.getBoolean(ModConfiguration.addHouseItemName);
		config.enableVersionCheckMessage = tag.getBoolean(ModConfiguration.enableVersionCheckMessageName);
		config.enableLoftHouse = tag.getBoolean(ModConfiguration.enableLoftHouseName);
		config.maximumStartingHouseSize = tag.getInteger(ModConfiguration.maximumHouseSizeName);
		config.includeSpawnersInMasher = tag.getBoolean(ModConfiguration.includeSpawnersInMasherName);
		config.enableStructurePreview = tag.getBoolean(ModConfiguration.enableStructurePreviewName);
		config.addModerateHouseInstead = tag.getBoolean(ModConfiguration.addModerateHouseInsteadName);
		config.includeMineshaftChest = tag.getBoolean(ModConfiguration.includeMineshaftChestName);

		// Make sure the server admin didn't set the maximum starting size to an
		// invalid value from the configuration file.
		if (config.maximumStartingHouseSize < 5 || config.maximumStartingHouseSize > 16)
		{
			config.maximumStartingHouseSize = 16;
		}

		config.addSword = tag.getBoolean(ModConfiguration.addSwordName);
		config.addAxe = tag.getBoolean(ModConfiguration.addAxeName);
		config.addShovel = tag.getBoolean(ModConfiguration.addShovelName);
		config.addHoe = tag.getBoolean(ModConfiguration.addHoeName);
		config.addPickAxe = tag.getBoolean(ModConfiguration.addPickAxeName);
		config.addArmor = tag.getBoolean(ModConfiguration.addArmorName);
		config.addFood = tag.getBoolean(ModConfiguration.addFoodName);
		config.addCrops = tag.getBoolean(ModConfiguration.addCropsName);
		config.addDirt = tag.getBoolean(ModConfiguration.addDirtName);
		config.addCobble = tag.getBoolean(ModConfiguration.addCobbleName);
		config.addSaplings = tag.getBoolean(ModConfiguration.addSaplingsName);
		config.addTorches = tag.getBoolean(ModConfiguration.addTorchesName);

		config.addBed = tag.getBoolean(ModConfiguration.addBedName);
		config.addCraftingTable = tag.getBoolean(ModConfiguration.addCraftingTableName);
		config.addFurnace = tag.getBoolean(ModConfiguration.addFurnaceName);
		config.addChests = tag.getBoolean(ModConfiguration.addChestsName);
		config.addChestContents = tag.getBoolean(ModConfiguration.addChestContentsName);
		config.addFarm = tag.getBoolean(ModConfiguration.addFarmName);
		config.addMineshaft = tag.getBoolean(ModConfiguration.addMineshaftName);

		config.versionMessage = tag.getString(ModConfiguration.versionMessageName);
		config.showMessage = tag.getBoolean(ModConfiguration.showMessageName);

		for (String key : ModConfiguration.recipeKeys)
		{
			config.recipeConfiguration.put(key, tag.getBoolean(key));
		}

		return config;
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
		Oak(0, GuiLangKeys.WALL_BLOCK_TYPE_OAK),
		Spruce(1, GuiLangKeys.WALL_BLOCK_TYPE_SPRUCE),
		Birch(2, GuiLangKeys.WALL_BLOCK_TYPE_BIRCH),
		Jungle(3, GuiLangKeys.WALL_BLOCK_TYPE_JUNGLE),
		Acacia(4, GuiLangKeys.WALL_BLOCK_TYPE_ACACIA),
		DarkOak(5, GuiLangKeys.WALL_BLOCK_TYPE_DARK_OAK);

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
