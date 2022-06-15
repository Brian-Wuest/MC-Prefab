package com.wuest.prefab.gui.controls;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ExtendedButton extends Button
{
    public float fontScale = 1;
    private final String label;

    public ExtendedButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler, String label)
    {
        super(xPos, yPos, width, height, displayString, handler);
        this.label = label;
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        if (label == null) {
            return super.createNarrationMessage();
        } else {
            return Component.literal(label + ": ").append(super.createNarrationMessage());
        }
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void renderButton(PoseStack mStack, int mouseX, int mouseY, float partial)
    {
        if (this.visible) {
            Minecraft mc = Minecraft.getInstance();
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);

            int i = this.getYImage(this.isHoveredOrFocused());

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(mStack, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
            this.blit(mStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);

            this.renderBg(mStack, mc, mouseX, mouseY);

            Component buttonText = this.getMessage();
            int strWidth = mc.font.width(buttonText);
            int ellipsisWidth = mc.font.width("...");

            if (strWidth > width - 6 && strWidth > ellipsisWidth) {
                buttonText = Utils.createTextComponent(mc.font.substrByWidth(buttonText, width - 6 - ellipsisWidth).getString() + "...");
            }

            PoseStack originalStack = new PoseStack();

            originalStack.pushPose();
            originalStack.scale(this.fontScale, this.fontScale, this.fontScale);

            int xPosition = (int) ((this.x + this.width / 2) / this.fontScale);
            int yPosition = (int) ((this.y + (this.height - (8 * this.fontScale)) / 2) / this.fontScale);

            GuiComponent.drawCenteredString(originalStack, mc.font, buttonText, xPosition, yPosition, this.getFGColor());
            originalStack.popPose();
        }
    }

    @Override
    public int getFGColor() {
        return this.active ? 16777215 : 10526880; // White : Light Grey
    }
}
