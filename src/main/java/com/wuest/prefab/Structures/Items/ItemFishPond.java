package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Gui.GuiFishPond;
import com.wuest.prefab.Structures.Gui.GuiStructure;
import com.wuest.prefab.Structures.Predefined.StructureFishPond;
import net.minecraft.item.ItemUseContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author WuestMan
 */
public class ItemFishPond extends StructureItem {
    public ItemFishPond(String name) {
        super(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiStructure getScreen() {
        return new GuiFishPond();
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureFishPond.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());
    }
}
