package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.gui.GuiStructurePart;
import net.minecraft.creativetab.CreativeTabs;

/**
 * @author WuestMan
 */
public class ItemStructurePart extends StructureItem {
    public ItemStructurePart(String name) {
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
            this.RegisterGui(GuiStructurePart.class);
        }
    }
}
