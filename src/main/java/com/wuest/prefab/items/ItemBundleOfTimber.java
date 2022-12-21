package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * @author WuestMan
 */
public class ItemBundleOfTimber extends BlockItem {
    public ItemBundleOfTimber(Block linkedBlock) {
        super(linkedBlock, new Item.Properties());

    }
}