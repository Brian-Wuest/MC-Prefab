package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.gui.GuiWareHouse;
import com.wuest.prefab.structures.predefined.StructureWarehouse;
import net.minecraft.item.ItemUseContext;

/**
 * This class is used to build the warehouse structure.
 *
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemWareHouse extends StructureItem {
    public ItemWareHouse() {
        super();
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiWareHouse.class);
        }
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureWarehouse.ScanStructure(
                context.getLevel(),
                context.getClickedPos(),
                context.getPlayer().getDirection(),
                false);
    }
}