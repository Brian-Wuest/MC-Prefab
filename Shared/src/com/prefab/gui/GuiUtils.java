package com.prefab.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.prefab.Utils;
import com.prefab.gui.controls.ExtendedButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiUtils {
    private static final RenderBuffers renderBuffers = initRenderBuffers();

    private static RenderBuffers initRenderBuffers() {
        int i = Runtime.getRuntime().availableProcessors();
        int j = Math.min(i, 4);
        return new RenderBuffers(j);
    }

    public static RenderBuffers renderBuffers() {
        return renderBuffers;
    }

    /**
     * Binds a texture to the texture manager for rendering.
     *
     * @param resourceLocation The resource location to bind.
     */
    public static void bindTexture(ResourceLocation resourceLocation) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        AbstractTexture abstractTexture = textureManager.getTexture(resourceLocation);
        abstractTexture.setUseMipmaps(false);
        RenderSystem.setShaderTexture(0, abstractTexture.getTextureView());
    }

    /**
     * Draws a textured rectangle Args: x, y, z, width, height, textureWidth, textureHeight
     *
     * @param resourceLocation The resource location of the texture to be rendered.
     * @param x                The X-Axis screen coordinate.
     * @param y                The Y-Axis screen coordinate.
     * @param z                The Z-Axis screen coordinate.
     * @param width            The width of the rectangle.
     * @param height           The height of the rectangle.
     * @param textureWidth     The width of the texture.
     * @param textureHeight    The height of the texture.
     */
    public static void drawTexture(ResourceLocation resourceLocation, GuiGraphics guiGraphics, int x, int y, int z, int width, int height, int textureWidth, int textureHeight) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, resourceLocation, x, y, 0, 0, width, height, textureWidth, textureHeight, textureWidth, textureHeight);
    }

    /**
     * Draws a textured box of any size (smallest size is borderSize * 2 square) based on a fixed size textured box with continuous borders
     * and filler. It is assumed that the desired texture ResourceLocation object has been bound using
     * Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation).
     *
     * @param x             x axis offset
     * @param y             y axis offset
     * @param u             bound resource location image x offset
     * @param v             bound resource location image y offset
     * @param width         the desired box width
     * @param height        the desired box height
     * @param textureWidth  the width of the box texture in the resource location image
     * @param textureHeight the height of the box texture in the resource location image
     * @param topBorder     the size of the box's top border
     * @param bottomBorder  the size of the box's bottom border
     * @param leftBorder    the size of the box's left border
     * @param rightBorder   the size of the box's right border
     */
    public static void drawContinuousTexturedBox(ResourceLocation resourceLocation, GuiGraphics guiGraphics, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
                                                 int topBorder, int bottomBorder, int leftBorder, int rightBorder) {

        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;

        // Draw Border
        // Top Left
        GuiUtils.drawTexturedModalRect(resourceLocation, guiGraphics, x, y, u, v, leftBorder, topBorder);

        // Top Right
        GuiUtils.drawTexturedModalRect(resourceLocation, guiGraphics, x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder);

        // Bottom Left
        GuiUtils.drawTexturedModalRect(resourceLocation, guiGraphics, x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder);

        // Bottom Right
        GuiUtils.drawTexturedModalRect(resourceLocation, guiGraphics, x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder);

        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); i++) {
            // Top Border
            GuiUtils.drawTexturedModalRect(resourceLocation, guiGraphics, x + leftBorder + (i * fillerWidth), y, u + leftBorder, v, (i == xPasses ? remainderWidth : fillerWidth), topBorder);

            // Bottom Border
            GuiUtils.drawTexturedModalRect(resourceLocation, guiGraphics, x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder);

            // Throw in some filler for good measure
            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++)
                GuiUtils.drawTexturedModalRect(resourceLocation, guiGraphics, x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight));
        }

        // Side Borders
        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
            // Left Border
            GuiUtils.drawTexturedModalRect(resourceLocation, guiGraphics, x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder, (j == yPasses ? remainderHeight : fillerHeight));

            // Right Border
            GuiUtils.drawTexturedModalRect(resourceLocation, guiGraphics, x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, rightBorder, (j == yPasses ? remainderHeight : fillerHeight));
        }
    }

    public static void drawTexturedModalRect(ResourceLocation resourceLocation, GuiGraphics guiGraphics, int x, int y, int u, int v, int width, int height) {
        GuiUtils.bindAndDrawTexture(resourceLocation, guiGraphics, x, y, 0, width, height, width, height);
    }

    public static void bindAndDrawTexture(ResourceLocation resourceLocation, GuiGraphics guiGraphics, int x, int y, int z, int width, int height, int textureWidth, int textureHeight) {
        GuiUtils.bindTexture(resourceLocation);
        GuiUtils.drawTexture(resourceLocation, guiGraphics, x, y, z, width, height, textureWidth, textureHeight);
    }

    public static void bindAndDrawScaledTexture(ResourceLocation resourceLocation, GuiGraphics guiGraphics, int x, int y, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        GuiUtils.bindTexture(resourceLocation);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, resourceLocation,
                x, y, 0, 0, width, height, textureWidth, textureHeight,
                regionWidth, regionHeight);
    }

    public static void setButtonText(ExtendedButton button, String message) {
        button.setMessage(Utils.createTextComponent(message));
    }

}
