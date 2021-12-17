package com.wuest.prefab.config;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * This class is used to sync the server configuration to the client.
 *
 * @author Brian
 */
public class ServerModConfiguration {
    // Configuration Options.
    public boolean enableVersionCheckMessage;
    public boolean enableLoftHouse;
    public boolean includeSpawnersInMasher;
    public boolean enableStructurePreview;
    public boolean includeMineshaftChest;
    public boolean allowBulldozerToCreateDrops;
    public boolean allowWaterInNonOverworldDimensions;
    public boolean enableAutomationOptionsFromModerateFarm;
    public boolean playBuildingSound;

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
    public boolean addMineshaft;

    public String startingItem;

    public HashMap<String, Boolean> recipeConfiguration;

    public ServerModConfiguration() {
        this.recipeConfiguration = new HashMap<>();
    }

    public static ServerModConfiguration getFromNBTTagCompound(CompoundTag tag) {
        ServerModConfiguration configuration = new ServerModConfiguration();

        configuration.startingItem = tag.getString(ModConfiguration.startingItemName);
        configuration.enableVersionCheckMessage = tag.getBoolean(ModConfiguration.enableVersionCheckMessageName);
        configuration.enableLoftHouse = tag.getBoolean(ModConfiguration.enableLoftHouseName);
        configuration.includeSpawnersInMasher = tag.getBoolean(ModConfiguration.includeSpawnersInMasherName);
        configuration.enableStructurePreview = tag.getBoolean(ModConfiguration.enableStructurePreviewName);
        configuration.includeMineshaftChest = tag.getBoolean(ModConfiguration.includeMineshaftChestName);
        configuration.allowBulldozerToCreateDrops = tag.getBoolean(ModConfiguration.allowBulldozerToCreateDropsName);
        configuration.allowWaterInNonOverworldDimensions = tag.getBoolean(ModConfiguration.allowWaterInNonOverworldDimensionsName);
        configuration.enableAutomationOptionsFromModerateFarm = tag.getBoolean(ModConfiguration.enableAutomationOptionsFromModerateFarmName);
        configuration.playBuildingSound = tag.getBoolean(ModConfiguration.playBuildingSoundName);

        configuration.addSword = tag.getBoolean(ModConfiguration.addSwordName);
        configuration.addAxe = tag.getBoolean(ModConfiguration.addAxeName);
        configuration.addShovel = tag.getBoolean(ModConfiguration.addShovelName);
        configuration.addHoe = tag.getBoolean(ModConfiguration.addHoeName);
        configuration.addPickAxe = tag.getBoolean(ModConfiguration.addPickAxeName);
        configuration.addArmor = tag.getBoolean(ModConfiguration.addArmorName);
        configuration.addFood = tag.getBoolean(ModConfiguration.addFoodName);
        configuration.addCrops = tag.getBoolean(ModConfiguration.addCropsName);
        configuration.addDirt = tag.getBoolean(ModConfiguration.addDirtName);
        configuration.addCobble = tag.getBoolean(ModConfiguration.addCobbleName);
        configuration.addSaplings = tag.getBoolean(ModConfiguration.addSaplingsName);
        configuration.addTorches = tag.getBoolean(ModConfiguration.addTorchesName);

        configuration.addBed = tag.getBoolean(ModConfiguration.addBedName);
        configuration.addCraftingTable = tag.getBoolean(ModConfiguration.addCraftingTableName);
        configuration.addFurnace = tag.getBoolean(ModConfiguration.addFurnaceName);
        configuration.addChests = tag.getBoolean(ModConfiguration.addChestsName);
        configuration.addChestContents = tag.getBoolean(ModConfiguration.addChestContentsName);
        configuration.addMineshaft = tag.getBoolean(ModConfiguration.addMineshaftName);

        for (String key : ModConfiguration.recipeKeys) {
            configuration.recipeConfiguration.put(key, tag.getBoolean(key));
        }

        return configuration;
    }

    public CompoundTag ToNBTTagCompound() {
        CompoundTag tag = new CompoundTag();

        tag.putString(ModConfiguration.startingItemName, this.startingItem);
        tag.putBoolean(ModConfiguration.enableVersionCheckMessageName, this.enableVersionCheckMessage);
        tag.putBoolean(ModConfiguration.enableLoftHouseName, this.enableLoftHouse);
        tag.putBoolean(ModConfiguration.includeSpawnersInMasherName, this.includeSpawnersInMasher);
        tag.putBoolean(ModConfiguration.enableStructurePreviewName, this.enableStructurePreview);
        tag.putBoolean(ModConfiguration.includeMineshaftChestName, this.includeMineshaftChest);
        tag.putBoolean(ModConfiguration.allowBulldozerToCreateDropsName, this.allowBulldozerToCreateDrops);
        tag.putBoolean(ModConfiguration.allowWaterInNonOverworldDimensionsName, this.allowWaterInNonOverworldDimensions);
        tag.putBoolean(ModConfiguration.enableAutomationOptionsFromModerateFarmName, this.enableAutomationOptionsFromModerateFarm);
        tag.putBoolean(ModConfiguration.playBuildingSoundName, this.playBuildingSound);

        tag.putBoolean(ModConfiguration.addSwordName, this.addSword);
        tag.putBoolean(ModConfiguration.addAxeName, this.addAxe);
        tag.putBoolean(ModConfiguration.addShovelName, this.addShovel);
        tag.putBoolean(ModConfiguration.addHoeName, this.addHoe);
        tag.putBoolean(ModConfiguration.addPickAxeName, this.addPickAxe);
        tag.putBoolean(ModConfiguration.addArmorName, this.addArmor);
        tag.putBoolean(ModConfiguration.addFoodName, this.addFood);
        tag.putBoolean(ModConfiguration.addCropsName, this.addCrops);
        tag.putBoolean(ModConfiguration.addDirtName, this.addDirt);
        tag.putBoolean(ModConfiguration.addCobbleName, this.addCobble);
        tag.putBoolean(ModConfiguration.addSaplingsName, this.addSaplings);
        tag.putBoolean(ModConfiguration.addTorchesName, this.addTorches);

        tag.putBoolean(ModConfiguration.addBedName, this.addBed);
        tag.putBoolean(ModConfiguration.addCraftingTableName, this.addCraftingTable);
        tag.putBoolean(ModConfiguration.addFurnaceName, this.addFurnace);
        tag.putBoolean(ModConfiguration.addChestsName, this.addChests);
        tag.putBoolean(ModConfiguration.addChestContentsName, this.addChestContents);
        tag.putBoolean(ModConfiguration.addMineshaftName, this.addMineshaft);

        for (Entry<String, Boolean> entry : this.recipeConfiguration.entrySet()) {
            tag.putBoolean(entry.getKey(), entry.getValue());
        }

        return tag;
    }
}
