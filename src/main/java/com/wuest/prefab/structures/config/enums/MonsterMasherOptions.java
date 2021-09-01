package com.wuest.prefab.structures.config.enums;

public class MonsterMasherOptions extends BaseOption{
    public static MonsterMasherOptions Default = new MonsterMasherOptions(
            "item.prefab.item_monster_masher",
            "assets/prefab/structures/monster_masher.zip",
            "textures/gui/monster_masher_top_down.png",
            false,
            true);

    protected MonsterMasherOptions(String translationString,
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
