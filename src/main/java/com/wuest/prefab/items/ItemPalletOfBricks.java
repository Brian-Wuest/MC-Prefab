package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * @author WuestMan
 */
public class ItemPalletOfBricks extends BlockItem {
    public ItemPalletOfBricks() {
        super(ModRegistry.PalletOfBricks.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP));

    }
}