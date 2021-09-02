package com.wuest.prefab.config;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.UpdateChecker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * This class is used to hold the mod configuration.
 *
 * @author WuestMan
 */
public class ModConfiguration {
    public static String OPTIONS = "general.options";
    public static String ChestContentOptions = "general.options.chest contents";
    public static String RecipeOptions = "general.options.recipes";
    public static String starterHouseOptions = "general.options.starter house";
    public static String tagKey = "PrefabConfig";
    public static String[] validStartingItems = new String[]
            {"Starter House", "Moderate House", "Structure Part", "Nothing"};

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
    public static String bundleOfTimberKey = "Bundle of Timber";
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
    public static String graniteSlabKey = "Granite Slabs";
    public static String moderateHouseKey = "Moderate House";
    public static String watchTowerKey = "Watch Tower";
    public static String bulldozerKey = "Bulldozer";
    public static String structurePartKey = "StructurePart";
    public static String jailKey = "Jail";
    public static String saloonKey = "Saloon";
    public static String[] recipeKeys = new String[]
            {compressedStoneKey, compressedGlowStoneKey, compressedDirteKey, compressedChestKey, pileOfBricksKey, warehouseKey, produceFarmKey, treeFarmKey, chickenCoopKey, fishFarmKey,
                    warehouseUpgradeKey, advancedWarehouseKey, monsterMasherKey, bundleOfTimberKey, horseStableKey, netherGateKey, advancedChickenCoopKey, advancedHorseStableKey, barnKey,
                    machineryTowerKey, defenseBunkerKey, mineshaftEntranceKey, enderGatewayKey, magicTempleKey, instantBridgeKey, paperLanternKey, compressedObsidianKey, villagerHousesKey,
                    phasicBlockKey, smartGlassKey, greenHouseKey, startingHouseKey, glassStairsKey, glassSlabsKey, andesiteStairsKey, andesiteSlabsKey, dioriteStairsKey, dioriteSlabKey,
                    graniteStairsKey, graniteSlabKey, moderateHouseKey, grassyPlainsKey, aquaBaseKey, watchTowerKey, bulldozerKey, structurePartKey, jailKey, saloonKey};

    // Config file option names.
    private static String enableVersionCheckMessageName = "Enable Version Checking";
    private static String enableLoftHouseName = "Enable Loft House";
    private static String includeSpawnersInMasherName = "Include Spawners in Monster Masher";
    private static String enableStructurePreviewName = "Include Structure Previews";
    private static String includeMineshaftChestName = "Include Mineshaft Chest";
    private static String allowWaterInNonOverworldDimensionsName = "Include Water In Overworld Dimension Only";

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
    private static String allowBulldozerToCreateDropsName = "Bulldozer Creates Drops";
    private static String versionMessageName = "Version Message";
    private static String showMessageName = "Show Message";

    // Configuration Options.
    public boolean enableVersionCheckMessage;
    public boolean enableLoftHouse;
    public boolean includeSpawnersInMasher;
    public boolean enableStructurePreview;
    public boolean includeMineshaftChest;
    public boolean allowWaterInNonOverworldDimensions;
    public boolean allowBulldozerToCreateDrops;

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
    public String startingItem;
    public HashMap<String, Boolean> recipeConfiguration;

    // Version Check Message Info
    public String versionMessage = "";
    public boolean showMessage = false;

    public ModConfiguration() {
        this.enableVersionCheckMessage = true;
        this.includeSpawnersInMasher = true;
        this.enableStructurePreview = true;
        this.recipeConfiguration = new HashMap<String, Boolean>();
    }

    public static void syncConfig() {
        Configuration config = Prefab.config;

        if (Prefab.proxy.proxyConfiguration == null) {
            Prefab.proxy.proxyConfiguration = new ModConfiguration();
        }

        ConfigCategory mainCategory = config.getCategory(ModConfiguration.OPTIONS);

        // General settings.
        Prefab.proxy.proxyConfiguration.startingItem = config.getString(ModConfiguration.startingItemName, ModConfiguration.OPTIONS, "Starting House",
                "Determines which starting item a player gets on first world join. Server configuration overrides client.", validStartingItems);
        Property startingItemProperty = mainCategory.get(ModConfiguration.startingItemName);

        Prefab.proxy.proxyConfiguration.enableVersionCheckMessage = config.getBoolean(ModConfiguration.enableVersionCheckMessageName, ModConfiguration.OPTIONS, true,
                "Determines if version checking is enabled when application starts. Also determines if the chat message about old versions is shown when joining a world. Server configuration overrides client.");
        Prefab.proxy.proxyConfiguration.enableLoftHouse = config.getBoolean(ModConfiguration.enableLoftHouseName, ModConfiguration.OPTIONS, false,
                "Determines if the loft starter house is enabled. This house contains Nether materials in it's construction. Server configuration overrides client.");
        Prefab.proxy.proxyConfiguration.includeSpawnersInMasher = config.getBoolean(ModConfiguration.includeSpawnersInMasherName, ModConfiguration.OPTIONS, true,
                "Determines if the spawners for the Monster Masher building are included. Server configuration overrides client.");
        Prefab.proxy.proxyConfiguration.enableStructurePreview = config.getBoolean(ModConfiguration.enableStructurePreviewName, ModConfiguration.OPTIONS, true,
                "Determines if the Preview buttons in structure GUIs and other structure previews functions are enabled. Client side only.");
        Prefab.proxy.proxyConfiguration.includeMineshaftChest = config.getBoolean(ModConfiguration.includeMineshaftChestName, ModConfiguration.OPTIONS, true,
                "Determines if the mineshaft chest is included when building mineshafts for various structures.");

        Prefab.proxy.proxyConfiguration.allowWaterInNonOverworldDimensions = config.getBoolean(ModConfiguration.allowWaterInNonOverworldDimensionsName, ModConfiguration.OPTIONS, true,
                "Determines if water can be generated in structures when the current dimension is not the oveworld. Does not affect Nether.");

        Prefab.proxy.proxyConfiguration.allowBulldozerToCreateDrops = config.getBoolean(ModConfiguration.allowBulldozerToCreateDropsName, ModConfiguration.OPTIONS, true,
                "Determines if the bulldozer item can create drops when it clears an area.");

        // Make this property require a restart.
        config.get(ModConfiguration.OPTIONS, ModConfiguration.enableVersionCheckMessageName, true).setRequiresMcRestart(true);
        config.get(ModConfiguration.OPTIONS, ModConfiguration.enableLoftHouseName, false).setRequiresMcRestart(true);

        Property startingHouseProperty = null;
        Property moderateHouseProperty = null;

        if (mainCategory.containsKey("Add House Item On New Player Join")) {
            startingHouseProperty = mainCategory.remove("Add House Item On New Player Join");
        }

        if (mainCategory.containsKey("Add Moderate House on World Join")) {
            moderateHouseProperty = mainCategory.remove("Add Moderate House on World Join");
        }

        if (startingHouseProperty != null) {
            if (moderateHouseProperty == null) {
                startingItemProperty.set(startingHouseProperty.getBoolean() ? "Starting House" : "Nothing");
            } else {
                if (startingHouseProperty.getBoolean()) {
                    startingItemProperty.set(moderateHouseProperty.getBoolean() ? "Moderate House" : "Starting House");
                } else {
                    startingItemProperty.set("Nothing");
                }
            }

            Prefab.proxy.proxyConfiguration.startingItem = startingItemProperty.getString();
        }

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
        for (String key : ModConfiguration.recipeKeys) {
            boolean value = config.getBoolean(key, RecipeOptions, true, "Determines if the recipe(s) associated with the " + key + " are enabled.");
            Prefab.proxy.proxyConfiguration.recipeConfiguration.put(key, value);
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static ModConfiguration getFromNBTTagCompound(NBTTagCompound tag) {
        ModConfiguration config = new ModConfiguration();

        config.startingItem = tag.getString(ModConfiguration.startingItemName);
        config.enableVersionCheckMessage = tag.getBoolean(ModConfiguration.enableVersionCheckMessageName);
        config.enableLoftHouse = tag.getBoolean(ModConfiguration.enableLoftHouseName);
        config.includeSpawnersInMasher = tag.getBoolean(ModConfiguration.includeSpawnersInMasherName);
        config.enableStructurePreview = tag.getBoolean(ModConfiguration.enableStructurePreviewName);
        config.includeMineshaftChest = tag.getBoolean(ModConfiguration.includeMineshaftChestName);
        config.allowWaterInNonOverworldDimensions = tag.getBoolean(ModConfiguration.allowWaterInNonOverworldDimensionsName);
        config.allowBulldozerToCreateDrops = tag.getBoolean(ModConfiguration.allowBulldozerToCreateDropsName);

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

        for (String key : ModConfiguration.recipeKeys) {
            config.recipeConfiguration.put(key, tag.getBoolean(key));
        }

        return config;
    }

    public NBTTagCompound ToNBTTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString(ModConfiguration.startingItemName, this.startingItem);
        tag.setBoolean(ModConfiguration.enableVersionCheckMessageName, this.enableVersionCheckMessage);
        tag.setBoolean(ModConfiguration.enableLoftHouseName, this.enableLoftHouse);
        tag.setBoolean(ModConfiguration.includeSpawnersInMasherName, this.includeSpawnersInMasher);
        tag.setBoolean(ModConfiguration.enableStructurePreviewName, this.enableStructurePreview);
        tag.setBoolean(ModConfiguration.includeMineshaftChestName, this.includeMineshaftChest);
        tag.setBoolean(ModConfiguration.allowWaterInNonOverworldDimensionsName, this.allowWaterInNonOverworldDimensions);
        tag.setBoolean(ModConfiguration.allowBulldozerToCreateDropsName, this.allowBulldozerToCreateDrops);

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

        for (Entry<String, Boolean> entry : this.recipeConfiguration.entrySet()) {
            tag.setBoolean(entry.getKey(), entry.getValue());
        }

        return tag;
    }
}
