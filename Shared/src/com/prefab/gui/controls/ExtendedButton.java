package com.prefab.gui.controls;

import com.mojang.blaze3d.vertex.PoseStack;
import com.prefab.Utils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ExtendedButton extends Button {
    public float fontScale = 1;
    private int activeStringColor;
    private int inactiveStringColor;

    public ExtendedButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler, @Nullable String label) {
        super(xPos, yPos, width, height, displayString, handler, Button.DEFAULT_NARRATION);

        this.activeStringColor = Color.WHITE.getRGB();
        this.inactiveStringColor = Color.LIGHT_GRAY.getRGB();
    }

    public int  getActiveStringColor() {
        return activeStringColor;
    }
    public int getInactiveStringColor() {
        return inactiveStringColor;
    }

    public void setInactiveStringColor(int inactiveStringColor) {
        this.inactiveStringColor = inactiveStringColor;
    }

    public void setActiveStringColor(int activeStringColor) {
        this.activeStringColor = activeStringColor;
    }

    @Override
    public void renderString(GuiGraphics guiGraphics, Font font, int i) {
        Component buttonText = this.getMessage();
        int strWidth = font.width(buttonText);
        int ellipsisWidth = font.width("...");

        if (strWidth > width - 6 && strWidth > ellipsisWidth) {
            buttonText = Utils.createTextComponent(font.substrByWidth(buttonText, width - 6 - ellipsisWidth).getString() + "...");
        }

        PoseStack originalStack = new PoseStack();

        originalStack.pushPose();
        originalStack.scale(this.fontScale, this.fontScale, this.fontScale);

        int xPosition = (int) ((this.getX() + this.width / 2));
        int yPosition = (int) ((this.getY() + (this.height - 8) / 2));

        guiGraphics.drawCenteredString(font, buttonText, xPosition, yPosition, this.getFGColor());
        originalStack.popPose();
    }

    public int getFGColor() {
        return this.active ? this.activeStringColor : this.inactiveStringColor; // White : Light Grey
    }
}
