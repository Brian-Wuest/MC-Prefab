package com.wuest.prefab.gui.controls;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wuest.prefab.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

/**
 * @author WuestMan
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class GuiCheckBox extends AbstractButton {
    private static final ResourceLocation buttonTexture = new ResourceLocation("prefab", "textures/gui/prefab_checkbox.png");
    private static final ResourceLocation buttonTexturePressed = new ResourceLocation("prefab", "textures/gui/prefab_checkbox_selected.png");
    private static final ResourceLocation buttonTextureHover = new ResourceLocation("prefab", "textures/gui/prefab_checkbox_hover.png");
    private static final ResourceLocation buttonTextureHoverSelected = new ResourceLocation("prefab", "textures/gui/prefab_checkbox_hover_selected.png");

    protected int boxWidth;
    protected int boxHeight;
    protected int stringColor;
    protected boolean withShadow;
    protected Minecraft mineCraft;
    protected String displayString;
    protected IPressable handler;
    protected int labelWidth;
    protected boolean isChecked;

    public GuiCheckBox(int xPos, int yPos, String displayString, boolean isChecked, IPressable handler) {
        super(xPos, yPos, 11, 12, Utils.createTextComponent(displayString));

        this.width = 22;
        this.height = 14;
        this.boxHeight = 14;
        this.boxWidth = 22;
        this.mineCraft = Minecraft.getInstance();
        this.displayString = displayString;
        this.stringColor = Color.DARK_GRAY.getRGB();
        this.handler = handler;
        this.withShadow = false;
        this.labelWidth = 98;
        this.isChecked = isChecked;
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
        this.isChecked = !this.isChecked;

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

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partial) {
        if (this.visible) {
            ResourceLocation resourceLocation = GuiCheckBox.buttonTexture;
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.boxWidth && mouseY < this.y + this.height;

            if (this.isChecked()) {
                resourceLocation = GuiCheckBox.buttonTexturePressed;

                if (this.isHovered) {
                    resourceLocation = GuiCheckBox.buttonTextureHoverSelected;
                }
            } else if (this.isHovered) {
                resourceLocation = GuiCheckBox.buttonTextureHover;
            }

            com.wuest.prefab.gui.GuiUtils.bindTexture(resourceLocation);
            com.wuest.prefab.gui.GuiUtils.drawTexture(matrixStack, this.x, this.y, 1, this.boxWidth, this.boxHeight, this.boxWidth, this.boxHeight);

            int color = this.stringColor;

            if (this.withShadow) {
                this.drawString(matrixStack, this.mineCraft.font, displayString, x + this.boxWidth + 2, y + 3, color);
            } else {
                this.mineCraft.font.drawWordWrap(Utils.createTextComponent(displayString), x + this.boxWidth + 2, y + 3, this.labelWidth, color);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface IPressable {
        void onPress(GuiCheckBox p_onPress_1_);
    }
}
