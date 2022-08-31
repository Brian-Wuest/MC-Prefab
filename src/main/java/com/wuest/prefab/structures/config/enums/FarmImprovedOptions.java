package com.wuest.prefab.structures.config.enums;

import com.wuest.prefab.Prefab;

import java.util.ArrayList;

@SuppressWarnings({"unchecked", "unused"})
public class FarmImprovedOptions extends BaseOption{
    public static FarmImprovedOptions AutomatedChickenCoop = new FarmImprovedOptions(
            "prefab.gui.farm.improved.chicken",
            "assets/prefab/structures/chicken_coop_improved.zip",
            "textures/gui/chicken_coop_improved.png",
            false,
            false);

    public static FarmImprovedOptions AutomatedFarm = new FarmImprovedOptions(
            "prefab.gui.farm.improved.automated",
            "assets/prefab/structures/automated_farm.zip",
            "textures/gui/automated_farm.png",
            false,
            false);

    public static FarmImprovedOptions FishPond = new FarmImprovedOptions(
            "prefab.gui.farm.improved.fish",
            "assets/prefab/structures/fish_pond.zip",
            "textures/gui/fish_pond.png",
            false,
            false);

    public static FarmImprovedOptions BeeFarm = new FarmImprovedOptions(
            "prefab.gui.farm.improved.bee",
            "assets/prefab/structures/bee_farm.zip",
            "textures/gui/bee_farm.png",
            false,
            false);

    public static FarmImprovedOptions SugarCaneFarm = new FarmImprovedOptions(
            "prefab.gui.farm.improved.sugar_cane",
            "assets/prefab/structures/sugar_cane_farm.zip",
            "textures/gui/sugar_cane_farm.png",
            false,
            true);

    public static FarmImprovedOptions MushroomFarm = new FarmImprovedOptions(
            "prefab.gui.farm.improved.mushroom",
            "assets/prefab/structures/mushroom_farm.zip",
            "textures/gui/mushroom_farm.png",
            false,
            false);

    public static FarmImprovedOptions Barn = new FarmImprovedOptions(
            "prefab.gui.farm.advanced.barn",
            "assets/prefab/structures/barn_improved.zip",
            "textures/gui/barn_improved.png",
            false,
            false);

    public static FarmImprovedOptions GreenHouse = new FarmImprovedOptions(
            "prefab.gui.farm.advanced.green_house",
            "assets/prefab/structures/green_house_improved.zip",
            "textures/gui/green_house.png",
            false,
            false);

    protected FarmImprovedOptions(String translationString,
                                  String assetLocation,
                                  String pictureLocation,
                                  boolean hasBedColor,
                                  boolean hasGlassColor) {
        super(
                translationString,
                assetLocation,
                pictureLocation,
                hasBedColor,
                hasGlassColor);
    }

    /**
     * Filters the supplied options to remove any options not available based on configuration.
     *
     * @param originalOptions The original options to filter.
     * @return A modified array list which potentially has some items removed.
     */
    @Override
    public ArrayList<BaseOption> filterOptions(ArrayList<BaseOption> originalOptions) {
        if (!Prefab.proxy.getServerConfiguration().enableAutomationOptionsFromModerateFarm) {
            ArrayList<BaseOption> modifiedList = (ArrayList<BaseOption>)originalOptions.clone();
            modifiedList.remove(FarmImprovedOptions.SugarCaneFarm);
            modifiedList.remove(FarmImprovedOptions.AutomatedFarm);
            modifiedList.remove(FarmImprovedOptions.AutomatedChickenCoop);

            return modifiedList;
        }

        return originalOptions;
    }
}
