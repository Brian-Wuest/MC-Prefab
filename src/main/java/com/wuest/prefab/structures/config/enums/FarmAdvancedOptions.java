package com.wuest.prefab.structures.config.enums;

public class FarmAdvancedOptions extends BaseOption {

    public static FarmAdvancedOptions AutomatedBambooFarm = new FarmAdvancedOptions(
            "prefab.gui.farm.advanced.bamboo",
            "assets/prefab/structures/automated_bamboo_farm.zip",
            "textures/gui/automated_bamboo_farm.png",
            false,
            true);

    public static FarmAdvancedOptions AutomatedMelonFarm = new FarmAdvancedOptions(
            "prefab.gui.farm.advanced.melon",
            "assets/prefab/structures/automated_melon_farm.zip",
            "textures/gui/automated_melon_farm.png",
            false,
            true);

    public static FarmAdvancedOptions Barn = new FarmAdvancedOptions(
            "prefab.gui.farm.advanced.barn",
            "assets/prefab/structures/barn_advanced.zip",
            "textures/gui/barn_advanced.png",
            false,
            false);

    public static FarmAdvancedOptions GreenHouse = new FarmAdvancedOptions(
            "prefab.gui.farm.advanced.green_house",
            "assets/prefab/structures/green_house_advanced.zip",
            "textures/gui/green_house.png",
            false,
            false);

    public static FarmAdvancedOptions LargeHorseStable = new FarmAdvancedOptions(
            "prefab.gui.farm.advanced.horse",
            "assets/prefab/structures/horse_stable_advanced.zip",
            "textures/gui/horse_stable_advanced.png",
            false,
            false);

    public static FarmAdvancedOptions MonsterMasher = new FarmAdvancedOptions(
            "prefab.gui.farm.advanced.monster",
            "assets/prefab/structures/monster_masher.zip",
            "textures/gui/monster_masher.png",
            false,
            true);

    public static FarmAdvancedOptions ProduceFarm = new FarmAdvancedOptions(
            "prefab.gui.farm.advanced.produce",
            "assets/prefab/structures/produce_farm.zip",
            "textures/gui/produce_farm.png",
            false,
            true);

    public static FarmAdvancedOptions TreeFarm = new FarmAdvancedOptions(
            "prefab.gui.farm.advanced.tree",
            "assets/prefab/structures/tree_farm.zip",
            "textures/gui/tree_farm.png",
            false,
            false);

    protected FarmAdvancedOptions(String translationString,
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
