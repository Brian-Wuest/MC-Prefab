package com.prefab.config;

import com.prefab.structures.config.BasicStructureConfiguration;
import com.prefab.structures.config.HouseAdvancedConfiguration;
import com.prefab.structures.config.HouseConfiguration;
import com.prefab.structures.config.HouseImprovedConfiguration;
import com.prefab.structures.config.enums.BaseOption;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Config(name = "Prefab")
public class ModConfiguration implements ConfigData {

    @Comment("Item provided to new players when joining a world")
    public StartingItemOptions startingItem = StartingItemOptions.StartingHouse;

    @Comment("Can new players for a world receive a starting item?")
    public boolean newPlayersGetStartingItem = true;

    @Comment("Add spawners to monster masher")
    public boolean includeSpawnersInMasher = true;

    @Comment("Enables preview buttons in screens")
    public boolean enableStructurePreview = true;

    @Comment("Bulldozer makes drops")
    public boolean allowBulldozerToCreateDrops = true;

    @Comment("Allow water in the End or Custom Dimensions")
    public boolean allowWaterInNonOverworldDimensions = true;

    @Comment("Generated mineshafts have chests with materials")
    public boolean includeMineshaftChest = true;

    @Comment("Play building sound when generating blueprint")
    public boolean playBuildingSound = true;

    @ConfigEntry.Category("recipes")
    public HashMap<String, Boolean> recipes = new HashMap<>();

    @ConfigEntry.Category("chest_options")
    @ConfigEntry.Gui.TransitiveObject()
    public ChestOptions chestOptions = new ChestOptions();

    @ConfigEntry.Category("starter_options")
    @ConfigEntry.Gui.TransitiveObject()
    public StarterHouseOptions starterHouseOptions = new StarterHouseOptions();

    @ConfigEntry.Category("structure_options")
    public HashMap<String, HashMap<String, Boolean>> structureOptions = new HashMap<>();

    @ConfigEntry.Category("strict_mode_options")
    @ConfigEntry.Gui.TransitiveObject()
    public StrictModeOptions strictModeOptions = new StrictModeOptions();

    public ModConfiguration() {
        this.initialize();
    }

    @Override
    public void validatePostLoad() {
        this.initialize();
    }

    protected void initialize() {
        for (String key : ConfigKeyNames.Keys) {
            if (!this.recipes.containsKey(key)) {
                this.recipes.put(key, true);
            }
        }

        // Add the Basic structure settings.
        for (BasicStructureConfiguration.EnumBasicStructureName value : BasicStructureConfiguration.EnumBasicStructureName.values()) {
            if (value.getName().equals("custom")) {
                continue;
            }

            String key = value.getItemTranslationString();
            ArrayList<BaseOption> options = value.getBaseOption().getSpecificOptions();

            if (options.size() > 1) {
                HashMap<String, Boolean> structureOptions;

                if (this.structureOptions.containsKey(key)) {
                    structureOptions = this.structureOptions.get(key);
                } else {
                    structureOptions = new HashMap<>();
                }

                for (BaseOption option : options) {
                    if (!structureOptions.containsKey(option.getTranslationString())) {
                        structureOptions.put(option.getTranslationString(), true);
                    }
                }

                if (!this.structureOptions.containsKey(key)) {
                    this.structureOptions.put(key, structureOptions);
                }
            }
        }

        // Add the basic house settings.
        String structureOptionsKey = "item.prefab.item_house";
        HashMap<String, Boolean> houseOptions;

        if (this.structureOptions.containsKey(structureOptionsKey)) {
            houseOptions = this.structureOptions.get(structureOptionsKey);
        } else {
            houseOptions = new HashMap<>();
            this.structureOptions.put(structureOptionsKey, houseOptions);
        }

        for (HouseConfiguration.HouseStyle houseStyle : HouseConfiguration.HouseStyle.values()) {
            if (!houseOptions.containsKey(houseStyle.getTranslationString())) {
                houseOptions.put(houseStyle.getTranslationString(), true);
            }
        }

        // Add the Improved house settings.
        structureOptionsKey = "item.prefab.item_house_improved";
        HashMap<String, Boolean> houseImprovedOptions;

        if (this.structureOptions.containsKey(structureOptionsKey)) {
            houseImprovedOptions = this.structureOptions.get(structureOptionsKey);
        } else {
            houseImprovedOptions = new HashMap<>();
            this.structureOptions.put(structureOptionsKey, houseImprovedOptions);
        }

        for (HouseImprovedConfiguration.HouseStyle houseStyle : HouseImprovedConfiguration.HouseStyle.values()) {
            if (!houseImprovedOptions.containsKey(houseStyle.getTranslationString())) {
                houseImprovedOptions.put(houseStyle.getTranslationString(), true);
            }
        }

        // Add the Advanced house settings.
        structureOptionsKey = "item.prefab.item_house_advanced";
        HashMap<String, Boolean> houseAdvancedOptions;

        if (this.structureOptions.containsKey(structureOptionsKey)) {
            houseAdvancedOptions = this.structureOptions.get(structureOptionsKey);
        } else {
            houseAdvancedOptions = new HashMap<>();
            this.structureOptions.put(structureOptionsKey, houseAdvancedOptions);
        }

        for (HouseAdvancedConfiguration.HouseStyle houseStyle : HouseAdvancedConfiguration.HouseStyle.values()) {
            if (!houseAdvancedOptions.containsKey(houseStyle.getTranslationString())) {
                houseAdvancedOptions.put(houseStyle.getTranslationString(), true);
            }
        }
    }

    public CompoundTag writeCompoundTag() {
        CompoundTag tag = new CompoundTag();

        tag.putString(ConfigKeyNames.startingItemName, this.startingItem.toString());
        tag.putBoolean(ConfigKeyNames.newPlayersGetStartingItemName, this.newPlayersGetStartingItem);
        tag.putBoolean(ConfigKeyNames.includeSpawnersInMasherName, this.includeSpawnersInMasher);
        tag.putBoolean(ConfigKeyNames.enableStructurePreviewName, this.enableStructurePreview);
        tag.putBoolean(ConfigKeyNames.includeMineshaftChestName, this.includeMineshaftChest);
        tag.putBoolean(ConfigKeyNames.allowBulldozerToCreateDropsName, this.allowBulldozerToCreateDrops);
        tag.putBoolean(ConfigKeyNames.allowWaterInNonOverworldDimensionsName, this.allowWaterInNonOverworldDimensions);
        tag.putBoolean(ConfigKeyNames.playBuildingSoundName, this.playBuildingSound);

        tag.putBoolean(ConfigKeyNames.addSwordName, this.chestOptions.addSword);
        tag.putBoolean(ConfigKeyNames.addAxeName, this.chestOptions.addAxe);
        tag.putBoolean(ConfigKeyNames.addShovelName, this.chestOptions.addShovel);
        tag.putBoolean(ConfigKeyNames.addHoeName, this.chestOptions.addHoe);
        tag.putBoolean(ConfigKeyNames.addPickAxeName, this.chestOptions.addPickAxe);
        tag.putBoolean(ConfigKeyNames.addArmorName, this.chestOptions.addArmor);
        tag.putBoolean(ConfigKeyNames.addFoodName, this.chestOptions.addFood);
        tag.putBoolean(ConfigKeyNames.addCropsName, this.chestOptions.addCrops);
        tag.putBoolean(ConfigKeyNames.addDirtName, this.chestOptions.addDirt);
        tag.putBoolean(ConfigKeyNames.addCobbleName, this.chestOptions.addCobble);
        tag.putBoolean(ConfigKeyNames.addSaplingsName, this.chestOptions.addSaplings);
        tag.putBoolean(ConfigKeyNames.addTorchesName, this.chestOptions.addTorches);

        tag.putBoolean(ConfigKeyNames.addBedName, this.starterHouseOptions.addBed);
        tag.putBoolean(ConfigKeyNames.addCraftingTableName, this.starterHouseOptions.addCraftingTable);
        tag.putBoolean(ConfigKeyNames.addFurnaceName, this.starterHouseOptions.addFurnace);
        tag.putBoolean(ConfigKeyNames.addChestsName, this.starterHouseOptions.addChests);
        tag.putBoolean(ConfigKeyNames.addChestContentsName, this.starterHouseOptions.addChestContents);
        tag.putBoolean(ConfigKeyNames.addMineshaftName, this.starterHouseOptions.addMineshaft);

        for (Map.Entry<String, Boolean> entry : this.recipes.entrySet()) {
            tag.putBoolean(entry.getKey(), entry.getValue());
        }

        CompoundTag structureOptionTag = getStructureOptionCompoundTag();

        tag.put(ConfigKeyNames.structureOptionsName, structureOptionTag);

        CompoundTag overwritableBlocksTag = getOverwritableBlocksTag();
        tag.put(ConfigKeyNames.overwritableBlocksName, overwritableBlocksTag);

        CompoundTag overwritableTagsTag = getOverwritableTagsTag();
        tag.put(ConfigKeyNames.overwritableBlocksName, overwritableTagsTag);

        return tag;
    }

    private @NotNull CompoundTag getStructureOptionCompoundTag() {
        CompoundTag structureOptionTag = new CompoundTag();

        for (Map.Entry<String, HashMap<String, Boolean>> entry : this.structureOptions.entrySet()) {
            // Create compound tag for this item.
            CompoundTag mainItem = new CompoundTag();

            for (Map.Entry<String, Boolean> subEntry : entry.getValue().entrySet()) {
                mainItem.putBoolean(subEntry.getKey(), subEntry.getValue());
            }

            structureOptionTag.put(entry.getKey(), mainItem);
        }

        return structureOptionTag;
    }

    private @NotNull CompoundTag getOverwritableBlocksTag() {
        return this.convertStringCollection(this.strictModeOptions.overwritableBlocks);
    }

    private @NotNull CompoundTag getOverwritableTagsTag() {
        return this.convertStringCollection(this.strictModeOptions.overwritableTags);
    }

    private @NotNull CompoundTag convertStringCollection(ArrayList<String> collection) {
        CompoundTag tag = new CompoundTag();

        if (collection != null && !collection.isEmpty()) {
            for (String value : collection) {
                // The key of the tag will be the value, so we have to make sure we don't have any duplicates.
                if (!StringUtils.isBlank(value) && !tag.contains(value)) {
                    tag.putString(value, value);
                }
            }
        }

        return tag;
    }

    public void readFromTag(CompoundTag tag) {
        this.startingItem = StartingItemOptions.getByName(tag.getString(ConfigKeyNames.startingItemName));

        this.newPlayersGetStartingItem = tag.getBoolean(ConfigKeyNames.newPlayersGetStartingItemName);
        this.includeSpawnersInMasher = tag.getBoolean(ConfigKeyNames.includeSpawnersInMasherName);
        this.enableStructurePreview = tag.getBoolean(ConfigKeyNames.enableStructurePreviewName);
        this.includeMineshaftChest = tag.getBoolean(ConfigKeyNames.includeMineshaftChestName);
        this.allowBulldozerToCreateDrops = tag.getBoolean(ConfigKeyNames.allowBulldozerToCreateDropsName);
        this.allowWaterInNonOverworldDimensions = tag.getBoolean(ConfigKeyNames.allowWaterInNonOverworldDimensionsName);
        this.playBuildingSound = tag.getBoolean(ConfigKeyNames.playBuildingSoundName);

        this.chestOptions.addSword = tag.getBoolean(ConfigKeyNames.addSwordName);
        this.chestOptions.addAxe = tag.getBoolean(ConfigKeyNames.addAxeName);
        this.chestOptions.addShovel = tag.getBoolean(ConfigKeyNames.addShovelName);
        this.chestOptions.addHoe = tag.getBoolean(ConfigKeyNames.addHoeName);
        this.chestOptions.addPickAxe = tag.getBoolean(ConfigKeyNames.addPickAxeName);
        this.chestOptions.addArmor = tag.getBoolean(ConfigKeyNames.addArmorName);
        this.chestOptions.addFood = tag.getBoolean(ConfigKeyNames.addFoodName);
        this.chestOptions.addCrops = tag.getBoolean(ConfigKeyNames.addCropsName);
        this.chestOptions.addDirt = tag.getBoolean(ConfigKeyNames.addDirtName);
        this.chestOptions.addCobble = tag.getBoolean(ConfigKeyNames.addCobbleName);
        this.chestOptions.addSaplings = tag.getBoolean(ConfigKeyNames.addSaplingsName);
        this.chestOptions.addTorches = tag.getBoolean(ConfigKeyNames.addTorchesName);

        this.starterHouseOptions.addBed = tag.getBoolean(ConfigKeyNames.addBedName);
        this.starterHouseOptions.addCraftingTable = tag.getBoolean(ConfigKeyNames.addCraftingTableName);
        this.starterHouseOptions.addFurnace = tag.getBoolean(ConfigKeyNames.addFurnaceName);
        this.starterHouseOptions.addChests = tag.getBoolean(ConfigKeyNames.addChestsName);
        this.starterHouseOptions.addChestContents = tag.getBoolean(ConfigKeyNames.addChestContentsName);
        this.starterHouseOptions.addMineshaft = tag.getBoolean(ConfigKeyNames.addMineshaftName);

        this.recipes.clear();
        this.structureOptions.clear();

        for (String key : ConfigKeyNames.Keys) {
            this.recipes.put(key, tag.getBoolean(key));
        }

        CompoundTag structureOptionsTag = tag.getCompound(ConfigKeyNames.structureOptionsName);

        if (!structureOptionsTag.isEmpty()) {
            for (String key : structureOptionsTag.getAllKeys()) {
                CompoundTag mainItem = structureOptionsTag.getCompound(key);
                HashMap<String, Boolean> structureOptions = new HashMap<>();

                for (String subKey : mainItem.getAllKeys()) {
                    boolean value = mainItem.getBoolean(subKey);

                    structureOptions.put(subKey, value);
                }

                this.structureOptions.put(key, structureOptions);
            }
        }
    }

    public enum StartingItemOptions {
        StartingHouse("Starting House"),
        ModerateHouse("Moderate House"),
        Nothing("Nothing");

        private final String name;

        StartingItemOptions(String name) {
            this.name = name;
        }

        public static StartingItemOptions getByName(String name) {
            StartingItemOptions returnValue = StartingItemOptions.Nothing;

            for (StartingItemOptions options : StartingItemOptions.values()) {
                if (options.name.equals(name)
                        || options.name().equals(name)) {
                    returnValue = options;
                    break;
                }
            }

            return returnValue;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static class ConfigKeyNames {
        public static String compressedStoneKey = "Compressed Stone";
        public static String compressedGlowStoneKey = "Compressed Glowstone";
        public static String compressedDirtKey = "Compressed Dirt";
        public static String compressedChestKey = "Compressed Chest";
        public static String pileOfBricksKey = "Pile of Bricks";
        public static String warehouseKey = "Warehouse";
        public static String warehouseUpgradeKey = "Upgrade";
        public static String advancedWarehouseKey = "Improved Warehouse";
        public static String bundleOfTimberKey = "Bundle of Timber";
        public static String netherGateKey = "Nether Gate";
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
        public static String smartGlassKey = "Boundary Block";
        public static String greenHouseKey = "Green House";
        public static String startingHouseKey = "House";
        public static String glassStairsKey = "Glass Stairs";
        public static String glassSlabsKey = "Glass Slabs";
        public static String moderateHouseKey = "Improved House";
        public static String watchTowerKey = "Watch Tower";
        public static String bulldozerKey = "Bulldozer";
        public static String jailKey = "Jail";
        public static String saloonKey = "Saloon";
        public static String skiLodgeKey = "Ski Lodge";
        public static String windMillKey = "Windmill";
        public static String townHallKey = "Town Hall";
        public static String heapOfTimberKey = "Heap of Timber";
        public static String tonOfTimberKey = "Ton of Timber";
        public static String workshopKey = "Workshop";
        public static String modernBuildingsKey = "Modern Buildings";
        public static String starterFarmKey = "Farm";
        public static String moderateFarmKey = "Improved Farm";
        public static String advancedFarmKey = "Advanced Farm";
        public static String swiftBladeKey = "Swift Blade";
        public static String sickleKey = "Sickle";
        public static String dirtRecipesKey = "Dirt Recipes";
        public static String bunchOfBeetsKey = "Bunch Of Beets";
        public static String bunchOfCarrotsKey = "Bunch Of Carrots";
        public static String bunchOfPotatoesKey = "Bunch Of Potatoes";
        public static String woodenCrateKey = "WoodenCrate";
        public static String quartzCreteKey = "Quartz-Crete";
        public static String houseAdvancedKey = "Advanced House";

        public static String darkLightLampKey = "Dark Light Lamp";

        public static String lightSwitchKey = "Light Switch";

        public static String[] Keys = new String[]
                {ConfigKeyNames.compressedStoneKey, ConfigKeyNames.compressedGlowStoneKey, ConfigKeyNames.compressedDirtKey, ConfigKeyNames.compressedChestKey, ConfigKeyNames.pileOfBricksKey,
                        ConfigKeyNames.warehouseKey,
                        ConfigKeyNames.warehouseUpgradeKey, ConfigKeyNames.advancedWarehouseKey, ConfigKeyNames.bundleOfTimberKey,
                        ConfigKeyNames.netherGateKey,
                        ConfigKeyNames.machineryTowerKey, ConfigKeyNames.defenseBunkerKey, ConfigKeyNames.mineshaftEntranceKey, ConfigKeyNames.enderGatewayKey, ConfigKeyNames.magicTempleKey,
                        ConfigKeyNames.instantBridgeKey, ConfigKeyNames.paperLanternKey, ConfigKeyNames.compressedObsidianKey, ConfigKeyNames.villagerHousesKey,
                        ConfigKeyNames.phasicBlockKey, ConfigKeyNames.smartGlassKey, ConfigKeyNames.greenHouseKey, ConfigKeyNames.startingHouseKey, ConfigKeyNames.glassStairsKey,
                        ConfigKeyNames.glassSlabsKey, ConfigKeyNames.moderateHouseKey, ConfigKeyNames.grassyPlainsKey, ConfigKeyNames.aquaBaseKey, ConfigKeyNames.watchTowerKey,
                        ConfigKeyNames.bulldozerKey, ConfigKeyNames.jailKey, ConfigKeyNames.saloonKey, ConfigKeyNames.skiLodgeKey, ConfigKeyNames.windMillKey,
                        ConfigKeyNames.townHallKey, ConfigKeyNames.heapOfTimberKey, ConfigKeyNames.tonOfTimberKey, ConfigKeyNames.workshopKey, ConfigKeyNames.modernBuildingsKey,
                        ConfigKeyNames.swiftBladeKey, ConfigKeyNames.sickleKey, ConfigKeyNames.dirtRecipesKey, ConfigKeyNames.bunchOfBeetsKey, ConfigKeyNames.bunchOfCarrotsKey, ConfigKeyNames.bunchOfPotatoesKey,
                        ConfigKeyNames.woodenCrateKey, ConfigKeyNames.starterFarmKey, ConfigKeyNames.moderateFarmKey, ConfigKeyNames.advancedFarmKey, ConfigKeyNames.quartzCreteKey, ConfigKeyNames.houseAdvancedKey,
                        ConfigKeyNames.darkLightLampKey, ConfigKeyNames.lightSwitchKey};

        // Config file option names.
        static String includeSpawnersInMasherName = "Include Spawners in Monster Masher";
        static String enableStructurePreviewName = "Include Structure Previews";
        static String includeMineshaftChestName = "Include Mineshaft Chest";
        static String enableAutomationOptionsFromModerateFarmName = "Enable Automation Options For Moderate Farm";
        static String allowBulldozerToCreateDropsName = "Bulldozer Creates Drops";
        static String allowWaterInNonOverworldDimensionsName = "Include Water In Overworld Dimension Only";
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

        // Structure option names.
        static String structureOptionsName = "Structure Options";

        // Strict Building Option Names
        static String strictBuildingModeEnabledName = "Strict Building Mode Enabled";
        static String overwritableBlocksName = "Overwritable Blocks";
        static String overwritableTagsName = "Overwritable Tags";
    }
}
