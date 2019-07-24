package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Gui.GuiModerateHouse;
import com.wuest.prefab.Structures.Gui.GuiStructure;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author WuestMan
 */
public class ItemModerateHouse extends StructureItem {
    /**
     * Initializes a new instance of the {@link ItemModerateHouse} class.
     *
     * @param name The name to register this item as.
     */
    public ItemModerateHouse(String name) {
        super(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiStructure getScreen() {
        return new GuiModerateHouse();
    }
}
