package com.wuest.prefab.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiUtils;

/**
 * @author WuestMan
 */
public class GuiCheckBox extends net.minecraftforge.fml.client.config.GuiCheckBox {
    protected int boxWidth;
    protected int stringColor;
    protected boolean withShadow;

    public GuiCheckBox(int id, int xPos, int yPos, String displayString, boolean isChecked) {
        super(id, xPos, yPos, displayString, isChecked);

        this.boxWidth = 11;
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

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        if (this.visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.boxWidth && mouseY < this.y + this.height;
            GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.x, this.y, 0, 46, this.boxWidth, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
            this.mouseDragged(mc, mouseX, mouseY);
            int color = this.stringColor;

            if (packedFGColour != 0) {
                color = packedFGColour;
            } else if (!this.enabled) {
                color = 10526880;
            }

            if (this.isChecked()) {
                this.drawCenteredString(mc.fontRenderer, "x", this.x + this.boxWidth / 2 + 1, this.y + 1, 14737632);
            }

            if (this.withShadow) {
                this.drawString(mc.fontRenderer, displayString, x + this.boxWidth + 2, y + 2, color);
            } else {
                mc.fontRenderer.drawString(displayString, x + this.boxWidth + 2, y + 2, color);
            }
        }
    }

}
