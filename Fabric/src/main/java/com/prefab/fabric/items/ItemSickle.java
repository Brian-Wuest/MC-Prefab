package com.prefab.fabric.items;

import com.prefab.gui.GuiLangKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.List;
import java.util.function.Consumer;

public class ItemSickle extends com.prefab.items.ItemSickle {
    public ItemSickle(ToolMaterial toolMaterial, Item.Properties properties) {
        super(toolMaterial, properties);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext,
                                TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);

        boolean advancedKeyDown = Minecraft.getInstance().hasShiftDown();

        if (!advancedKeyDown) {
            consumer.accept(GuiLangKeys.translateToComponent(GuiLangKeys.SHIFT_TOOLTIP));
        } else {
            consumer.accept(GuiLangKeys.translateToComponent(GuiLangKeys.SICKLE_DESC));
        }
    }
}
