package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.Structures.Gui.GuiBasicStructure;
import com.wuest.prefab.Structures.Gui.GuiStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getWorld().isRemote) {
            if (context.getFace() == Direction.UP) {

				/*StructureBasic basicStructure = new StructureBasic(); ItemStack stack = player.getHeldItem(hand);
				IStructureConfigurationCapability capability =
				stack.getCapability(ModRegistry.StructureConfiguration, Direction.NORTH);
				BasicStructureConfiguration structureConfiguration = capability.getConfiguration();
				basicStructure.ScanStructure(world, hitBlockPos, player.getHorizontalFacing(),
				structureConfiguration, false, false);*/

                // Open the client side gui to determine the house options.
                GuiStructure screen = this.getScreen();
                screen.pos = context.getPos();

                Minecraft.getInstance().displayGuiScreen(screen);
                // context.getPlayer().openGui(Prefab.instance, this.guiId, context.getPlayer().world, context.getPos().getX(), context.getPos().getY(), context.getPos().getZ());

                return ActionResultType.PASS;
            }
        }

        return ActionResultType.FAIL;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiStructure getScreen() {
        return new GuiBasicStructure();
    }
}