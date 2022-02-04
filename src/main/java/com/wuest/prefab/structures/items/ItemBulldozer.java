package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.structures.gui.GuiBulldozer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemBulldozer extends StructureItem {

    private boolean creativePowered = false;

    /**
     * Initializes a new instance of the {@link ItemBulldozer} class.
     */
    public ItemBulldozer() {
        super(new Item.Properties()
                .tab(ModRegistry.PREFAB_GROUP)
                .durability(4));
    }

    /**
     * Initializes a new instance of the {@link ItemBulldozer} class
     *
     * @param creativePowered - Set this to true to create an always powered bulldozer.
     */
    public ItemBulldozer(boolean creativePowered) {
        super(new Item.Properties()
                .tab(ModRegistry.PREFAB_GROUP));

        this.creativePowered = creativePowered;
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        ModRegistry.guiRegistrations.add(x -> this.RegisterGui(GuiBulldozer.class));
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) {
            if (context.getClickedFace() == Direction.UP && this.getPoweredValue(context.getPlayer(), context.getHand())) {
                // Open the client side gui to determine the house options.
                Prefab.proxy.openGuiForItem(context);
                return InteractionResult.PASS;
            }
        }

        return InteractionResult.FAIL;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        boolean advancedKeyDown = Screen.hasShiftDown();

        if (!advancedKeyDown) {
            tooltip.add(GuiLangKeys.translateToComponent(GuiLangKeys.SHIFT_TOOLTIP));
        } else {
            if (this.getPoweredValue(stack)) {
                tooltip.add(GuiLangKeys.translateToComponent(GuiLangKeys.BULLDOZER_POWERED_TOOLTIP));
            } else {
                tooltip.add(GuiLangKeys.translateToComponent(GuiLangKeys.BULLDOZER_UNPOWERED_TOOLTIP));
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
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack stack) {
        return this.getPoweredValue(stack) || super.isFoil(stack);
    }

    /**
     * Override this method to change the NBT data being sent to the client. You should ONLY override this when you have
     * no other choice, as this might change behavior client side!
     *
     * @param stack The stack to send the NBT tag for
     * @return The NBT tag
     */
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        if (stack.getTag() == null
                || stack.getTag().isEmpty()) {
            // Make sure to serialize the NBT for this stack so the information is pushed to the client and the
            // appropriate Icon is displayed for this stack.
            stack.setTag(stack.serializeNBT());
        }

        return stack.getTag();
    }

    private boolean getPoweredValue(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        return this.getPoweredValue(stack);
    }

    private boolean getPoweredValue(ItemStack stack) {
        if (this.creativePowered) {
            return true;
        }

        if (stack.getItem() == ModRegistry.Bulldozer.get()) {
            if (stack.getTag() == null
                    || stack.getTag().isEmpty()) {
                stack.setTag(stack.serializeNBT());
            } else {
                CompoundTag tag = stack.getTag();

                if (tag.contains("prefab")) {
                    CompoundTag prefabTag = tag.getCompound("prefab");

                    if (prefabTag.contains("powered")) {
                        return prefabTag.getBoolean("powered");
                    }
                }
            }
        }

        return false;
    }

    public void setPoweredValue(ItemStack stack, boolean value) {
        if (stack.getTag() == null
                || stack.getTag().isEmpty()) {
            stack.setTag(stack.serializeNBT());
        }

        CompoundTag prefabTag = new CompoundTag();
        prefabTag.putBoolean("powered", value);
        stack.getTag().put(Prefab.MODID, prefabTag);
    }
}