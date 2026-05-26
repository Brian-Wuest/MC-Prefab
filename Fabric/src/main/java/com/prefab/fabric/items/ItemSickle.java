package com.prefab.fabric.items;

import com.prefab.gui.GuiLangKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;

public class ItemSickle extends com.prefab.items.ItemSickle {
    public ItemSickle(ToolMaterial toolMaterial, Item.Properties properties) {
        super(toolMaterial, properties);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip,
                                TooltipFlag advanced) {
        super.appendHoverText(stack, tooltipContext, tooltip, advanced);

        boolean advancedKeyDown = Screen.hasShiftDown();

        if (!advancedKeyDown) {
            tooltip.add(GuiLangKeys.translateToComponent(GuiLangKeys.SHIFT_TOOLTIP));
        } else {
            tooltip.add(GuiLangKeys.translateToComponent(GuiLangKeys.SICKLE_DESC));
        }
    }
}
