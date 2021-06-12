package com.wuest.prefab.gui.controls;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.awt.*;

/**
 * @author WuestMan
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class GuiCheckBox extends CheckboxButton {
    protected int boxWidth;
    protected int stringColor;
    protected boolean withShadow;
    protected Minecraft mineCraft;
    protected String displayString;
    protected IPressable handler;
    protected int labelWidth;

    public GuiCheckBox(int xPos, int yPos, String displayString, boolean isChecked, IPressable handler) {
        super(xPos, yPos, 11, 12, Utils.createTextComponent(displayString), isChecked);

        this.boxWidth = 11;
        this.mineCraft = Minecraft.getInstance();
        this.displayString = displayString;
        this.stringColor = Color.DARK_GRAY.getRGB();
        this.handler = handler;
        this.withShadow = false;
        this.labelWidth = 98;
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

    @Override
    public void onPress() {
        super.onPress();

        if (this.handler != null) {
            this.handler.onPress(this);
        }
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

    /**
     * Draws this button to the screen.
     */
    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partial) {
        if (this.visible) {
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.boxWidth && mouseY < this.y + this.height;
            GuiUtils.drawContinuousTexturedBox(
                    WIDGETS_LOCATION,
                    this.x,
                    this.y,
                    0,
                    46,
                    this.boxWidth,
                    this.height,
                    200,
                    20,
                    2,
                    3,
                    2,
                    2,
                    this.getBlitOffset());

            int color = this.stringColor;

            if (!this.active) {
                color = 10526880;
            }

            if (this.selected()) {
                this.drawCenteredString(matrixStack, this.mineCraft.font, "x", this.x + this.boxWidth / 2 + 1, this.y + 1, 14737632);
            }

            if (this.withShadow) {
                this.drawString(matrixStack, this.mineCraft.font, displayString, x + this.boxWidth + 2, y + 2, color);
            } else {
                this.mineCraft.font.drawWordWrap(Utils.createTextComponent(displayString), x + this.boxWidth + 2, y + 2, this.labelWidth, color);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface IPressable {
        void onPress(GuiCheckBox p_onPress_1_);
    }
}
