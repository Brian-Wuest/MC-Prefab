package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Predefined.StructureChickenCoop;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
public class ItemChickenCoop extends StructureItem {
    public ItemChickenCoop(String name) {
        super(name);
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureChickenCoop.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());
    }
}
