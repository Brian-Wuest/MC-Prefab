package com.wuest.prefab.structures.config.enums;

public class ModernBuildingsImprovedOptions extends BaseOption{
    public static ModernBuildingsImprovedOptions Mall = new ModernBuildingsImprovedOptions(
            "prefab.gui.modern.mall_store",
            "assets/prefab/structures/modern_mall_store.zip",
            "textures/gui/modern_mall_store.png",
            false,
            true);

    public static ModernBuildingsImprovedOptions WaterPark = new ModernBuildingsImprovedOptions(
            "prefab.gui.modern.water_park",
            "assets/prefab/structures/modern_water_park.zip",
            "textures/gui/modern_water_park.png",
            false,
            false);

    public static ModernBuildingsImprovedOptions ConstructionSite = new ModernBuildingsImprovedOptions(
            "prefab.gui.modern.construction_site",
            "assets/prefab/structures/modern_construction_site.zip",
            "textures/gui/modern_construction_site.png",
            true,
            true);

    public static ModernBuildingsImprovedOptions Library = new ModernBuildingsImprovedOptions(
            "prefab.gui.modern.library",
            "assets/prefab/structures/modern_library.zip",
            "textures/gui/modern_library.png",
            false,
            true);

    public static ModernBuildingsImprovedOptions House = new ModernBuildingsImprovedOptions(
            "prefab.gui.modern.house",
            "assets/prefab/structures/modern_house.zip",
            "textures/gui/modern_house.png",
            false,
            true);

    public static ModernBuildingsImprovedOptions GasStation = new ModernBuildingsImprovedOptions(
            "prefab.gui.modern.gas_station",
            "assets/prefab/structures/modern_gas_station.zip",
            "textures/gui/modern_gas_station.png",
            false,
            true);

    public static ModernBuildingsImprovedOptions Bank = new ModernBuildingsImprovedOptions(
            "prefab.gui.modern.bank",
            "assets/prefab/structures/modern_bank.zip",
            "textures/gui/modern_bank.png",
            false,
            true);

    protected ModernBuildingsImprovedOptions(String translationString,
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
