package com.prefab.structures.items;

import com.prefab.ModRegistryBase;
import com.prefab.structures.gui.GuiHouse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ALL")
public class ItemHouse extends StructureItem {
    public ItemHouse(Item.Properties properties) {
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
        ModRegistryBase.guiRegistrations.add(x -> this.RegisterGui(GuiHouse.class));
    }
}