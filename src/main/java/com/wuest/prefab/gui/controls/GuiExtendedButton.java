package com.wuest.prefab.gui.controls;

import com.wuest.prefab.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiExtendedButton extends GuiButtonExt {
    private final ResourceLocation buttonTexture = new ResourceLocation("prefab", "textures/gui/prefab_button.png");
    private final ResourceLocation buttonTexturePressed = new ResourceLocation("prefab", "textures/gui/prefab_button_pressed.png");
    private final ResourceLocation buttonTextureHover = new ResourceLocation("prefab", "textures/gui/prefab_button_highlight.png");
    public GuiExtendedButton(int id, int xPos, int yPos, String displayString)
    {
        super(id, xPos, yPos, displayString);
    }

    public GuiExtendedButton(int id, int xPos, int yPos, int width, int height, String displayString)
    {
        super(id, xPos, yPos, width, height, displayString);
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial)
    {
        if (this.visible)
        {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            ResourceLocation buttonTexture = this.hovered ? this.buttonTextureHover : this.buttonTexture;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            Minecraft.getMinecraft().getTextureManager().bindTexture(buttonTexture);

            Gui.drawScaledCustomSizeModalRect(this.x, this.y, 0, 0, 90, 20, this.width, this.height, 90, 20);
            this.mouseDragged(mc, mouseX, mouseY);
            int color = 14737632;

            String buttonText = this.displayString;
            int strWidth = mc.fontRenderer.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRenderer.getStringWidth("...");

            if (strWidth > width - 6 && strWidth > ellipsisWidth)
                buttonText = mc.fontRenderer.trimStringToWidth(buttonText, width - 6 - ellipsisWidth).trim() + "...";

            GuiUtils.drawCenteredString(mc.fontRenderer, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, color, false);
        }
    }
}