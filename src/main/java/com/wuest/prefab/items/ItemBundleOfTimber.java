package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * @author WuestMan
 */
public class ItemBundleOfTimber extends BlockItem {
    public ItemBundleOfTimber(Block linkedBlock) {
        super(linkedBlock, new Item.Properties().tab(ModRegistry.PREFAB_GROUP));

    }
}