package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Predefined.StructureFishPond;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
public class ItemFishPond extends StructureItem {
    public ItemFishPond(String name) {
        super(name);
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureFishPond.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());
    }
}
