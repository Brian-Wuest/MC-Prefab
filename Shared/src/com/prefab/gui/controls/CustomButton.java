package com.prefab.gui.controls;

import com.mojang.blaze3d.systems.RenderSystem;
import com.prefab.PrefabBase;
import com.prefab.Utils;
import com.prefab.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CustomButton extends ExtendedButton {
    private final ResourceLocation buttonTexture = ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/prefab_button.png");
    private final ResourceLocation buttonTexturePressed = ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/prefab_button_pressed.png");
    private final ResourceLocation buttonTextureHover = ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/prefab_button_highlight.png");

    public CustomButton(int xPos, int yPos, Component displayString, OnPress handler) {
        super(xPos, yPos, 200, 90, displayString, handler, null);
    }

    public CustomButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler) {
        super(xPos, yPos, width, height, displayString, handler, null);
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    //renderWidget(GuiGraphics guiGraphics, int i, int j, float f)
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        if (this.visible) {
            Minecraft mc = Minecraft.getInstance();
            this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
            ResourceLocation buttonTexture = this.isHovered ? this.buttonTextureHover : this.buttonTexture;
            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            AbstractTexture abstractTexture = textureManager.getTexture(buttonTexture);
            abstractTexture.setUseMipmaps(false);
            RenderSystem.setShaderTexture(0, abstractTexture.getTextureView());

            GuiUtils.bindAndDrawScaledTexture(buttonTexture, guiGraphics, this.getX(), this.getY(), this.width, this.height, 90, 20, 90, 20);
            int color = 14737632;

            Component buttonText = this.getMessage();
            int strWidth = mc.font.width(buttonText);
            int ellipsisWidth = mc.font.width("...");

            if (strWidth > width - 6 && strWidth > ellipsisWidth)
                buttonText = Utils.createTextComponent(mc.font.substrByWidth(buttonText, width - 6 - ellipsisWidth).getString() + "...");

            guiGraphics.drawCenteredString(mc.font, buttonText, this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, color);

        }
    }
}
