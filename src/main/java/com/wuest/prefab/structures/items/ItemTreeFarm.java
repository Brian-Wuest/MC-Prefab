package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.gui.GuiTreeFarm;
import com.wuest.prefab.structures.predefined.StructureTreeFarm;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemTreeFarm extends StructureItem {
    public ItemTreeFarm() {
        super();
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiTreeFarm.class);
        }
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureTreeFarm.ScanStructure(
                context.getLevel(),
                context.getClickedPos(),
                context.getPlayer().getDirection());
    }
}
