package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Gui.GuiNetherGate;
import com.wuest.prefab.Structures.Gui.GuiStructure;
import com.wuest.prefab.Structures.Predefined.StructureNetherGate;
import net.minecraft.item.ItemUseContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author WuestMan This is the item used to generate the Nether Gate structure.
 */
public class ItemNetherGate extends StructureItem {
    public ItemNetherGate(String name) {
        super(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiStructure getScreen() {
        return new GuiNetherGate();
    }

    @Override
    public void scanningMode(ItemUseContext context) {
        StructureNetherGate.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());
    }
}
