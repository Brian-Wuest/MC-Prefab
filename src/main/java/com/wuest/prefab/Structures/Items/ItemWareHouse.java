package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Config.WareHouseConfiguration;
import com.wuest.prefab.Structures.Gui.GuiStructure;
import com.wuest.prefab.Structures.Gui.GuiWareHouse;
import com.wuest.prefab.Structures.Predefined.StructureWarehouse;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * This class is used to build the warehouse structure.
 *
 * @author WuestMan
 */
public class ItemWareHouse extends StructureItem {
    public ItemWareHouse(String name) {
        super(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiStructure getScreen() {
        return new GuiWareHouse();
    }

    @Override
    public void scanningMode(ItemUseContext context)
    {
        StructureWarehouse.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing(),
                false);
    }
}