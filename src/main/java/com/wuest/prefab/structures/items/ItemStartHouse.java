package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.gui.GuiHouse;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ALL")
public class ItemStartHouse extends StructureItem {
    public ItemStartHouse() {
        super();
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        ModRegistry.guiRegistrations.add(x -> this.RegisterGui(GuiHouse.class));
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) {
            if (context.getClickedFace() == Direction.UP) {
                if (!Prefab.useScanningMode) {
                    // Open the client side gui to determine the house options.
                    Prefab.proxy.openGuiForItem(context);
                } else {
                    this.scanningMode(context);
                }
                return InteractionResult.PASS;
            }
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void scanningMode(UseOnContext context) {
    }
}