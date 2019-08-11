package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Predefined.StructureTreeFarm;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
public class ItemTreeFarm extends StructureItem {
    public ItemTreeFarm(String name) {
        super(name);
    }


    @Override
    public void scanningMode(ItemUseContext context) {
        StructureTreeFarm.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());
    }
}
