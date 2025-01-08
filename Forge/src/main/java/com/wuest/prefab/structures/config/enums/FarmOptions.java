package com.wuest.prefab.structures.config.enums;

public class FarmOptions extends BaseOption {
    public static FarmOptions BeefFarm = new FarmOptions(
            "prefab.gui.farm.beef",
            "assets/prefab/structures/beef_farm.gz",
            "textures/gui/beef_farm.png",
            false,
            false);

    public static FarmOptions BerryFarm = new FarmOptions(
            "prefab.gui.farm.berry",
            "assets/prefab/structures/berry_farm.gz",
            "textures/gui/berry_farm.png",
            false,
            false);

    public static FarmOptions CactusFarm = new FarmOptions(
            "prefab.gui.farm.cactus",
            "assets/prefab/structures/cactus_farm.gz",
            "textures/gui/cactus_farm.png",
            false,
            true);

    public static FarmOptions ChickenCoop = new FarmOptions(
            "prefab.gui.farm.chicken",
            "assets/prefab/structures/chicken_coop.gz",
            "textures/gui/chicken_coop.png",
            false,
            false);

    public static FarmOptions HorseStable = new FarmOptions(
            "prefab.gui.farm.horse",
            "assets/prefab/structures/horse_stable.gz",
            "textures/gui/horse_stable.png",
            false,
            false);

    public static FarmOptions ElevatedFarm = new FarmOptions(
            "prefab.gui.farm.elevated",
            "assets/prefab/structures/elevated_farm.gz",
            "textures/gui/elevated_farm.png",
            false,
            false);

    public static FarmOptions MultiLevelFarm = new FarmOptions(
            "prefab.gui.farm.multi_level",
            "assets/prefab/structures/multi_level_farm.gz",
            "textures/gui/multi_level_farm.png",
            false,
            false);

    public static FarmOptions RabbitHutch = new FarmOptions(
            "prefab.gui.farm.rabbit",
            "assets/prefab/structures/rabbit_hutch.gz",
            "textures/gui/rabbit_hutch.png",
            false,
            false);

    protected FarmOptions(String translationString,
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
