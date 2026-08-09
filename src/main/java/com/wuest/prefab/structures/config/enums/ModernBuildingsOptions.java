package com.wuest.prefab.structures.config.enums;


import net.minecraft.util.Direction;

public class ModernBuildingsOptions extends BaseOption{
    public static ModernBuildingsOptions Mall = new ModernBuildingsOptions(
            "prefab.gui.item_modern_mall_store",
            "assets/prefab/structures/modern_mall_store.gz",
            "textures/gui/modern_mall_store_topdown.png",
            false,
            true);

    public static ModernBuildingsOptions HipsterFruitStand = new ModernBuildingsOptions(
            "prefab.gui.item_modern_hipster_fruit_stand",
            "assets/prefab/structures/modern_hipster_fruit_stand.gz",
            "textures/gui/modern_hipster_fruit_stand_topdown.png",
            false,
            true);

    public static ModernBuildingsOptions Cinema = new ModernBuildingsOptions(
            "prefab.gui.item_modern_cinema",
            "assets/prefab/structures/modern_cinema.gz",
            "textures/gui/modern_cinema_topdown.png",
            false,
            false);

    public static ModernBuildingsOptions WaterPark = new ModernBuildingsOptions(
            "prefab.gui.item_modern_water_park",
            "assets/prefab/structures/modern_water_park.gz",
            "textures/gui/modern_water_park_topdown.png",
            false,
            false);

    public static ModernBuildingsOptions ConstructionSite = new ModernBuildingsOptions(
            "prefab.gui.item_modern_construction_site",
            "assets/prefab/structures/modern_construction_site.gz",
            "textures/gui/modern_construction_site_topdown.png",
            true,
            true);

    public static ModernBuildingsOptions ApartmentBuilding = new ModernBuildingsOptions(
            "prefab.gui.item_modern_apartment",
            "assets/prefab/structures/modern_apartment.gz",
            "textures/gui/modern_apartment_topdown.png",
            false,
            true);

    public static ModernBuildingsOptions MiniHotel = new ModernBuildingsOptions(
            "prefab.gui.item_modern_mini_hotel",
            "assets/prefab/structures/modern_mini_hotel.gz",
            "textures/gui/modern_mini_hotel_topdown.png",
            false,
            true);

    public static ModernBuildingsOptions House = new ModernBuildingsOptions(
            "prefab.gui.item_modern_house",
            "assets/prefab/structures/modern_house.gz",
            "textures/gui/modern_house_topdown.png",
            false,
            true);

    public static ModernBuildingsOptions GasStation = new ModernBuildingsOptions(
            "prefab.gui.item_modern_gas_station",
            "assets/prefab/structures/modern_gas_station.gz",
            "textures/gui/modern_gas_station_topdown.png",
            false,
            true);

    public static ModernBuildingsOptions Cottage = new ModernBuildingsOptions(
            "prefab.gui.item_modern_cottage",
            "assets/prefab/structures/modern_cottage.gz",
            "textures/gui/modern_cottage_topdown.png",
            false,
            true);

    public static ModernBuildingsOptions Bank = new ModernBuildingsOptions(
            "prefab.gui.item_modern_bank",
            "assets/prefab/structures/modern_bank.gz",
            "textures/gui/modern_bank_topdown.png",
            false,
            true);

    public static ModernBuildingsOptions TreeHouse = new ModernBuildingsOptions(
            "prefab.gui.item_modern_tree_house",
            "assets/prefab/structures/modern_tree_house.gz",
            "textures/gui/modern_tree_house_topdown.png",
            true,
            true);

    protected ModernBuildingsOptions(String translationString,
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
