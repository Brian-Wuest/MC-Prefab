package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Predefined.StructureProduceFarm;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemProduceFarm extends StructureItem {
    public ItemProduceFarm(String name) {
        super(name);
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureProduceFarm.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());
    }
}