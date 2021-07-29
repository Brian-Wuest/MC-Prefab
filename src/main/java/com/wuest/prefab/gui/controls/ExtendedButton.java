package com.wuest.prefab.gui.controls;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class ExtendedButton extends Button
{
    public float fontScale = 1;

    public ExtendedButton(int xPos, int yPos, int width, int height, ITextComponent displayString, IPressable handler)
    {
        super(xPos, yPos, width, height, displayString, handler);
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void renderButton(MatrixStack mStack, int mouseX, int mouseY, float partial)
    {
        if (this.visible)
        {
            Minecraft mc = Minecraft.getInstance();
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int k = this.getYImage(this.isHovered());
            GuiUtils.drawContinuousTexturedBox(mStack, WIDGETS_LOCATION, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.getBlitOffset());
            this.renderBg(mStack, mc, mouseX, mouseY);

            ITextComponent buttonText = this.getMessage();
            int strWidth = mc.font.width(buttonText);
            int ellipsisWidth = mc.font.width("...");

            if (strWidth > width - 6 && strWidth > ellipsisWidth)
                //TODO, srg names make it hard to figure out how to append to an ITextProperties from this trim operation, wraping this in StringTextComponent is kinda dirty.
                buttonText = new StringTextComponent(mc.font.substrByWidth(buttonText, width - 6 - ellipsisWidth).getString() + "...");

            MatrixStack originalStack = new MatrixStack();

            originalStack.pushPose();
            originalStack.scale(this.fontScale, this.fontScale, this.fontScale);

            int xPosition = (int) ((this.x + this.width / 2) / this.fontScale);
            int yPosition = (int) ((this.y + (this.height - (8 * this.fontScale)) / 2) / this.fontScale);

            AbstractGui.drawCenteredString(mStack, mc.font, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, getFGColor());

            originalStack.popPose();
        }
    }
}
