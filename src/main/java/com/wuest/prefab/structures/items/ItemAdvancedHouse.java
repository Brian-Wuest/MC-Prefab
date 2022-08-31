package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.structures.gui.GuiHouseAdvanced;
import net.minecraft.world.item.context.UseOnContext;

/**
 * @author WuestMan
 */
public class ItemAdvancedHouse extends StructureItem {
    /**
     * Initializes a new instance of the {@link ItemAdvancedHouse} class.
     */
    public ItemAdvancedHouse() {
        super();
    }

    @Override
    public void scanningMode(UseOnContext context) {
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        ModRegistry.guiRegistrations.add(x -> this.RegisterGui(GuiHouseAdvanced.class));
    }
}
