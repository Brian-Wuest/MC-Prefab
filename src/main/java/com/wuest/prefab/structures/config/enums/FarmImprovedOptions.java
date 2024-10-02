package com.wuest.prefab.structures.config.enums;

@SuppressWarnings({"unchecked", "unused"})
public class FarmImprovedOptions extends BaseOption{
    public static FarmImprovedOptions AutomatedChickenCoop = new FarmImprovedOptions(
            "prefab.gui.farm.improved.chicken",
            "assets/prefab/structures/chicken_coop_improved.gz",
            "textures/gui/chicken_coop_improved.png",
            false,
            false);

    public static FarmImprovedOptions AutomatedFarm = new FarmImprovedOptions(
            "prefab.gui.farm.improved.automated",
            "assets/prefab/structures/automated_farm.gz",
            "textures/gui/automated_farm.png",
            false,
            false);

    public static FarmImprovedOptions FishPond = new FarmImprovedOptions(
            "prefab.gui.farm.improved.fish",
            "assets/prefab/structures/fish_pond.gz",
            "textures/gui/fish_pond.png",
            false,
            false);

    public static FarmImprovedOptions BeeFarm = new FarmImprovedOptions(
            "prefab.gui.farm.improved.bee",
            "assets/prefab/structures/bee_farm.gz",
            "textures/gui/bee_farm.png",
            false,
            false);

    public static FarmImprovedOptions SugarCaneFarm = new FarmImprovedOptions(
            "prefab.gui.farm.improved.sugar_cane",
            "assets/prefab/structures/sugar_cane_farm.gz",
            "textures/gui/sugar_cane_farm.png",
            false,
            true);

    public static FarmImprovedOptions MushroomFarm = new FarmImprovedOptions(
            "prefab.gui.farm.mushroom",
            "assets/prefab/structures/mushroom_farm.gz",
            "textures/gui/mushroom_farm.png",
            false,
            false);

    public static FarmImprovedOptions Barn = new FarmImprovedOptions(
            "prefab.gui.farm.advanced.barn",
            "assets/prefab/structures/barn_improved.gz",
            "textures/gui/barn_improved.png",
            false,
            false);

    public static FarmImprovedOptions GreenHouse = new FarmImprovedOptions(
            "prefab.gui.farm.advanced.green_house",
            "assets/prefab/structures/green_house_improved.gz",
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
}
