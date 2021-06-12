package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.config.ModerateHouseConfiguration;
import com.wuest.prefab.structures.gui.GuiModerateHouse;
import com.wuest.prefab.structures.predefined.StructureModerateHouse;
import net.minecraft.item.ItemUseContext;

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
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiModerateHouse.class);
        }
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureModerateHouse.ScanStructure(
                context.getLevel(),
                context.getClickedPos(),
                context.getPlayer().getDirection(),
                ModerateHouseConfiguration.HouseStyle.MOUNTAIN_HOME);
    }
}
