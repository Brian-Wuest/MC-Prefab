package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Gui.GuiStructure;
import com.wuest.prefab.Structures.Gui.GuiStructurePart;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author WuestMan
 */
public class ItemStructurePart extends StructureItem {
    public ItemStructurePart(String name) {
        super(name, new Item.Properties()
                .group(ItemGroup.MISC)
                .maxDamage(10));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiStructure getScreen() {
        return new GuiStructurePart();
    }
}
