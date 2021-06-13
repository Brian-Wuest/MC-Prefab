package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;

/**
 * This class is used to build the warehouse structure.
 *
 * @author WuestMan
 */
public class ItemWareHouse extends StructureItem {
    public ItemWareHouse(String name) {
        super(name, ModRegistry.GuiWareHouse);
    }
}