package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Gui.GuiStructure;
import com.wuest.prefab.Structures.Gui.GuiTreeFarm;
import com.wuest.prefab.Structures.Predefined.StructureTreeFarm;
import net.minecraft.item.ItemUseContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author WuestMan
 */
public class ItemTreeFarm extends StructureItem {
    public ItemTreeFarm(String name) {
        super(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiStructure getScreen() {
        return new GuiTreeFarm();
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureTreeFarm.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());
    }
}
