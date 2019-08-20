package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Predefined.StructureNetherGate;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan This is the item used to generate the Nether Gate structure.
 */
@SuppressWarnings("ConstantConditions")
public class ItemNetherGate extends StructureItem {
    public ItemNetherGate(String name) {
        super(name);
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureNetherGate.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());
    }
}
