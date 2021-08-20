package com.wuest.prefab.structures.config.enums;


import net.minecraft.core.Direction;

public class ModernBuildingsOptions extends BaseOption{
    public static ModernBuildingsOptions Mall = new ModernBuildingsOptions(
            "prefab.gui.item_modern_mall_store",
            "assets/prefab/structures/modern_mall_store.zip",
            "textures/gui/modern_mall_store_topdown.png",
            Direction.SOUTH,
            10,
            31,
            27,
            1,
            19,
            1,
            false,
            true);

    public static ModernBuildingsOptions HipsterFruitStand = new ModernBuildingsOptions(
            "prefab.gui.item_modern_hipster_fruit_stand",
            "assets/prefab/structures/modern_hipster_fruit_stand.zip",
            "textures/gui/modern_hipster_fruit_stand_topdown.png",
            Direction.SOUTH,
            9,
            19,
            38,
            1,
            9,
            0,
            false,
            true);

    public static ModernBuildingsOptions Cinema = new ModernBuildingsOptions(
            "prefab.gui.item_modern_cinema",
            "assets/prefab/structures/modern_cinema.zip",
            "textures/gui/modern_cinema_topdown.png",
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
            Direction.SOUTH,
            16,
            37,
            31,
            1,
            9,
            -1,
            true,
            true);

    public static ModernBuildingsOptions ApartmentBuilding = new ModernBuildingsOptions(
            "prefab.gui.item_modern_apartment",
            "assets/prefab/structures/modern_apartment.zip",
            "textures/gui/modern_apartment_topdown.png",
            Direction.SOUTH,
            23,
            12,
            11,
            1,
            10,
            -1,
            false,
            false);

    public static ModernBuildingsOptions Library = new ModernBuildingsOptions(
            "prefab.gui.item_modern_library",
            "assets/prefab/structures/modern_library.zip",
            "textures/gui/modern_library_topdown.png",
            Direction.SOUTH,
            14,
            38,
            23,
            1,
            19,
            0,
            false,
            false);

    public static ModernBuildingsOptions MiniHotel = new ModernBuildingsOptions(
            "prefab.gui.item_modern_mini_hotel",
            "assets/prefab/structures/modern_mini_hotel.zip",
            "textures/gui/modern_mini_hotel_topdown.png",
            Direction.SOUTH,
            11,
            29,
            28,
            1,
            17,
            0,
            false,
            true);

    public static ModernBuildingsOptions House = new ModernBuildingsOptions(
            "prefab.gui.item_modern_house",
            "assets/prefab/structures/modern_house.zip",
            "textures/gui/modern_house_topdown.png",
            Direction.SOUTH,
            15,
            24,
            18,
            1,
            7,
            0,
            false,
            true);

    public static ModernBuildingsOptions GasStation = new ModernBuildingsOptions(
            "prefab.gui.item_modern_gas_station",
            "assets/prefab/structures/modern_gas_station.zip",
            "textures/gui/modern_gas_station_topdown.png",
            Direction.SOUTH,
            14,
            39,
            36,
            1,
            19,
            0,
            false,
            true);

    public static ModernBuildingsOptions Cottage = new ModernBuildingsOptions(
            "prefab.gui.item_modern_cottage",
            "assets/prefab/structures/modern_cottage.zip",
            "textures/gui/modern_cottage_topdown.png",
            Direction.SOUTH,
            14,
            17,
            16,
            1,
            15,
            -2,
            false,
            true);

    public static ModernBuildingsOptions Bank = new ModernBuildingsOptions(
            "prefab.gui.item_modern_bank",
            "assets/prefab/structures/modern_bank.zip",
            "textures/gui/modern_bank_topdown.png",
            Direction.SOUTH,
            10,
            16,
            16,
            1,
            8,
            0,
            false,
            true);

    protected ModernBuildingsOptions(String translationString,
                          String assetLocation,
                          String pictureLocation,
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
