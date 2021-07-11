package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class ModernBuildingsOptions extends BaseOption{
    public static ModernBuildingsOptions Mall = new ModernBuildingsOptions(
            "prefab.gui.item_modern_mall_store",
            "assets/prefab/structures/modern_mall_store.zip",
            "textures/gui/modern_mall_store_topdown.png",
            164,
            160,
            Direction.SOUTH,
            18,
            31,
            27,
            1,
            19,
            1,
            false,
            false);

    public static ModernBuildingsOptions HipsterFruitStand = new ModernBuildingsOptions(
            "prefab.gui.item_modern_hipster_fruit_stand",
            "assets/prefab/structures/modern_hipster_fruit_stand.zip",
            "textures/gui/modern_hipster_fruit_stand_topdown.png",
            164,
            160,
            Direction.SOUTH,
            9,
            19,
            38,
            1,
            9,
            0,
            false,
            false);

    public static ModernBuildingsOptions Cinema = new ModernBuildingsOptions(
            "prefab.gui.item_modern_cinema",
            "assets/prefab/structures/modern_cinema.zip",
            "textures/gui/modern_cinema_topdown.png",
            164,
            160,
            Direction.SOUTH,
            12,
            20,
            34,
            1,
            10,
            -1,
            false,
            false);

    public static ModernBuildingsOptions WaterPark = new ModernBuildingsOptions(
            "prefab.gui.item_modern_water_park",
            "assets/prefab/structures/modern_water_park.zip",
            "textures/gui/modern_water_park_topdown.png",
            164,
            160,
            Direction.SOUTH,
            18,
            46,
            32,
            1,
            22,
            -1,
            false,
            false);

    public static ModernBuildingsOptions ConstructionSite = new ModernBuildingsOptions(
            "prefab.gui.item_modern_construction_site",
            "assets/prefab/structures/modern_construction_site.zip",
            "textures/gui/modern_construction_site_topdown.png",
            164,
            160,
            Direction.SOUTH,
            16,
            37,
            31,
            1,
            9,
            -1,
            true,
            false);

    public static ModernBuildingsOptions ApartmentBuilding = new ModernBuildingsOptions(
            "prefab.gui.item_modern_apartment",
            "assets/prefab/structures/modern_apartment.zip",
            "textures/gui/modern_apartment_topdown.png",
            164,
            160,
            Direction.SOUTH,
            23,
            12,
            11,
            1,
            10,
            -1,
            false,
            false);

    protected ModernBuildingsOptions(String translationString,
                          String assetLocation,
                          String pictureLocation,
                          int imageWidth,
                          int imageHeight,
                          Direction direction,
                          int height,
                          int width,
                          int length,
                          int offsetParallelToPlayer,
                          int offsetToLeftOfPlayer,
                          int heightOffset,
                          boolean hasBedColor,
                          boolean hasGlassColor) {
        super(
                translationString,
                assetLocation,
                pictureLocation,
                imageWidth,
                imageHeight,
                direction,
                height,
                width,
                length,
                offsetParallelToPlayer,
                offsetToLeftOfPlayer,
                heightOffset,
                hasBedColor,
                hasGlassColor);
    }
}
