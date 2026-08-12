package com.prefab.gui;

import com.prefab.Utils;
import com.prefab.gui.controls.ExtendedButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;

public class GuiUtils {
    private static final RenderBuffers renderBuffers = initRenderBuffers();

    private static RenderBuffers initRenderBuffers() {
        int i = Runtime.getRuntime().availableProcessors();
        int j = Math.min(i, 4);
        return new RenderBuffers(j);
    }

    public static void bindAndDrawTexture(Identifier resourceLocation, GuiGraphicsExtractor guiGraphics, int x, int y,
                                          int z, int width, int height, int textureWidth, int textureHeight) {
        //GuiUtils.bindTexture(resourceLocation);
        GuiUtils.drawTexture(resourceLocation, guiGraphics, x, y, z, width, height, textureWidth, textureHeight);
    }


    public static void bindAndDrawScaledTexture(Identifier resourceLocation, GuiGraphicsExtractor guiGraphics, int x,
                                                int y, int width, int height, int regionWidth, int regionHeight,
                                                int textureWidth, int textureHeight) {
        //GuiUtils.bindTexture(resourceLocation);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, resourceLocation,
                x, y, 0, 0, width, height, textureWidth, textureHeight,
                regionWidth, regionHeight);
    }

    /**
     * Binds a texture to the texture manager for rendering.
     *
     * @param resourceLocation The resource location to bind.
     */
    public static void bindTexture(Identifier resourceLocation) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        AbstractTexture abstractTexture = textureManager.getTexture(resourceLocation);
        //abstractTexture.setUseMipmaps(false);
        //RenderSystem.setShaderTexture(0, abstractTexture.getTextureView());
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
    public static void drawTexture(Identifier resourceLocation, GuiGraphicsExtractor guiGraphics, int x, int y,
                                   int z, int width, int height, int textureWidth, int textureHeight) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, resourceLocation,
                x, y, 0, 0, width, height, textureWidth, textureHeight,
                textureWidth, textureHeight);
    }


    public static void setButtonText(ExtendedButton button, String message) {
        button.setMessage(Utils.createTextComponent(message));
    }

}
