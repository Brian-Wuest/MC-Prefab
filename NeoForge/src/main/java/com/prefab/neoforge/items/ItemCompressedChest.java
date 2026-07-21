package com.prefab.neoforge.items;

import com.prefab.gui.GuiLangKeys;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;
import java.util.function.Consumer;

public class ItemCompressedChest extends com.prefab.items.ItemCompressedChest {
    /**
     * Initializes a new instance of the ItemCompressedChest class.
     */
    public ItemCompressedChest() {
        super();

    }

    /**
     * allows items to add custom lines of information to the mouse-over description
     */
    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext,
                                TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        consumer.accept(GuiLangKeys.translateToComponent(GuiLangKeys.COMPRESSED_CHEST));
    }
}