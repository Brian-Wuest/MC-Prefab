package com.wuest.prefab.structures.config.enums;

public class ModernBuildingsOptions extends BaseOption {
    public static ModernBuildingsOptions HipsterFruitStand = new ModernBuildingsOptions(
            "prefab.gui.item_modern_hipster_fruit_stand",
            "assets/prefab/structures/modern_hipster_fruit_stand.zip",
            "textures/gui/modern_hipster_fruit_stand_topdown.png",
            false,
            true);

    public static ModernBuildingsOptions Cinema = new ModernBuildingsOptions(
            "prefab.gui.item_modern_cinema",
            "assets/prefab/structures/modern_cinema.zip",
            "textures/gui/modern_cinema_topdown.png",
            false,
            false);

    public static ModernBuildingsOptions ApartmentBuilding = new ModernBuildingsOptions(
            "prefab.gui.item_modern_apartment",
            "assets/prefab/structures/modern_apartment.zip",
            "textures/gui/modern_apartment_topdown.png",
            false,
            true);

    public static ModernBuildingsOptions MiniHotel = new ModernBuildingsOptions(
            "prefab.gui.item_modern_mini_hotel",
            "assets/prefab/structures/modern_mini_hotel.zip",
            "textures/gui/modern_mini_hotel_topdown.png",
            false,
            true);

    public static ModernBuildingsOptions Cottage = new ModernBuildingsOptions(
            "prefab.gui.item_modern_cottage",
            "assets/prefab/structures/modern_cottage.zip",
            "textures/gui/modern_cottage_topdown.png",
            false,
            true);

    public static ModernBuildingsOptions Restaurant = new ModernBuildingsOptions(
            "prefab.gui.item_modern_restaurant",
            "assets/prefab/structures/modern_restaurant.zip",
            "textures/gui/modern_restaurant.png",
            false,
            true);

    public static ModernBuildingsOptions JuiceShop = new ModernBuildingsOptions(
            "prefab.gui.item_modern_juice_shop",
            "assets/prefab/structures/modern_juice_shop.zip",
            "textures/gui/modern_juice_shop.png",
            false,
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
