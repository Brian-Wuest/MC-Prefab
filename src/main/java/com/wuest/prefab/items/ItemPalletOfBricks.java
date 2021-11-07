package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.world.item.Item;

/**
 * @author WuestMan
 */
public class ItemPalletOfBricks extends BlockItem {
    public ItemPalletOfBricks() {
        super(ModRegistry.PalletOfBricks.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP));

    }
}