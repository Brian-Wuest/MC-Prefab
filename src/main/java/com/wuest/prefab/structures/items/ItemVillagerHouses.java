package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.config.VillagerHouseConfiguration;
import com.wuest.prefab.structures.gui.GuiVillagerHouses;
import com.wuest.prefab.structures.predefined.StructureVillagerHouses;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemVillagerHouses extends StructureItem {
    public ItemVillagerHouses() {
        super(new Item.Properties()
                .tab(ItemGroup.TAB_MISC)
                .durability(10));
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiVillagerHouses.class);
        }
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureVillagerHouses.ScanStructure(
                context.getLevel(),
                context.getClickedPos(),
                context.getPlayer().getDirection(),
                VillagerHouseConfiguration.HouseStyle.BLACKSMITH);
    }
}
