package com.wuest.prefab.Structures.Items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * This is the instant bridge item.
 *
 * @author WuestMan
 */
public class ItemInstantBridge extends StructureItem {
    public ItemInstantBridge(String name) {
        super(name, new Item.Properties()
                .group(ItemGroup.MISC)
                .maxDamage(10));
    }
}
