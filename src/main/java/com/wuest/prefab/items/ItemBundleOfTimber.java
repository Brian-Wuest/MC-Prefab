package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author WuestMan
 */
public class ItemBundleOfTimber extends Item {
    public ItemBundleOfTimber(String name) {
        super();

        this.setCreativeTab(ModRegistry.PREFAB_GROUP);
        ModRegistry.setItemName(this, name);
    }
}