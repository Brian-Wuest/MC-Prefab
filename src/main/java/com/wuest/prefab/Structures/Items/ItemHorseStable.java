package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Predefined.StructureHorseStable;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
public class ItemHorseStable extends StructureItem {
    public ItemHorseStable(String name) {
        super(name);
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureHorseStable.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());
    }
}
