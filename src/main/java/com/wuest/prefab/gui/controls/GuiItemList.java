package com.wuest.prefab.gui.controls;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.gui.GuiUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class GuiItemList extends GuiListBox {
    /**
     * Initializes a new instance of the GuiListBox class.
     *
     * @param minecraft           The minecraft instance.
     * @param width               The width of the list.
     * @param height              The height of the list.
     * @param x                   The x-position of the top-left most part of the control.
     * @param y                   The y-position of the top-left most part of the control.
     * @param itemHeight          The height of each item in the list.
     * @param bufferColor         The color to use around the top and bottom to hide partially shown items.
     * @param entryChangedHandler The handler to call whenever an entry has been selected.
     */
    public GuiItemList(Minecraft minecraft, int width, int height, int x, int y, int itemHeight, int bufferColor, IEntryChanged entryChangedHandler) {
        super(minecraft, width, height, x, y, itemHeight, bufferColor, entryChangedHandler);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float f) {
        if (this.visible) {
            super.render(poseStack, mouseX, mouseY, f);

            // Render any tooltips after rendering the main box.
            // This is because the main box rendering has clipping enabled and these tooltips should show outside of the box if necessary.
            for (ListEntry entry : this.children()) {
                if (entry instanceof ItemEntry itemEntry) {
                    if (itemEntry.IsHovered()) {
                        GuiUtils.renderTooltip(poseStack, new ItemStack(itemEntry.getEntryItem()), mouseX, mouseY, this.x1, this.y1);
                    }
                }
            }
        }
    }

    public GuiItemList addEntry(Item item, int neededCount, int hasCount) {
        ItemEntry entry = new ItemEntry(this, this.minecraft.getItemRenderer());
        entry.setItemEntry(item).setNeededCount(neededCount).setHasCount(hasCount);

        this.addEntry(entry);
        return this;
    }

    protected void replaceItemEntries(Collection<ItemEntry> collection) {
        this.children().clear();
        this.children().addAll(collection);
    }

    public static class ItemEntry extends ListEntry {
        private final ResourceLocation checkMark = new ResourceLocation("prefab", "textures/gui/check_mark.png");
        private final ResourceLocation xMark = new ResourceLocation("prefab", "textures/gui/x.png");

        private final ItemRenderer itemRenderer;
        private Item entryItem;
        private int neededCount;
        private int hasCount;
        private boolean isHovered;

        private GuiItemList parent;

        public ItemEntry(GuiItemList parent, ItemRenderer itemRenderer) {
            super(parent);
            this.itemRenderer = itemRenderer;
            this.setText("");
            this.parent = parent;
        }

        public Item getEntryItem() {
            return this.entryItem;
        }

        public ItemEntry setItemEntry(Item value) {
            this.entryItem = value;
            return this;
        }

        public int getNeededCount() {
            return this.neededCount;
        }

        public ItemEntry setNeededCount(int value) {
            this.neededCount = value;
            return this;
        }

        public int getHasCount() {
            return this.hasCount;
        }

        public ItemEntry setHasCount(int value) {
            this.hasCount = value;
            return this;
        }

        public boolean IsHovered() {
            return this.isHovered;
        }

        @Override
        public void render(PoseStack poseStack, int entryIndex, int rowTop, int rowLeft, int rowWidth, int rowHeightWithoutBuffer, int mouseX, int mouseY, boolean isHovered, float partialTicks) {
            Minecraft mc = Minecraft.getInstance();
            int textColor = 16777215;

            if (this.entryItem != null) {
                GuiUtils.drawItemBackground(rowLeft + 2, rowTop);

                this.itemRenderer.renderGuiItem(new ItemStack(this.entryItem), rowLeft + 3, rowTop + 1);

                this.drawText(poseStack, mc.font, rowTop, rowLeft, textColor);

                this.isHovered = isHovered;
            }
        }

        private void drawText(PoseStack poseStack, Font font, int rowTop, int rowLeft, int textColor) {
            TextComponent textComponent = new TextComponent(String.valueOf(this.hasCount) + " / " + String.valueOf(this.neededCount));

            int textRowLeft = rowLeft + 25;
            int neededWidth = font.width(textComponent.getString()) + 6;
            font.draw(poseStack, textComponent, textRowLeft + 2, rowTop + 6, textColor);

            if (this.neededCount > this.hasCount) {
                // Draw red x to show that there is still items needed.
                GuiUtils.bindAndDrawTexture(this.xMark, poseStack, textRowLeft + neededWidth, rowTop + 4, 0, 12, 12, 12, 12);
            } else {
                GuiUtils.bindAndDrawTexture(this.checkMark, poseStack, textRowLeft + neededWidth, rowTop + 4, 0, 12, 12, 12, 12);
            }
        }
    }
}
