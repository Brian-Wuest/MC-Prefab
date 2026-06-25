package com.prefab.blockItems;

import com.prefab.TooltipHelper;
import com.prefab.gui.GuiLangKeys;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class ToolTipBlockItem extends BlockItem {
    private final ToolTipInfo toolTipInfo;

    public ToolTipBlockItem (Block block, Item.Properties properties, ToolTipInfo toolTipInfo) {
        super(block, properties);

        this.toolTipInfo = toolTipInfo;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        if (TooltipHelper.isClientEnvironment()) {
            boolean advancedKeyDown = TooltipHelper.isShiftPressed();

            if (!advancedKeyDown) {
                consumer.accept(GuiLangKeys.translateToComponent(this.toolTipInfo.getNormalTranslationKey()));
            } else {
                consumer.accept(GuiLangKeys.translateToComponent(this.toolTipInfo.getTooltipTranslationKey()));
            }
        }

    }

    public enum ToolTipInfo {
        BLOCK_BOUNDARY(0, GuiLangKeys.SHIFT_TOOLTIP, GuiLangKeys.BOUNDARY_TOOLTIP);

        private final int value;

        private final String normalTranslationKey;

        private final String tooltipTranslationKey;

        ToolTipInfo(int value, String normalTranslationKey, String shiftDownTranslationKey) {
            this.value = value;
            this.normalTranslationKey = normalTranslationKey;
            this.tooltipTranslationKey = shiftDownTranslationKey;
        }

        public static ToolTipInfo ValueOf(int value) {
            ToolTipInfo returnValue = ToolTipInfo.BLOCK_BOUNDARY;

            for (ToolTipInfo current : ToolTipInfo.values()) {
                if (current.value == value) {
                    returnValue = current;
                    break;
                }
            }

            return returnValue;
        }

        public int getValue() {
            return value;
        }

        public String getNormalTranslationKey() {
            return normalTranslationKey;
        }

        public String getTooltipTranslationKey() {
            return tooltipTranslationKey;
        }
    }
}
