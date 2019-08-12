package com.wuest.prefab.Config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Prefab;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
public class ModConfiguration {
    public static String OPTIONS = "general.";
    public static String ChestContentOptions = "general.chest contents.";
    public static String RecipeOptions = "general.recipes.";
    public static String starterHouseOptions = "general.starter house.";
    public static String tagKey = "PrefabConfig";
    public static ArrayList<String> validStartingItems = new ArrayList<String>(Arrays.asList("Starting House", "Moderate House", "Structure Part", "Nothing"));
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
            {compressedStoneKey, compressedGlowStoneKey, compressedDirteKey, compressedChestKey, pileOfBricksKey, warehouseKey, produceFarmKey, treeFarmKey, chickenCoopKey, fishFarmKey,
                    warehouseUpgradeKey, advancedWarehouseKey, monsterMasherKey, bundleofTimberKey, horseStableKey, netherGateKey, advancedChickenCoopKey, advancedHorseStableKey, barnKey,
                    machineryTowerKey, defenseBunkerKey, mineshaftEntranceKey, enderGatewayKey, magicTempleKey, instantBridgeKey, paperLanternKey, compressedObsidianKey, villagerHousesKey,
                    phasicBlockKey, smartGlassKey, greenHouseKey, startingHouseKey, glassStairsKey, glassSlabsKey, andesiteStairsKey, andesiteSlabsKey, dioriteStairsKey, dioriteSlabKey,
                    graniteStairsKey, grantiteSlabKey, moderateHouseKey, grassyPlainsKey, aquaBaseKey, watchTowerKey, bulldozerKey, structurePartKey, jailKey, saloonKey};
    public static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
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
    static String versionMessageName = "Version Message";
    static String showMessageName = "Show Message";
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
    public BooleanValue addMineshaft;
    public ConfigValue<String> startingItem;
    public HashMap<String, BooleanValue> recipeConfiguration;
    public ServerModConfiguration serverConfiguration;
    // Version Check Message Info
    public String versionMessage = "";
    public boolean showMessage = false;

    public ModConfiguration(ForgeConfigSpec.Builder builder) {
        this.recipeConfiguration = new HashMap<String, BooleanValue>();
        this.serverConfiguration = new ServerModConfiguration();
        ModConfiguration.BUILDER = builder;
        ModConfiguration.buildOptions(this, builder);
    }

    public static void buildOptions(ModConfiguration config, ForgeConfigSpec.Builder builder) {
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

    public static void UpdateServerConfig() {
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

    public enum CeilingFloorBlockType {
        StoneBrick(0, GuiLangKeys.CEILING_BLOCK_TYPE_STONE), Brick(1, GuiLangKeys.CEILING_BLOCK_TYPE_BRICK), SandStone(2, GuiLangKeys.CEILING_BLOCK_TYPE_SAND);

        private final int value;
        private final String langKey;

        CeilingFloorBlockType(int newValue, String langKey) {
            this.value = newValue;
            this.langKey = langKey;
        }

        public static CeilingFloorBlockType ValueOf(int value) {
            switch (value) {
                case 1: {
                    return CeilingFloorBlockType.Brick;
                }

                case 2: {
                    return CeilingFloorBlockType.SandStone;
                }

                default: {
                    return CeilingFloorBlockType.StoneBrick;
                }
            }
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return GuiLangKeys.translateString(this.langKey);
        }
    }

    public enum WallBlockType {
        Oak(0, GuiLangKeys.WALL_BLOCK_TYPE_OAK),
        Spruce(1, GuiLangKeys.WALL_BLOCK_TYPE_SPRUCE),
        Birch(2, GuiLangKeys.WALL_BLOCK_TYPE_BIRCH),
        Jungle(3,
                GuiLangKeys.WALL_BLOCK_TYPE_JUNGLE),
        Acacia(4, GuiLangKeys.WALL_BLOCK_TYPE_ACACIA),
        DarkOak(5, GuiLangKeys.WALL_BLOCK_TYPE_DARK_OAK);

        private final int value;
        private final String langKey;

        WallBlockType(final int newValue, String langKey) {
            value = newValue;
            this.langKey = langKey;
        }

        public static WallBlockType ValueOf(int value) {
            switch (value) {
                case 1: {
                    return WallBlockType.Spruce;
                }

                case 2: {
                    return WallBlockType.Birch;
                }

                case 3: {
                    return WallBlockType.Jungle;
                }

                case 4: {
                    return WallBlockType.Acacia;
                }

                case 5: {
                    return WallBlockType.DarkOak;
                }

                default: {
                    return WallBlockType.Oak;
                }
            }
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return GuiLangKeys.translateString(this.langKey);
        }

        public BlockState getBlockState() {
            switch (this.value) {
                case 1: {
                    return Blocks.SPRUCE_PLANKS.getDefaultState();
                }

                case 2: {
                    return Blocks.BIRCH_PLANKS.getDefaultState();
                }

                case 3: {
                    return Blocks.JUNGLE_PLANKS.getDefaultState();
                }

                case 4: {
                    return Blocks.ACACIA_PLANKS.getDefaultState();
                }

                case 5: {
                    return Blocks.DARK_OAK_PLANKS.getDefaultState();
                }

                default: {
                    return Blocks.OAK_PLANKS.getDefaultState();
                }
            }
        }
    }

}
