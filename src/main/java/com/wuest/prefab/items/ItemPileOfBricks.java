package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.world.item.Item;

/**
 * @author WuestMan
 */
public class ItemPileOfBricks extends Item {
    public ItemPileOfBricks() {
        super(new Item.Properties().tab(ModRegistry.PREFAB_GROUP));

    }
}