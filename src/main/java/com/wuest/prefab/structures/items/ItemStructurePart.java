package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.structures.gui.GuiStructurePart;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

/**
 * @author WuestMan
 */
public class ItemStructurePart extends StructureItem {
    public ItemStructurePart() {
        super(new Item.Properties()
                .tab(ModRegistry.PREFAB_GROUP)
                .durability(10));
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        ModRegistry.guiRegistrations.add(x -> this.RegisterGui(GuiStructurePart.class));
    }
}
