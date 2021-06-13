package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author WuestMan
 */
public class ItemPileOfBricks extends Item {
    public ItemPileOfBricks(String name) {
        super();

        this.setCreativeTab(CreativeTabs.MATERIALS);
        ModRegistry.setItemName(this, name);
    }
}