package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * @author WuestMan
 */
public class ItemPileOfBricks extends BlockItem {
    public ItemPileOfBricks() {
        super(ModRegistry.PileOfBricks.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP));

    }
}