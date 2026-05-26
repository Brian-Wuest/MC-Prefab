package com.prefab.structures.items;

import com.prefab.ClientModRegistryBase;
import com.prefab.PrefabBase;
import com.prefab.structures.gui.GuiStructure;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

import java.lang.reflect.InvocationTargetException;

/**
 * @author WuestMan
 */
public class StructureItem extends Item {

    /**
     * Initializes a new instance of the StructureItem class.
     */
    public StructureItem(Item.Properties props) {
        super(props);
        this.Initialize();
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide) {
            if (context.getClickedFace() == Direction.UP) {
                if (PrefabBase.useScanningMode) {
                    this.scanningMode(context);
                } else {
                    // Open the client side gui to determine the house options.
                    ClientModRegistryBase.openGuiForItem(context);
                }

                return InteractionResult.PASS;
            }
        }

        return InteractionResult.FAIL;
    }

    public void scanningMode(UseOnContext context) {
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    protected void Initialize() {
    }

    protected void RegisterGui(Class<?> classToRegister) {
        try {
            GuiStructure userInterface = (GuiStructure) classToRegister.getDeclaredConstructor().newInstance();
            ClientModRegistryBase.ModGuis.put(this, userInterface);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
