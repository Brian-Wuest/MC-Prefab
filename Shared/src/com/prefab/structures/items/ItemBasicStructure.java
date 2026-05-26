package com.prefab.structures.items;

import com.prefab.ModRegistryBase;
import com.prefab.structures.config.BasicStructureConfiguration;
import com.prefab.structures.gui.GuiBasicStructure;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

/**
 * This class is used for basic structures to show the basic GUI.
 *
 * @author WuestMan
 */
public class ItemBasicStructure extends StructureItem {
    public final BasicStructureConfiguration.EnumBasicStructureName structureType;

    public ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName structureType, Item.Properties properties) {
        super(properties);

        this.structureType = structureType;
    }

    public ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName structureType, Item.Properties properties, int durability) {
        super(properties
                .durability(durability));
        this.structureType = structureType;
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        ModRegistryBase.guiRegistrations.add(x -> this.RegisterGui(GuiBasicStructure.class));
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public void scanningMode(UseOnContext context) {
    }
}