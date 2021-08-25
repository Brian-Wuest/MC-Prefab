package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.proxy.ClientProxy;
import com.wuest.prefab.structures.gui.GuiStructure;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;

/**
 * @author WuestMan
 */
@SuppressWarnings("NullableProblems")
public class StructureItem extends Item {

    /**
     * Initializes a new instance of the StructureItem class.
     */
    public StructureItem() {
        super(new Item.Properties().tab(ModRegistry.PREFAB_GROUP));
        this.Initialize();
    }

    public StructureItem(Item.Properties properties) {
        super(properties);
        this.Initialize();
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public ActionResultType useOn(ItemUseContext context) {
        if (context.getLevel().isClientSide) {
            if (context.getClickedFace() == Direction.UP) {
                if (Prefab.useScanningMode) {
                    this.scanningMode(context);
                } else {
                    // Open the client side gui to determine the house options.
                    Prefab.proxy.openGuiForItem(context);
                }

                return ActionResultType.PASS;
            }
        }

        return ActionResultType.FAIL;
    }

    public void scanningMode(ItemUseContext context) {
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    protected void Initialize() {
    }

    protected void RegisterGui(Class<?> classToRegister) {
        try {
            if (Prefab.proxy.isClient) {
                GuiStructure userInterface = (GuiStructure) classToRegister.newInstance();
                ClientProxy.ModGuis.put(this, userInterface);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
