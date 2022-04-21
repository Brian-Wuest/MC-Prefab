package com.wuest.prefab.structures.config.enums;

import com.wuest.prefab.Prefab;

import java.util.ArrayList;

@SuppressWarnings({"unchecked", "unused"})
public class ModerateFarmOptions extends BaseOption{
    public static ModerateFarmOptions AutomatedChickenCoop = new ModerateFarmOptions(
            "prefab.gui.moderate.farm.chicken",
            "assets/prefab/structures/advancedcoop.zip",
            "textures/gui/advanced_chicken_coop_topdown.png",
            false,
            false);

    public static ModerateFarmOptions AutomatedFarm = new ModerateFarmOptions(
            "prefab.gui.moderate.farm.automated",
            "assets/prefab/structures/automated_farm.zip",
            "textures/gui/automated_farm_topdown.png",
            false,
            false);

    public static ModerateFarmOptions FishPond = new ModerateFarmOptions(
            "prefab.gui.moderate.farm.fish",
            "assets/prefab/structures/fishpond.zip",
            "textures/gui/fish_pond_top_down.png",
            false,
            false);

    public static ModerateFarmOptions BeeFarm = new ModerateFarmOptions(
            "prefab.gui.moderate.farm.bee",
            "assets/prefab/structures/bee_farm.zip",
            "textures/gui/bee_farm_topdown.png",
            false,
            false);

    public static ModerateFarmOptions SugarCaneFarm = new ModerateFarmOptions(
            "prefab.gui.moderate.farm.sugar_cane",
            "assets/prefab/structures/sugar_cane_farm.zip",
            "textures/gui/sugar_cane_farm_topdown.png",
            false,
            true);

    public static ModerateFarmOptions MushroomFarm = new ModerateFarmOptions(
            "prefab.gui.starter.farm.mushroom",
            "assets/prefab/structures/mushroom_farm.zip",
            "textures/gui/mushroom_farm_topdown.png",
            false,
            false);

    public static ModerateFarmOptions Barn = new ModerateFarmOptions(
            "prefab.gui.advanced.farm.barn",
            "assets/prefab/structures/moderate_barn.zip",
            "textures/gui/barn_alternate.png",
            false,
            false);

    protected ModerateFarmOptions(String translationString,
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
            modifiedList.remove(ModerateFarmOptions.SugarCaneFarm);
            modifiedList.remove(ModerateFarmOptions.AutomatedFarm);
            modifiedList.remove(ModerateFarmOptions.AutomatedChickenCoop);

            return modifiedList;
        }

        return originalOptions;
    }
}
