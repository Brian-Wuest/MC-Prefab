package com.wuest.prefab.structures.gui;

import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import net.minecraft.util.ResourceLocation;

/**
 * @author WuestMan
 */
public class GuiAdvancedWareHouse extends GuiWareHouse {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/advanced_warehouse_top_down.png");

    public GuiAdvancedWareHouse() {
        super();
        this.clientGUIIdentifier = "Advanced Warehouse";
    }

    @Override
    public void Initialize() {
        super.Initialize();
        this.configuration.advanced = true;
        this.structureConfiguration = EnumStructureConfiguration.AdvancedWareHouse;
        this.structureIdentifier = "item.prefab.item_advanced_warehouse";
        this.structureImageLocation = structureTopDown;
    }

}
