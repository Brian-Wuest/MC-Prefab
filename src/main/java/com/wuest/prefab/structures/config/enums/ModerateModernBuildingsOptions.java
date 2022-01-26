package com.wuest.prefab.structures.config.enums;

public class ModerateModernBuildingsOptions extends BaseOption {
    public static ModerateModernBuildingsOptions Mall = new ModerateModernBuildingsOptions(
            "prefab.gui.item_modern_mall_store",
            "assets/prefab/structures/modern_mall_store.zip",
            "textures/gui/modern_mall_store_topdown.png",
            false,
            true);

    public static ModerateModernBuildingsOptions WaterPark = new ModerateModernBuildingsOptions(
            "prefab.gui.item_modern_water_park",
            "assets/prefab/structures/modern_water_park.zip",
            "textures/gui/modern_water_park_topdown.png",
            false,
            false);

    public static ModerateModernBuildingsOptions ConstructionSite = new ModerateModernBuildingsOptions(
            "prefab.gui.item_modern_construction_site",
            "assets/prefab/structures/modern_construction_site.zip",
            "textures/gui/modern_construction_site_topdown.png",
            true,
            true);

    public static ModerateModernBuildingsOptions Library = new ModerateModernBuildingsOptions(
            "prefab.gui.item_modern_library",
            "assets/prefab/structures/modern_library.zip",
            "textures/gui/modern_library_topdown.png",
            false,
            true);

    public static ModerateModernBuildingsOptions House = new ModerateModernBuildingsOptions(
            "prefab.gui.item_modern_house",
            "assets/prefab/structures/modern_house.zip",
            "textures/gui/modern_house_topdown.png",
            false,
            true);

    public static ModerateModernBuildingsOptions GasStation = new ModerateModernBuildingsOptions(
            "prefab.gui.item_modern_gas_station",
            "assets/prefab/structures/modern_gas_station.zip",
            "textures/gui/modern_gas_station_topdown.png",
            false,
            true);

    public static ModerateModernBuildingsOptions Bank = new ModerateModernBuildingsOptions(
            "prefab.gui.item_modern_bank",
            "assets/prefab/structures/modern_bank.zip",
            "textures/gui/modern_bank_topdown.png",
            false,
            true);

    protected ModerateModernBuildingsOptions(String translationString,
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
