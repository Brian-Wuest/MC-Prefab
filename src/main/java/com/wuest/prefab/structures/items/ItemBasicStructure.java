package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.config.BasicStructureConfiguration;
import com.wuest.prefab.structures.config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.structures.config.enums.AdvancedAquaBaseOptions;
import com.wuest.prefab.structures.gui.GuiBasicStructure;
import com.wuest.prefab.structures.predefined.StructureBasic;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;

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

    public static ItemStack getBasicStructureItemInHand(PlayerEntity player) {
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
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiBasicStructure.class);
        }
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public void scanningMode(ItemUseContext context) {
        StructureBasic basicStructure = new StructureBasic();
        ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        BasicStructureConfiguration structureConfiguration = new BasicStructureConfiguration();
        structureConfiguration.basicStructureName = ((ItemBasicStructure) stack.getItem()).structureType;
        structureConfiguration.chosenOption = AdvancedAquaBaseOptions.Default;

        boolean isWaterStructure = structureConfiguration.basicStructureName == EnumBasicStructureName.AquaBase
                || structureConfiguration.basicStructureName == EnumBasicStructureName.AdvancedAquaBase;

        basicStructure.ScanStructure(
                context.getLevel(),
                context.getClickedPos(),
                context.getPlayer().getDirection(),
                structureConfiguration, isWaterStructure, isWaterStructure);
    }
}