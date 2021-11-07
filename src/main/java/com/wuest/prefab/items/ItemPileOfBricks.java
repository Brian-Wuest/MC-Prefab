package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.world.item.Item;

/**
 * @author WuestMan
 */
public class ItemPileOfBricks extends BlockItem {
    public ItemPileOfBricks() {
        super(ModRegistry.PileOfBricks.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP));

    }
}