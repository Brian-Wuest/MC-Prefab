package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Gui.GuiAdvancedWareHouse;
import com.wuest.prefab.Structures.Gui.GuiStructure;
import com.wuest.prefab.Structures.Predefined.StructureWarehouse;
import net.minecraft.item.ItemUseContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author WuestMan
 */
public class ItemAdvancedWareHouse extends ItemWareHouse {
    public ItemAdvancedWareHouse(String name) {
        super(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiStructure getScreen() {
        return new GuiAdvancedWareHouse();
    }

    @Override
    public void scanningMode(ItemUseContext context)
    {
        StructureWarehouse.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing(),
                true);
    }
}
