package com.wuest.prefab.structures.config.enums;

import com.wuest.prefab.gui.GuiLangKeys;
import net.minecraft.util.Direction;

public class VillagerHouseOptions extends BaseOption {
    public static VillagerHouseOptions FLAT_ROOF = new VillagerHouseOptions(
            GuiLangKeys.VILLAGER_HOUSE_FLAT_ROOF,
            "assets/prefab/structures/villager_house_flat.zip",
            "textures/gui/village_house_flat.png",
            Direction.SOUTH,
            5,
            4,
            5,
            1,
            2,
            0,
            true,
            false);

    public static VillagerHouseOptions ANGLED_ROOF = new VillagerHouseOptions(
            GuiLangKeys.VILLAGER_HOUSE_ANGLED_ROOF,
            "assets/prefab/structures/villager_house_angled.zip",
            "textures/gui/village_house_angled.png",
            Direction.SOUTH,
            6,
            4,
            5,
            1,
            2,
            0,
            true,
            false);

    public static VillagerHouseOptions FENCED_ROOF = new VillagerHouseOptions(
            GuiLangKeys.VILLAGER_HOUSE_FENCED_ROOF,
            "assets/prefab/structures/villager_house_fenced.zip",
            "textures/gui/village_house_fenced.png",
            Direction.SOUTH,
            6,
            4,
            5,
            1,
            2,
            0,
            true,
            false);

    public static VillagerHouseOptions BLACKSMITH = new VillagerHouseOptions(
            GuiLangKeys.VILLAGER_HOUSE_BLACKSMITH,
            "assets/prefab/structures/villager_house_blacksmith.zip",
            "textures/gui/village_house_blacksmith.png",
            Direction.SOUTH,
            6,
            7,
            5,
            1,
            5,
            0,
            true,
            false);

    public static VillagerHouseOptions LONG_HOUSE = new VillagerHouseOptions(
            GuiLangKeys.VILLAGER_HOUSE_LONGHOUSE,
            "assets/prefab/structures/villager_house_long.zip",
            "textures/gui/village_house_long.png",
            Direction.SOUTH,
            7,
            8,
            6,
            1,
            4,
            0,
            true,
            false);

    protected VillagerHouseOptions(String translationString,
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
