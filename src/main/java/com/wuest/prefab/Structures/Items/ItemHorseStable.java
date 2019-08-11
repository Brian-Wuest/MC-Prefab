package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Gui.GuiHorseStable;
import com.wuest.prefab.Structures.Gui.GuiStructure;
import com.wuest.prefab.Structures.Predefined.StructureHorseStable;
import net.minecraft.item.ItemUseContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author WuestMan
 */
public class ItemHorseStable extends StructureItem {
    public ItemHorseStable(String name) {
        super(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiStructure getScreen() {
        return new GuiHorseStable();
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureHorseStable.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());
    }
}
