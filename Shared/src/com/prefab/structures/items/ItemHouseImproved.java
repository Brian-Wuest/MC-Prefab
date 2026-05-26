package com.prefab.structures.items;

import com.prefab.ModRegistryBase;
import com.prefab.structures.gui.GuiHouseImproved;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

/**
 * @author WuestMan
 */
public class ItemHouseImproved extends StructureItem {
    /**
     * Initializes a new instance of the {@link ItemHouseImproved} class.
     */
    public ItemHouseImproved(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void scanningMode(UseOnContext context) {
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        ModRegistryBase.guiRegistrations.add(x -> this.RegisterGui(GuiHouseImproved.class));
    }
}
