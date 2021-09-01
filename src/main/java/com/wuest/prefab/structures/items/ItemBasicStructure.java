package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.config.BasicStructureConfiguration;
import com.wuest.prefab.structures.config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.structures.config.enums.BarnOptions;
import com.wuest.prefab.structures.gui.GuiBasicStructure;
import com.wuest.prefab.structures.predefined.StructureBasic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    public ItemBasicStructure(String name, EnumBasicStructureName structureType, int durability) {
        super(name);

        this.setMaxDamage(durability);
        this.setMaxStackSize(1);
        this.structureType = structureType;
    }

    public static ItemStack getBasicStructureItemInHand(EntityPlayer player) {
        ItemStack stack = player.getHeldItemOffhand();

        // Get off hand first since that is the right-click hand if there is
        // something in there.
        if (!(stack.getItem() instanceof ItemBasicStructure)) {
            if (player.getHeldItemMainhand().getItem() instanceof ItemBasicStructure) {
                stack = player.getHeldItemMainhand();
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
    protected void PostInit() {
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiBasicStructure.class);
        }
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public void scanningMode(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand) {
    }
}