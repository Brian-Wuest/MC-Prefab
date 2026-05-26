package com.prefab.structures.items;

import com.prefab.ModRegistryBase;
import com.prefab.structures.gui.GuiInstantBridge;
import net.minecraft.world.item.Item;

/**
 * This is the instant bridge item.
 *
 * @author WuestMan
 */
public class ItemInstantBridge extends StructureItem {
    public ItemInstantBridge(Item.Properties properties) {
        super(properties
                .durability(10));
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        ModRegistryBase.guiRegistrations.add(x -> this.RegisterGui(GuiInstantBridge.class));
    }
}
