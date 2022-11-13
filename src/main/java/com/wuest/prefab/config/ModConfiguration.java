package com.wuest.prefab.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.structures.config.BasicStructureConfiguration;
import com.wuest.prefab.structures.config.HouseAdvancedConfiguration;
import com.wuest.prefab.structures.config.HouseConfiguration;
import com.wuest.prefab.structures.config.HouseImprovedConfiguration;
import com.wuest.prefab.structures.config.enums.BaseOption;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

import java.nio.file.Path;
import java.util.*;

/**
 * This class is used to hold the mod configuration.
 *
 * @author WuestMan
 */
@SuppressWarnings({"AccessStaticViaInstance", "FieldCanBeLocal", "SpellCheckingInspection"})
public class ModConfiguration {
    private static final String OPTIONS = "general.";
    private static final String ChestContentOptions = "general.chest contents.";
    private static final String RecipeOptions = "general.recipes.";
    private static final String StructureOptions = "general.structure options.";
    private static final String starterHouseOptions = "general.starter house.";
    private static final ArrayList<String> validStartingItems = new ArrayList(Arrays.asList("Starting House", "Moderate House", "Nothing"));
    // Recipe Options
    private static final String compressedStoneKey = "Compressed Stone";
    private static final String compressedGlowStoneKey = "Compressed Glowstone";
    private static final String compressedDirteKey = "Compressed Dirt";
    private static final String compressedChestKey = "Compressed Chest";
    private static final String pileOfBricksKey = "Pile of Bricks";
    private static final String warehouseKey = "Warehouse";
    private static final String warehouseUpgradeKey = "Upgrade";
    private static final String advancedWarehouseKey = "Improved Warehouse";
    private static final String bundleofTimberKey = "Bundle of Timber";
    private static final String netherGateKey = "Nether Gate";
    private static final String machineryTowerKey = "Machinery Tower";
    private static final String defenseBunkerKey = "Defense Bunker";
    private static final String mineshaftEntranceKey = "Mineshaft Entrance";
    private static final String enderGatewayKey = "Ender Gateway";
    private static final String aquaBaseKey = "Aqua Base";
    private static final String grassyPlainsKey = "Grassy Plains";
    private static final String magicTempleKey = "Magic Temple";
    private static final String instantBridgeKey = "Instant Bridge";
    private static final String paperLanternKey = "Paper Lantern";
    private static final String compressedObsidianKey = "Compressed Obsidian";
    private static final String villagerHousesKey = "Villager Houses";
    private static final String phasicBlockKey = "Phasic Block";
    private static final String smartGlassKey = "Smart Glass";
    private static final String greenHouseKey = "Green House";
    private static final String startingHouseKey = "House";
    private static final String glassStairsKey = "Glass Stairs";
    private static final String glassSlabsKey = "Glass Slabs";
    private static final String moderateHouseKey = "Improved House";
    private static final String watchTowerKey = "Watch Tower";
    private static final String bulldozerKey = "Bulldozer";
    private static final String jailKey = "Jail";
    private static final String saloonKey = "Saloon";
    private static final String skiLodgeKey = "Ski Lodge";
    private static final String windMillKey = "Windmill";
    private static final String townHallKey = "Town Hall";
    private static final String heapOfTimberKey = "Heap of Timber";
    private static final String tonOfTimberKey = "Ton of Timber";
    private static final String workshopKey = "Workshop";
    private static final String modernBuildingsKey = "Modern Buildings";
    public static String SwiftBladeKey = "Swift Blade";
    public static String SickleKey = "Sickle";
    public static String DirtRecipesKey = "Dirt Recipes";
    public static String BunchOfBeetsKey = "Bunch of Beets";
    public static String BunchOfCarrotsKey = "Bunch of Carrots";
    public static String BunchOfPotatoesKey = "Bunch of Potatoes";
    public static String BunchOfEggsKey = "Bunch of Eggs";
    public static String WoodenCrateKey = "Wooden Crate";
    public static String starterFarmKey = "Farm";
    public static String moderateFarmKey = "Improved Farm";
    public static String advancedFarmKey = "Advanced Farm";
    public static String quartzCreteKey = "Quartz-Crete";
    public static String houseAdvancedKey = "Advanced House";
    public static String darkLightLampKey = "Dark Light Lamp";
    public static String lightSwitchKey = "Light Switch";

    public static String tagKey = "PrefabConfig";

    // Config file option names.
    static String enableVersionCheckMessageName = "Enable Version Checking";
    static String includeSpawnersInMasherName = "Include Spawners in Monster Masher";
    static String enableStructurePreviewName = "Include Structure Previews";
    static String includeMineshaftChestName = "Include Mineshaft Chest";
    static String allowWaterInNonOverworldDimensionsName = "Include Water In Non-Overworld Dimesions";
    static String playBuildingSoundName = "Play Building Sound";

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

    static String newPlayersGetStartingItemName = "New Players Get Starting Item";

    // Starter House option names.
    static String addBedName = "Add Bed";
    static String addCraftingTableName = "Add Crafting Table";
    static String addFurnaceName = "Add Furnace";
    static String addChestsName = "Add Chests";
    static String addChestContentsName = "Add Chest Contents";
    static String addMineshaftName = "Add Mineshaft";
    static String allowBulldozerToCreateDropsName = "Bulldozer Creates Drops";

    static String structureOptionsName = "Structure Options";

    static String[] recipeKeys = new String[]
            {compressedStoneKey, compressedGlowStoneKey, compressedDirteKey, compressedChestKey, pileOfBricksKey, warehouseKey,
                    warehouseUpgradeKey, advancedWarehouseKey, bundleofTimberKey, netherGateKey,
                    machineryTowerKey, defenseBunkerKey, mineshaftEntranceKey, enderGatewayKey, magicTempleKey, instantBridgeKey, paperLanternKey, compressedObsidianKey, villagerHousesKey,
                    phasicBlockKey, smartGlassKey, greenHouseKey, startingHouseKey, glassStairsKey, glassSlabsKey,
                     moderateHouseKey, grassyPlainsKey, aquaBaseKey, watchTowerKey, bulldozerKey, jailKey, saloonKey, skiLodgeKey,
                    windMillKey, townHallKey, heapOfTimberKey, tonOfTimberKey, workshopKey, modernBuildingsKey, SwiftBladeKey, SickleKey, DirtRecipesKey, BunchOfBeetsKey,
                    BunchOfCarrotsKey, BunchOfPotatoesKey, BunchOfEggsKey, WoodenCrateKey, starterFarmKey, moderateFarmKey, advancedFarmKey, quartzCreteKey, houseAdvancedKey,
                    darkLightLampKey, lightSwitchKey};

    private static ForgeConfigSpec SPEC;
    private final HashMap<String, BooleanValue> recipeConfiguration;
    public HashMap<String, HashMap<String, BooleanValue>> structureOptions = new HashMap<>();
    public ServerModConfiguration serverConfiguration;
    public ArrayList<ConfigOption<?>> configOptions;
    // Configuration Options.
    private BooleanValue includeSpawnersInMasher;
    private BooleanValue enableStructurePreview;
    private BooleanValue allowBulldozerToCreateDrops;
    private BooleanValue allowWaterInNonOverworldDimensions;
    private BooleanValue playBuildingSound;

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

    private BooleanValue newPlayersGetStartingItem;

    public ModConfiguration(ForgeConfigSpec.Builder builder) {
        this.recipeConfiguration = new HashMap<>();
        this.serverConfiguration = new ServerModConfiguration();
        this.configOptions = new ArrayList<>();

        ModConfiguration.buildOptions(this, builder);
    }

    private static void buildOptions(ModConfiguration config, ForgeConfigSpec.Builder builder) {
        Prefab.proxy.proxyConfiguration = config;
        builder.comment("General");

        Prefab.proxy.proxyConfiguration.startingItem = builder
                .comment("Determines which starting item a player gets on first world join. Valid values for this option are: \"Starting House\", \"Moderate House\", \"Nothing\". Server configuration overrides client.")
                .defineInList(OPTIONS + ModConfiguration.startingItemName, "Starting House", validStartingItems);

        config.configOptions.add(new ConfigOption<String>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.startingItem)
                .setName(ModConfiguration.startingItemName)
                .setCategory(ConfigCategory.General)
                .setConfigType("String")
                .setDefaultValue("Starting House")
                .setHoverText("Determines which starting item a player gets on first world join. Valid values for this option are: \"Starting House\", \"Moderate House\", \"Nothing\". Server configuration overrides client.")
                .setValidValues(validStartingItems));

        Prefab.proxy.proxyConfiguration.newPlayersGetStartingItem = builder
                .comment("Can new players for a world receive a starting item?. Server configuration overrides client.")
                .define(OPTIONS + ModConfiguration.newPlayersGetStartingItemName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.newPlayersGetStartingItem)
                .setName(ModConfiguration.newPlayersGetStartingItemName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.General)
                .setHoverText("Can new players for a world receive a starting item?. Server configuration overrides client.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.includeSpawnersInMasher = builder
                .comment("Determines if the spawners for the Monster Masher building are included. Server configuration overrides client.")
                .define(OPTIONS + ModConfiguration.includeSpawnersInMasherName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.includeSpawnersInMasher)
                .setName(ModConfiguration.includeSpawnersInMasherName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.General)
                .setHoverText("Determines if the spawners for the Monster Masher building are included. Server configuration overrides client.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.enableStructurePreview = builder
                .comment("Determines if the Preview buttons in structure GUIs and other structure previews functions are enabled. Client side only.")
                .define(OPTIONS + ModConfiguration.enableStructurePreviewName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.enableStructurePreview)
                .setName(ModConfiguration.enableStructurePreviewName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.General)
                .setHoverText("Determines if the Preview buttons in structure GUIs and other structure previews functions are enabled. Client side only.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.includeMineshaftChest = builder
                .comment("Determines if the mineshaft chest is included when building mineshafts for various structures.")
                .define(OPTIONS + ModConfiguration.includeMineshaftChestName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.includeMineshaftChest)
                .setName(ModConfiguration.includeMineshaftChestName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.General)
                .setHoverText("Determines if the mineshaft chest is included when building mineshafts for various structures.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.allowBulldozerToCreateDrops = builder
                .comment("Determines if the bulldozer item can create drops when it clears an area.")
                .define(OPTIONS + ModConfiguration.allowBulldozerToCreateDropsName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.allowBulldozerToCreateDrops)
                .setName(ModConfiguration.allowBulldozerToCreateDropsName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.General)
                .setHoverText("Determines if the bulldozer item can create drops when it clears an area.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.allowWaterInNonOverworldDimensions = builder
                .comment("Determines if water can be generated in structures when the current dimension is not the oveworld. Does not affect Nether")
                .define(OPTIONS + ModConfiguration.allowWaterInNonOverworldDimensionsName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.allowWaterInNonOverworldDimensions)
                .setName(ModConfiguration.allowWaterInNonOverworldDimensionsName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.General)
                .setHoverText("Determines if water can be generated in structures when the current dimension is not the oveworld. Does not affect Nether")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.playBuildingSound = builder
                .comment("Play Sound When Generating Blueprint")
                .define(OPTIONS + ModConfiguration.playBuildingSoundName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.playBuildingSound)
                .setName(ModConfiguration.playBuildingSoundName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.General)
                .setHoverText("Play Sound When Generating Blueprint")
                .setDefaultValue(true));

        builder.comment("Chest Options");

        Prefab.proxy.proxyConfiguration.addSword = builder.comment("Determines if a Stone Sword is added the the chest when the house is created.")
                .define(ModConfiguration.ChestContentOptions + ModConfiguration.addSwordName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addSword)
                .setName(ModConfiguration.addSwordName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.ChestOptions)
                .setHoverText("Determines if a Stone Sword is added the the chest when the house is created.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addAxe = builder.comment("Determines if a Stone Axe is added the the chest when the house is created.")
                .define(ModConfiguration.ChestContentOptions + ModConfiguration.addAxeName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addAxe)
                .setName(ModConfiguration.addAxeName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.ChestOptions)
                .setHoverText("Determines if a Stone Axe is added the the chest when the house is created.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addShovel = builder.comment("Determines if a Stone Shovel is added the the chest when the house is created.")
                .define(ModConfiguration.ChestContentOptions + ModConfiguration.addShovelName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addShovel)
                .setName(ModConfiguration.addShovelName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.ChestOptions)
                .setHoverText("Determines if a Stone Shovel is added the the chest when the house is created.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addHoe = builder.comment("Determines if a Stone Hoe is added the the chest when the house is created.")
                .define(ModConfiguration.ChestContentOptions + ModConfiguration.addHoeName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addHoe)
                .setName(ModConfiguration.addHoeName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.ChestOptions)
                .setHoverText("Determines if a Stone Hoe is added the the chest when the house is created.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addPickAxe = builder.comment("Determines if a Stone Pickaxe is added the the chest when the house is created.")
                .define(ModConfiguration.ChestContentOptions + ModConfiguration.addPickAxeName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addPickAxe)
                .setName(ModConfiguration.addPickAxeName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.ChestOptions)
                .setHoverText("Determines if a Stone Pickaxe is added the the chest when the house is created.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addArmor = builder.comment("Determines if Leather Armor is added the the chest when the house is created.")
                .define(ModConfiguration.ChestContentOptions + ModConfiguration.addArmorName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addArmor)
                .setName(ModConfiguration.addArmorName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.ChestOptions)
                .setHoverText("Determines if Leather Armor is added the the chest when the house is created.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addFood = builder.comment("Determines if Bread is added the the chest when the house is created.")
                .define(ModConfiguration.ChestContentOptions + ModConfiguration.addFoodName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addFood)
                .setName(ModConfiguration.addFoodName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.ChestOptions)
                .setHoverText("Determines if Bread is added the the chest when the house is created.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addCrops = builder.comment("Determines if seeds, potatoes and carrots are added the the chest when the house is created.")
                .define(ModConfiguration.ChestContentOptions + ModConfiguration.addCropsName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addCrops)
                .setName(ModConfiguration.addCropsName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.ChestOptions)
                .setHoverText("Determines if seeds, potatoes and carrots are added the the chest when the house is created.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addDirt = builder.comment("Determines if a stack of dirt is added the the chest when the house is created.")
                .define(ModConfiguration.ChestContentOptions + ModConfiguration.addDirtName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addDirt)
                .setName(ModConfiguration.addDirtName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.ChestOptions)
                .setHoverText("Determines if a stack of dirt is added the the chest when the house is created.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addCobble = builder.comment("Determines if a stack of cobble is added the the chest when the house is created.")
                .define(ModConfiguration.ChestContentOptions + ModConfiguration.addCobbleName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addCobble)
                .setName(ModConfiguration.addCobbleName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.ChestOptions)
                .setHoverText("Determines if a stack of cobble is added the the chest when the house is created.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addSaplings = builder.comment("Determines if a set of oak saplings are added the the chest when the house is created.")
                .define(ModConfiguration.ChestContentOptions + ModConfiguration.addSaplingsName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addSaplings)
                .setName(ModConfiguration.addSaplingsName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.ChestOptions)
                .setHoverText("Determines if a set of oak saplings are added the the chest when the house is created.")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addTorches = builder.comment("Determines if a set of torches are added the the chest when the house is created.")
                .define(ModConfiguration.ChestContentOptions + ModConfiguration.addTorchesName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addTorches)
                .setName(ModConfiguration.addTorchesName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.ChestOptions)
                .setHoverText("Determines if a set of torches are added the the chest when the house is created.")
                .setDefaultValue(true));

        builder.comment("Starter House Options");

        Prefab.proxy.proxyConfiguration.addBed = builder
                .comment("Determines if the bed is included in the starter house. When playing on a server, the server configuration is used")
                .define(ModConfiguration.starterHouseOptions + ModConfiguration.addBedName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addBed)
                .setName(ModConfiguration.addBedName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.HouseOptions)
                .setHoverText("Determines if the bed is included in the starter house. When playing on a server, the server configuration is used")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addCraftingTable = builder
                .comment("Determines if the crafting table is included in the starter house. When playing on a server, the server configuration is used")
                .define(ModConfiguration.starterHouseOptions + ModConfiguration.addCraftingTableName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addCraftingTable)
                .setName(ModConfiguration.addCraftingTableName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.HouseOptions)
                .setHoverText("Determines if the crafting table is included in the starter house. When playing on a server, the server configuration is used")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addFurnace = builder
                .comment("Determines if the furnace is included in the starter house. When playing on a server, the server configuration is used")
                .define(ModConfiguration.starterHouseOptions + ModConfiguration.addFurnaceName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addFurnace)
                .setName(ModConfiguration.addFurnaceName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.HouseOptions)
                .setHoverText("Determines if the furnace is included in the starter house. When playing on a server, the server configuration is used")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addChests = builder
                .comment("Determines if chests are included in the starter house. When playing on a server, the server configuration is used")
                .define(ModConfiguration.starterHouseOptions + ModConfiguration.addChestsName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addChests)
                .setName(ModConfiguration.addChestsName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.HouseOptions)
                .setHoverText("Determines if chests are included in the starter house. When playing on a server, the server configuration is used")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addChestContents = builder
                .comment("Determines if the chest contents is included in the starter house. When playing on a server, the server configuration is used")
                .define(ModConfiguration.starterHouseOptions + ModConfiguration.addChestContentsName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addChestContents)
                .setName(ModConfiguration.addChestContentsName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.HouseOptions)
                .setHoverText("Determines if the chest contents is included in the starter house. When playing on a server, the server configuration is used")
                .setDefaultValue(true));

        Prefab.proxy.proxyConfiguration.addMineshaft = builder
                .comment("Determines if the mineshaft is included in the starter house. When playing on a server, the server configuration is used")
                .define(ModConfiguration.starterHouseOptions + ModConfiguration.addMineshaftName, true);

        config.configOptions.add(new ConfigOption<Boolean>()
                .setConfigValue(Prefab.proxy.proxyConfiguration.addMineshaft)
                .setName(ModConfiguration.addMineshaftName)
                .setConfigType("Boolean")
                .setCategory(ConfigCategory.HouseOptions)
                .setHoverText("Determines if the mineshaft is included in the starter house. When playing on a server, the server configuration is used")
                .setDefaultValue(true));

        // Recipe configuration.
        for (String key : ModConfiguration.recipeKeys) {
            BooleanValue value = builder
                    .comment("Determines if the recipe(s) associated with the " + key + " are enabled.")
                    .define(ModConfiguration.RecipeOptions + key, true);

            config.configOptions.add(new ConfigOption<Boolean>()
                    .setConfigValue(value)
                    .setName(key)
                    .setCategory(ConfigCategory.RecipeOptions)
                    .setConfigType("Boolean")
                    .setHoverText("Determines if the recipe(s) associated with the " + key + " are enabled.")
                    .setDefaultValue(true));

            Prefab.proxy.proxyConfiguration.recipeConfiguration.put(key, value);
        }

        ModConfiguration.loadStructureOptionConfiguration(config, builder);
    }

    private static void loadStructureOptionConfiguration(ModConfiguration config, ForgeConfigSpec.Builder builder) {
        // Sort the structure names by their name, so they show up well on the configuration screen.
        List<BasicStructureConfiguration.EnumBasicStructureName> values = Arrays.stream(BasicStructureConfiguration.EnumBasicStructureName.values())
                .sorted(Comparator.comparing(BasicStructureConfiguration.EnumBasicStructureName::getName)).toList();

        for (BasicStructureConfiguration.EnumBasicStructureName value : values) {
            if (value.getName().equals("custom")) {
                continue;
            }

            // Sort the options for this structure.
            List<BaseOption> options = value.getBaseOption().getSpecificOptions().stream().sorted(Comparator.comparing(BaseOption::getTranslationString)).toList();

            if (options.size() > 1) {
                String key = value.getItemTranslationString();
                String path = ModConfiguration.StructureOptions + key.replace('.', '_');

                HashMap<String, BooleanValue> structureOptions = new HashMap<>();

                for (BaseOption option : options) {
                    BooleanValue booleanValue = builder
                            .define(path + '_' + option.getTranslationString().replace('.', '_'), true);

                    // Translations are not available at the time this is built, create a separate tranlsatable key for now so it can be tranlsated later.
                    MutableComponent component = Component.literal("Enables or disables the option for ");
                    component.append(Component.translatable(key));

                    config.configOptions.add(new ConfigOption<Boolean>()
                            .setConfigValue(booleanValue)
                            .setName(option.getTranslationString())
                            .setCategory(ConfigCategory.StructureOptions)
                            .setConfigType("Boolean")
                            .setHoverText("Placeholder")
                            .setHoverTextComponent(component)
                            .setDefaultValue(true));

                    structureOptions.put(option.getTranslationString(), booleanValue);
                }

                Prefab.proxy.proxyConfiguration.structureOptions.put(key, structureOptions);
            }
        }

        // Add the basic house settings.
        List<HouseConfiguration.HouseStyle> houseStyles = Arrays.stream(HouseConfiguration.HouseStyle.values())
                .sorted(Comparator.comparing(HouseConfiguration.HouseStyle::getTranslationKey)).toList();
        String mainKey = "item.prefab.item_house";
        String mainPath = ModConfiguration.StructureOptions + mainKey.replace('.', '_');
        HashMap<String, BooleanValue> structureOptions = new HashMap<>();

        for (HouseConfiguration.HouseStyle houseStyle : houseStyles) {
            BooleanValue booleanValue = builder
                    .define(mainPath + '_' + houseStyle.getTranslationKey().replace('.', '_'), true);

            // Translations are not available at the time this is built, create a separate tranlsatable key for now so it can be tranlsated later.
            MutableComponent component = Component.literal("Enables or disables the option for ");
            component.append(Component.translatable(mainKey));

            config.configOptions.add(new ConfigOption<Boolean>()
                    .setConfigValue(booleanValue)
                    .setName(houseStyle.getTranslationKey())
                    .setCategory(ConfigCategory.StructureOptions)
                    .setConfigType("Boolean")
                    .setHoverText("Placeholder")
                    .setHoverTextComponent(component)
                    .setDefaultValue(true));

            structureOptions.put(houseStyle.getTranslationKey(), booleanValue);
        }

        Prefab.proxy.proxyConfiguration.structureOptions.put(mainKey, structureOptions);

        // Improved House options
        List<HouseImprovedConfiguration.HouseStyle> houseImprovedStyles = Arrays.stream(HouseImprovedConfiguration.HouseStyle.values())
                .sorted(Comparator.comparing(HouseImprovedConfiguration.HouseStyle::getTranslationKey)).toList();
        mainKey = "item.prefab.item_house_improved";
        mainPath = ModConfiguration.StructureOptions + mainKey.replace('.', '_');
        structureOptions = new HashMap<>();

        for (HouseImprovedConfiguration.HouseStyle houseStyle : houseImprovedStyles) {
            BooleanValue booleanValue = builder
                    .define(mainPath + '_' + houseStyle.getTranslationKey().replace('.', '_'), true);

            // Translations are not available at the time this is built, create a separate tranlsatable key for now so it can be tranlsated later.
            MutableComponent component = Component.literal("Enables or disables the option for ");
            component.append(Component.translatable(mainKey));

            config.configOptions.add(new ConfigOption<Boolean>()
                    .setConfigValue(booleanValue)
                    .setName(houseStyle.getTranslationKey())
                    .setCategory(ConfigCategory.StructureOptions)
                    .setConfigType("Boolean")
                    .setHoverText("Placeholder")
                    .setHoverTextComponent(component)
                    .setDefaultValue(true));

            structureOptions.put(houseStyle.getTranslationKey(), booleanValue);
        }

        Prefab.proxy.proxyConfiguration.structureOptions.put(mainKey, structureOptions);

        // Advanced House options
        List<HouseAdvancedConfiguration.HouseStyle> houseAdvancedStyles = Arrays.stream(HouseAdvancedConfiguration.HouseStyle.values())
                .sorted(Comparator.comparing(HouseAdvancedConfiguration.HouseStyle::getTranslationKey)).toList();
        mainKey = "item.prefab.item_house_advanced";
        mainPath = ModConfiguration.StructureOptions + mainKey.replace('.', '_');
        structureOptions = new HashMap<>();

        for (HouseAdvancedConfiguration.HouseStyle houseStyle : houseAdvancedStyles) {
            BooleanValue booleanValue = builder
                    .define(mainPath + '_' + houseStyle.getTranslationKey().replace('.', '_'), true);

            // Translations are not available at the time this is built, create a separate tranlsatable key for now so it can be tranlsated later.
            MutableComponent component = Component.literal("Enables or disables the option for ");
            component.append(Component.translatable(mainKey));

            config.configOptions.add(new ConfigOption<Boolean>()
                    .setConfigValue(booleanValue)
                    .setName(houseStyle.getTranslationKey())
                    .setCategory(ConfigCategory.StructureOptions)
                    .setConfigType("Boolean")
                    .setHoverText("Placeholder")
                    .setHoverTextComponent(component)
                    .setDefaultValue(true));

            structureOptions.put(houseStyle.getTranslationKey(), booleanValue);
        }

        Prefab.proxy.proxyConfiguration.structureOptions.put(mainKey, structureOptions);
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        Prefab.LOGGER.debug("Loading config file {}", path);

        ModConfiguration.SPEC = spec;

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        Prefab.LOGGER.info("Built TOML config for {}", path.toString());
        configData.load();
        Prefab.LOGGER.info("Loaded TOML config file {}", path.toString());
        spec.setConfig(configData);

        ModConfiguration.UpdateServerConfig();
    }

    public static void UpdateServerConfig() {
        Prefab.proxy.proxyConfiguration.serverConfiguration.startingItem = Prefab.proxy.proxyConfiguration.startingItem.get();
        Prefab.proxy.proxyConfiguration.serverConfiguration.newPlayersGetStartingItem = Prefab.proxy.proxyConfiguration.newPlayersGetStartingItem.get();
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
        Prefab.proxy.proxyConfiguration.serverConfiguration.allowBulldozerToCreateDrops = Prefab.proxy.proxyConfiguration.allowBulldozerToCreateDrops.get();
        Prefab.proxy.proxyConfiguration.serverConfiguration.allowWaterInNonOverworldDimensions = Prefab.proxy.proxyConfiguration.allowWaterInNonOverworldDimensions.get();
        Prefab.proxy.proxyConfiguration.serverConfiguration.playBuildingSound = Prefab.proxy.proxyConfiguration.playBuildingSound.get();

        // Recipe configuration.
        for (String key : ModConfiguration.recipeKeys) {
            Prefab.LOGGER.debug("Setting recipe configuration for key: " + key);
            Prefab.proxy.proxyConfiguration.serverConfiguration.recipeConfiguration.put(key, Prefab.proxy.proxyConfiguration.recipeConfiguration.get(key).get());
        }

        // Structure options.
        Prefab.proxy.proxyConfiguration.serverConfiguration.structureOptions.clear();

        for (Map.Entry<String, HashMap<String, BooleanValue>> entry : Prefab.proxy.proxyConfiguration.structureOptions.entrySet()) {
            String key = entry.getKey();
            HashMap<String, Boolean> optionValues = new HashMap<>();

            for (Map.Entry<String, BooleanValue> subEntry: entry.getValue().entrySet()) {
                optionValues.put(subEntry.getKey(), subEntry.getValue().get());
            }

            Prefab.proxy.proxyConfiguration.serverConfiguration.structureOptions.put(key, optionValues);
        }
    }
}
