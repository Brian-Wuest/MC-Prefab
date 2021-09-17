package com.wuest.prefab.structures.config.enums;

import com.wuest.prefab.gui.GuiLangKeys;
import net.minecraft.core.Direction;

public class VillagerHouseOptions extends BaseOption {
    public static VillagerHouseOptions FLAT_ROOF = new VillagerHouseOptions(
            GuiLangKeys.VILLAGER_HOUSE_FLAT_ROOF,
            "assets/prefab/structures/villager_house_flat.zip",
            "textures/gui/village_house_flat.png",
            true,
            false);

    public static VillagerHouseOptions ANGLED_ROOF = new VillagerHouseOptions(
            GuiLangKeys.VILLAGER_HOUSE_ANGLED_ROOF,
            "assets/prefab/structures/villager_house_angled.zip",
            "textures/gui/village_house_angled.png",
            true,
            false);

    public static VillagerHouseOptions FENCED_ROOF = new VillagerHouseOptions(
            GuiLangKeys.VILLAGER_HOUSE_FENCED_ROOF,
            "assets/prefab/structures/villager_house_fenced.zip",
            "textures/gui/village_house_fenced.png",
            true,
            false);

    public static VillagerHouseOptions BLACKSMITH = new VillagerHouseOptions(
            GuiLangKeys.VILLAGER_HOUSE_BLACKSMITH,
            "assets/prefab/structures/villager_house_blacksmith.zip",
            "textures/gui/village_house_blacksmith.png",
            true,
            false);

    public static VillagerHouseOptions LONG_HOUSE = new VillagerHouseOptions(
            GuiLangKeys.VILLAGER_HOUSE_LONGHOUSE,
            "assets/prefab/structures/villager_house_long.zip",
            "textures/gui/village_house_long.png",
            true,
            false);

    protected VillagerHouseOptions(String translationString,
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
