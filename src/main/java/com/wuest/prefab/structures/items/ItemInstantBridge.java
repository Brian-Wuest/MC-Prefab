package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.gui.GuiInstantBridge;
import net.minecraft.creativetab.CreativeTabs;

/**
 * This is the instant bridge item.
 *
 * @author WuestMan
 */
public class ItemInstantBridge extends StructureItem {
    public ItemInstantBridge(String name) {
        super(name);

        this.setCreativeTab(ModRegistry.PREFAB_GROUP);
        this.setMaxDamage(10);
        this.setMaxStackSize(1);
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void PostInit() {
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiInstantBridge.class);
        }
    }
}
