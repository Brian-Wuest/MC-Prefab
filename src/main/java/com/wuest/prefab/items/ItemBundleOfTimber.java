package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.world.item.Item;

/**
 * @author WuestMan
 */
public class ItemBundleOfTimber extends BlockItem {
    public ItemBundleOfTimber(Block linkedBlock) {
        super(linkedBlock, new Item.Properties().tab(ModRegistry.PREFAB_GROUP));

    }
}