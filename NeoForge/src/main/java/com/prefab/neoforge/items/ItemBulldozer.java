package com.prefab.neoforge.items;

import com.prefab.Utils;
import com.prefab.gui.GuiLangKeys;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

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
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay,
                                Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);

        boolean advancedKeyDown = Screen.hasShiftDown();

        if (!advancedKeyDown) {
            consumer.accept(GuiLangKeys.translateToComponent(GuiLangKeys.SHIFT_TOOLTIP));
        } else {
            if (this.getPoweredValue(itemStack)) {
                for (Component component : Utils.WrapStringToLiterals(GuiLangKeys.translateString(GuiLangKeys.BULLDOZER_POWERED_TOOLTIP))) {
                    consumer.accept(component);
                }
            } else {
                for (Component component : Utils.WrapStringToLiterals(GuiLangKeys.translateString(GuiLangKeys.BULLDOZER_UNPOWERED_TOOLTIP))) {
                    consumer.accept(component);
                }
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
    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return this.getPoweredValue(stack) || super.isFoil(stack);
    }
}
