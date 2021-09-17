package com.wuest.prefab.structures.config.enums;

public class AdvancedFarmOptions extends BaseOption {

    public static AdvancedFarmOptions AutomatedBambooFarm = new AdvancedFarmOptions(
            "prefab.gui.advanced.farm.bamboo",
            "assets/prefab/structures/automated_bamboo_farm.zip",
            "textures/gui/automated_bamboo_farm_topdown.png",
            false,
            true);

    public static AdvancedFarmOptions AutomatedBeeFarm = new AdvancedFarmOptions(
            "prefab.gui.advanced.farm.bee",
            "assets/prefab/structures/automated_bee_farm.zip",
            "textures/gui/automated_bee_farm_topdown.png",
            false,
            true);

    public static AdvancedFarmOptions AutomatedMelonFarm = new AdvancedFarmOptions(
            "prefab.gui.advanced.farm.melon",
            "assets/prefab/structures/automated_melon_farm.zip",
            "textures/gui/automated_melon_farm_topdown.png",
            false,
            true);

    public static AdvancedFarmOptions Barn = new AdvancedFarmOptions(
            "prefab.gui.advanced.farm.barn",
            "assets/prefab/structures/barn.zip",
            "textures/gui/barn_topdown.png",
            false,
            false);

    public static AdvancedFarmOptions GreenHouse = new AdvancedFarmOptions(
            "prefab.gui.advanced.farm.green_house",
            "assets/prefab/structures/green_house.zip",
            "textures/gui/green_house_topdown.png",
            false,
            false);

    public static AdvancedFarmOptions LargeHorseStable = new AdvancedFarmOptions(
            "prefab.gui.advanced.farm.horse",
            "assets/prefab/structures/advanced_horse_stable.zip",
            "textures/gui/advanced_horse_stable_topdown.png",
            false,
            false);

    public static AdvancedFarmOptions MonsterMasher = new AdvancedFarmOptions(
            "prefab.gui.advanced.farm.monster",
            "assets/prefab/structures/monster_masher.zip",
            "textures/gui/monster_masher_top_down.png",
            false,
            true);

    public static AdvancedFarmOptions ProduceFarm = new AdvancedFarmOptions(
            "prefab.gui.advanced.farm.produce",
            "assets/prefab/structures/producefarm.zip",
            "textures/gui/produce_farm_top_down.png",
            false,
            true);

    public static AdvancedFarmOptions TreeFarm = new AdvancedFarmOptions(
            "prefab.gui.advanced.farm.tree",
            "assets/prefab/structures/treefarm.zip",
            "textures/gui/tree_farm_top_down.png",
            false,
            false);

    protected AdvancedFarmOptions(String translationString,
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
