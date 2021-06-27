package com.wuest.prefab.structures.gui;

import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;

/**
 * @author WuestMan
 */
public class GuiAdvancedWareHouse extends GuiWareHouse {

    public GuiAdvancedWareHouse() {
        super();
        this.clientGUIIdentifier = "Advanced Warehouse";
    }

    @Override
    public void Initialize() {
        super.Initialize();
        this.configuration.advanced = true;
        this.structureConfiguration = EnumStructureConfiguration.AdvancedWareHouse;
        this.structureIdentifier = "item.prefab:item_advanced_warehouse.name";
    }

}
