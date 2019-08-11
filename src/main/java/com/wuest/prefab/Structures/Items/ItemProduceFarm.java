package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Gui.GuiProduceFarm;
import com.wuest.prefab.Structures.Gui.GuiStructure;
import com.wuest.prefab.Structures.Predefined.StructureProduceFarm;
import net.minecraft.item.ItemUseContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author WuestMan
 */
public class ItemProduceFarm extends StructureItem {
    public ItemProduceFarm(String name) {
        super(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiStructure getScreen() {
        return new GuiProduceFarm();
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureProduceFarm.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());
    }
}