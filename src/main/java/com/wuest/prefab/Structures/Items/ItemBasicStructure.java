package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Config.BasicStructureConfiguration;
import com.wuest.prefab.Structures.Config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.Structures.Predefined.StructureBasic;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;

/**
 * This class is used for basic structures to show the basic GUI.
 *
 * @author WuestMan
 */
public class ItemBasicStructure extends StructureItem {
    public final EnumBasicStructureName structureType;

    public ItemBasicStructure(String name, EnumBasicStructureName structureType) {
        super(name);

        this.structureType = structureType;
    }

    public static ItemStack getBasicStructureItemInHand(PlayerEntity player) {
        ItemStack stack = player.getHeldItemOffhand();

        // Get off hand first since that is the right-click hand if there is
        // something in there.
        if (stack == null || !(stack.getItem() instanceof ItemBasicStructure)) {
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemBasicStructure) {
                stack = player.getHeldItemMainhand();
            } else {
                stack = null;
            }
        }

        return stack;
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public void scanningMode(ItemUseContext context)
    {
        StructureBasic basicStructure = new StructureBasic();
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        BasicStructureConfiguration structureConfiguration = new BasicStructureConfiguration();
        structureConfiguration.basicStructureName = ((ItemBasicStructure) stack.getItem()).structureType;

        boolean isWaterStructure = structureConfiguration.basicStructureName == EnumBasicStructureName.AquaBase;

        basicStructure.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing(),
                structureConfiguration, isWaterStructure, isWaterStructure);
    }
}