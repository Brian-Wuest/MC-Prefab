package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;

/**
 * @author WuestMan
 */
public class ItemStartHouse extends StructureItem {
    public ItemStartHouse(String name) {
        super(name, ModRegistry.GuiStartHouseChooser);
    }
}