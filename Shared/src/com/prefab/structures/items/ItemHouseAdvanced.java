package com.prefab.structures.items;

import com.prefab.ModRegistryBase;
import com.prefab.structures.gui.GuiHouseAdvanced;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

/**
 * @author WuestMan
 */
public class ItemHouseAdvanced extends StructureItem {
    /**
     * Initializes a new instance of the {@link ItemHouseAdvanced} class.
     */
    public ItemHouseAdvanced(Item.Properties properties) {
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
        ModRegistryBase.guiRegistrations.add(x -> this.RegisterGui(GuiHouseAdvanced.class));
    }
}
