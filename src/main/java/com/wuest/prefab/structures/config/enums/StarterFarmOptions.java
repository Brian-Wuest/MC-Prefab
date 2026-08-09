package com.wuest.prefab.structures.config.enums;

public class StarterFarmOptions extends BaseOption {
    public static StarterFarmOptions BeefFarm = new StarterFarmOptions(
            "prefab.gui.starter.farm.beef",
            "assets/prefab/structures/beef_farm.gz",
            "textures/gui/beef_farm_topdown.png",
            false,
            false);

    public static StarterFarmOptions BerryFarm = new StarterFarmOptions(
            "prefab.gui.starter.farm.berry",
            "assets/prefab/structures/berry_farm.gz",
            "textures/gui/berry_farm_topdown.png",
            false,
            false);

    public static StarterFarmOptions CactusFarm = new StarterFarmOptions(
            "prefab.gui.starter.farm.cactus",
            "assets/prefab/structures/cactus_farm.gz",
            "textures/gui/cactus_farm_topdown.png",
            false,
            true);

    public static StarterFarmOptions ChickenCoop = new StarterFarmOptions(
            "prefab.gui.starter.farm.chicken",
            "assets/prefab/structures/chickencoop.gz",
            "textures/gui/chicken_coop_topdown.png",
            false,
            false);

    public static StarterFarmOptions HorseStable = new StarterFarmOptions(
            "prefab.gui.starter.farm.horse",
            "assets/prefab/structures/horsestable.gz",
            "textures/gui/horse_stable_top_down.png",
            false,
            false);

    public static StarterFarmOptions ElevatedFarm = new StarterFarmOptions(
            "prefab.gui.starter.farm.elevated",
            "assets/prefab/structures/elevated_farm.gz",
            "textures/gui/elevated_farm_topdown.png",
            false,
            false);

    public static StarterFarmOptions MultiLevelFarm = new StarterFarmOptions(
            "prefab.gui.starter.farm.multi_level",
            "assets/prefab/structures/multi_level_farm.gz",
            "textures/gui/multi_level_farm_topdown.png",
            false,
            false);

    public static StarterFarmOptions RabbitHutch = new StarterFarmOptions(
            "prefab.gui.starter.farm.rabbit",
            "assets/prefab/structures/rabbit_hutch.gz",
            "textures/gui/rabbit_hutch_topdown.png",
            false,
            false);

    protected StarterFarmOptions(String translationString,
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
