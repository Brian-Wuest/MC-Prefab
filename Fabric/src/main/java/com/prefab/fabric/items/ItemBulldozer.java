package com.prefab.fabric.items;

import com.prefab.Utils;
import com.prefab.gui.GuiLangKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Fabric specific override for client-side operations.
 */
public class ItemBulldozer extends com.prefab.structures.items.ItemBulldozer {
    public ItemBulldozer(Item.Properties properties) {
        super(properties);
    }

    public ItemBulldozer(Item.Properties properties, boolean powered) {
        super(properties, powered);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.TooltipContext tooltipContext, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);

        boolean advancedKeyDown = Screen.hasShiftDown();

        if (!advancedKeyDown) {
            tooltip.add(GuiLangKeys.translateToComponent(GuiLangKeys.SHIFT_TOOLTIP));
        } else {
            if (this.getPoweredValue(stack)) {
                tooltip.addAll(Utils.WrapStringToLiterals(GuiLangKeys.translateString(GuiLangKeys.BULLDOZER_POWERED_TOOLTIP)));
            } else {
                tooltip.addAll(Utils.WrapStringToLiterals(GuiLangKeys.translateString(GuiLangKeys.BULLDOZER_UNPOWERED_TOOLTIP)));
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
    @Environment(EnvType.CLIENT)
    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return this.getPoweredValue(stack) || super.isFoil(stack);
    }
}
