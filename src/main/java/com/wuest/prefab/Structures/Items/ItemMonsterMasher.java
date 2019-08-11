package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Gui.GuiMonsterMasher;
import com.wuest.prefab.Structures.Gui.GuiStructure;
import com.wuest.prefab.Structures.Predefined.StructureMonsterMasher;
import net.minecraft.item.ItemUseContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author WuestMan
 */
public class ItemMonsterMasher extends StructureItem {
    public ItemMonsterMasher(String name) {
        super(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiStructure getScreen() {
        return new GuiMonsterMasher();
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureMonsterMasher.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());
    }
}
