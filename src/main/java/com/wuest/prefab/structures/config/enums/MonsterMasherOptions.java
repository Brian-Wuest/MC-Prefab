package com.wuest.prefab.structures.config.enums;

import net.minecraft.util.Direction;

public class MonsterMasherOptions extends BaseOption{
    public static MonsterMasherOptions Default = new MonsterMasherOptions(
            "item.prefab.item_monster_masher",
            "assets/prefab/structures/monster_masher.zip",
            "textures/gui/monster_masher_top_down.png",
            164,
            160,
            Direction.SOUTH,
            18,
            13,
            15,
            1,
            6,
            0,
            false,
            true);

    protected MonsterMasherOptions(String translationString,
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
