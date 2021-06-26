package com.wuest.prefab.gui.controls;

import com.wuest.prefab.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author WuestMan
 */
public class GuiCheckBox extends GuiButton {
    private static final ResourceLocation buttonTexture = new ResourceLocation("prefab", "textures/gui/prefab_checkbox.png");
    private static final ResourceLocation buttonTexturePressed = new ResourceLocation("prefab", "textures/gui/prefab_checkbox_selected.png");
    private static final ResourceLocation buttonTextureHover = new ResourceLocation("prefab", "textures/gui/prefab_checkbox_hover.png");

    protected int boxWidth;
    protected int boxHeight;
    protected int stringColor;
    protected boolean withShadow;
    protected int labelWidth;
    protected boolean isChecked;

    public GuiCheckBox(int id, int xPos, int yPos, String displayString, boolean isChecked) {
        super(id, xPos, yPos, displayString);

        this.boxWidth = 11;
        this.labelWidth = 98;
        this.isChecked = isChecked;
        this.width = this.boxWidth + 2 + Minecraft.getMinecraft().fontRenderer.getStringWidth(displayString);
        this.height = 11;
        this.boxHeight = 11;
    }

    /**
     * Gets the string color to write.
     *
     * @return The color used when writing the string value of this checkbox.
     */
    public int getStringColor() {
        return this.stringColor;
    }

    /**
     * Sets the color used when writing the text for this checkbox.
     *
     * @param color The color used for the text.
     * @return An updated instance of this class.
     */
    public GuiCheckBox setStringColor(int color) {
        this.stringColor = color;
        return this;
    }

    /**
     * Gets a value indicating whether a shadow is included with the checkbox text.
     *
     * @return The value of whether shadows are included when writing the text of this checkbox.
     */
    public boolean getWithShadow() {
        return this.withShadow;
    }

    /**
     * Sets the value of whether shadows are included when writing the text of this checkbox.
     *
     * @param value The new value of the property.
     * @return An updated instance of this class
     */
    public GuiCheckBox setWithShadow(boolean value) {
        this.withShadow = value;
        return this;
    }

    public GuiCheckBox setLabelWidth(int value) {
        this.labelWidth = value;
        return this;
    }

    public boolean isChecked()
    {
        return this.isChecked;
    }

    public void setIsChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        if (this.visible) {
            ResourceLocation resourceLocation = GuiCheckBox.buttonTexture;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.boxWidth && mouseY < this.y + this.boxHeight;

            if (this.isChecked()) {
                resourceLocation = GuiCheckBox.buttonTexturePressed;
            } else if (this.hovered) {
                resourceLocation = GuiCheckBox.buttonTextureHover;
            }

            GuiUtils.bindTexture(resourceLocation);
            Gui.drawModalRectWithCustomSizedTexture(this.x, this.y, 0, 0, 11, 11, 11, 11);

            this.mouseDragged(mc, mouseX, mouseY);
            int color = this.stringColor;

            if (this.withShadow) {
                this.drawString(mc.fontRenderer, displayString, x + this.boxWidth + 2, y + 2, color);
            } else {
                mc.fontRenderer.drawSplitString(displayString, x + this.boxWidth + 2, y + 2, this.labelWidth, color);
            }
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.boxHeight)
        {
            this.isChecked = !this.isChecked;
            return true;
        }

        return false;
    }
}
