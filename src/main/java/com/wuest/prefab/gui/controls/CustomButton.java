package com.wuest.prefab.gui.controls;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wuest.prefab.Utils;
import com.wuest.prefab.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class CustomButton extends ExtendedButton {
    private final ResourceLocation buttonTexture = new ResourceLocation("prefab", "textures/gui/prefab_button.png");
    private final ResourceLocation buttonTexturePressed = new ResourceLocation("prefab", "textures/gui/prefab_button_pressed.png");
    private final ResourceLocation buttonTextureHover = new ResourceLocation("prefab", "textures/gui/prefab_button_highlight.png");

    public CustomButton(int xPos, int yPos, ITextComponent displayString, IPressable handler) {
        super(xPos, yPos, 200, 90, displayString, handler);
    }

    public CustomButton(int xPos, int yPos, int width, int height, ITextComponent displayString, IPressable handler) {
        super(xPos, yPos, width, height, displayString, handler);
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void renderButton(MatrixStack mStack, int mouseX, int mouseY, float partial) {
        if (this.visible) {
            Minecraft mc = Minecraft.getInstance();
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            ResourceLocation buttonTexture = this.isHovered ? this.buttonTextureHover : this.buttonTexture;

            RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
            GuiUtils.bindAndDrawScaledTexture(buttonTexture, mStack, this.x, this.y, this.width, this.height, 90, 20, 90, 20);
            int color = 14737632;

            ITextComponent buttonText = this.getMessage();
            int strWidth = mc.font.width(buttonText);
            int ellipsisWidth = mc.font.width("...");

            if (strWidth > width - 6 && strWidth > ellipsisWidth) {
                buttonText = Utils.createTextComponent(mc.font.substrByWidth(buttonText, width - 6 - ellipsisWidth).getString() + "...");
            }

            AbstractGui.drawCenteredString(mStack, mc.font, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);
        }
    }
}
