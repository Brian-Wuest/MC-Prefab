package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.config.BasicStructureConfiguration.EnumBasicStructureName;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
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
        super(name, ModRegistry.GuiBasicStructure);

        this.structureType = structureType;
    }

    public static ItemStack getBasicStructureItemInHand(EntityPlayer player) {
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
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            if (side == EnumFacing.UP) {

				/*StructureBasic basicStructure = new StructureBasic(); ItemStack stack = player.getHeldItem(hand);
				IStructureConfigurationCapability capability =
				stack.getCapability(ModRegistry.StructureConfiguration, EnumFacing.NORTH);
				BasicStructureConfiguration structureConfiguration = capability.getConfiguration();
				basicStructure.ScanStructure(world, hitBlockPos, player.getHorizontalFacing(),
				structureConfiguration, false, false);*/

                // Open the client side gui to determine the house options.
                player.openGui(Prefab.instance, this.guiId, player.world, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());

                return EnumActionResult.PASS;
            }
        }

        return EnumActionResult.FAIL;
    }
}