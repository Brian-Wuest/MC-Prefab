package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.gui.GuiStartHouseChooser;
import com.wuest.prefab.structures.predefined.StructureAlternateStart;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;

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
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiStartHouseChooser.class);
        }
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public ActionResultType useOn(ItemUseContext context) {
        if (context.getLevel().isClientSide()) {
            if (context.getClickedFace() == Direction.UP) {
                if (!Prefab.useScanningMode) {
                    // Open the client side gui to determine the house options.
                    Prefab.proxy.openGuiForItem(context);
                } else {
                    this.scanningMode(context);
                }
                return ActionResultType.PASS;
            }
        }

        return ActionResultType.FAIL;
    }

    @Override
    public void scanningMode(ItemUseContext context) {
    }
}