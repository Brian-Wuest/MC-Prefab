package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.gui.GuiLangKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author WuestMan
 */
public class ItemBulldozer extends StructureItem {

    /**
     * Initializes a new instance of the {@link ItemBulldozer} class.
     *
     * @param name The registered name of this item.
     */
    public ItemBulldozer(String name) {
        super(name, ModRegistry.GuiBulldozer);
        this.setMaxDamage(4);
        this.setMaxStackSize(1);
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand, EnumFacing side, float hitX,
                                      float hitY, float hitZ) {
        if (world.isRemote) {
            if (side == EnumFacing.UP && this.getPoweredValue(player, hand)) {
                // Open the client side gui to determine the house options.
                player.openGui(Prefab.instance, this.guiId, player.world, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
                return EnumActionResult.PASS;
            }
        }

        return EnumActionResult.FAIL;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, world, tooltip, advanced);

        boolean advancedKeyDown = Minecraft.getMinecraft().currentScreen.isShiftKeyDown();

        if (!advancedKeyDown) {
            tooltip.add(GuiLangKeys.translateString(GuiLangKeys.SHIFT_TOOLTIP));
        } else {
            if (this.getPoweredValue(stack)) {
                tooltip.add(GuiLangKeys.translateString(GuiLangKeys.BULLDOZER_POWERED_TOOLTIP));
            } else {
                tooltip.add(GuiLangKeys.translateString(GuiLangKeys.BULLDOZER_UNPOWERED_TOOLTIP));
            }
        }
    }

    /**
     * Returns true if this item has an enchantment glint. By default, this returns
     * <code>stack.isItemEnchanted()</code>, but other items can override it (for instance, written books always return
     * true).
     * <p>
     * Note that if you override this method, you generally want to also call the super version (on {@link Item}) to get
     * the glint for enchanted items. Of course, that is unnecessary if the overwritten version always returns true.
     */
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return this.getPoweredValue(stack) || super.hasEffect(stack);
    }

    /**
     * This used to be 'display damage' but its really just 'aux' data in the ItemStack, usually shares the same variable as damage.
     *
     * @param stack
     * @return
     */
    @Override
    public int getMetadata(ItemStack stack) {
        if (stack.getTagCompound() == null
                || stack.getTagCompound().isEmpty()) {
            // Make sure to serialize the NBT for this stack so the information is pushed to the client and the appropriate Icon is displayed for this stack.
            stack.setTagCompound(stack.serializeNBT());
        }

        return stack.getItemDamage();
    }

    /**
     * Override this method to change the NBT data being sent to the client.
     * You should ONLY override this when you have no other choice, as this might change behavior client side!
     *
     * @param stack The stack to send the NBT tag for
     * @return The NBT tag
     */
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        if (stack.getTagCompound() == null
                || stack.getTagCompound().isEmpty()) {
            // Make sure to serialize the NBT for this stack so the information is pushed to the client and the appropriate Icon is displayed for this stack.
            stack.setTagCompound(stack.serializeNBT());
        }

        return stack.getTagCompound();
    }

    public boolean getPoweredValue(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        return this.getPoweredValue(stack);
    }

    public boolean getPoweredValue(ItemStack stack) {
        if (stack.getItem() == ModRegistry.Bulldozer) {
            if (stack.getTagCompound() == null
                    || stack.getTagCompound().isEmpty()) {
                stack.setTagCompound(stack.serializeNBT());
            } else {
                NBTTagCompound tag = stack.getTagCompound();

                if (tag.hasKey("prefab")) {
                    NBTTagCompound prefabTag = tag.getCompoundTag("prefab");

                    if (prefabTag.hasKey("powered")) {
                        return prefabTag.getBoolean("powered");
                    }
                }
            }
        }

        return false;
    }

    public void setPoweredValue(ItemStack stack, boolean value) {
        if (stack.getTagCompound() == null
                || stack.getTagCompound().isEmpty()) {
            stack.setTagCompound(stack.serializeNBT());
        }

        NBTTagCompound prefabTag = new NBTTagCompound();
        prefabTag.setBoolean("powered", value);
        stack.getTagCompound().setTag("prefab", prefabTag);
    }
}