package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.structures.config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.structures.gui.GuiBasicStructure;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

/**
 * This class is used for basic structures to show the basic GUI.
 *
 * @author WuestMan
 */
@SuppressWarnings({"AccessStaticViaInstance", "ConstantConditions"})
public class ItemBasicStructure extends StructureItem {
    public final EnumBasicStructureName structureType;

    public ItemBasicStructure(EnumBasicStructureName structureType) {
        super();
        this.structureType = structureType;
    }

    public ItemBasicStructure(EnumBasicStructureName structureType, int durability) {
        super(new Item.Properties()
                .durability(durability));
        this.structureType = structureType;
    }

    public static ItemStack getBasicStructureItemInHand(Player player) {
        ItemStack stack = player.getOffhandItem();

        // Get off hand first since that is the right-click hand if there is
        // something in there.
        if (!(stack.getItem() instanceof ItemBasicStructure)) {
            if (player.getMainHandItem().getItem() instanceof ItemBasicStructure) {
                stack = player.getMainHandItem();
            } else {
                stack = null;
            }
        }

        return stack;
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        ModRegistry.guiRegistrations.add(x -> this.RegisterGui(GuiBasicStructure.class));
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public void scanningMode(UseOnContext context) {
    }
}