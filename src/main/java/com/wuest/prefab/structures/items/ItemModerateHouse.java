    package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.structures.gui.GuiHouseImproved;
import net.minecraft.world.item.context.UseOnContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemModerateHouse extends StructureItem {
    /**
     * Initializes a new instance of the {@link ItemModerateHouse} class.
     */
    public ItemModerateHouse() {
        super();
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        ModRegistry.guiRegistrations.add(x -> this.RegisterGui(GuiHouseImproved.class));
    }

    @Override
    public void scanningMode(UseOnContext context) {
    }
}
