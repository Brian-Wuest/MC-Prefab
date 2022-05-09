package com.wuest.prefab.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.wuest.prefab.Utils;
import com.wuest.prefab.gui.controls.ExtendedButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GuiUtils {

    /**
     * Binds a texture to the current texture manager or render system.
     *
     * @param resourceLocation The resource to bind.
     */
    public static void bindTexture(ResourceLocation resourceLocation) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, resourceLocation);
    }

    /**
     * Draws a textured rectangle Args: x, y, z, width, height, textureWidth, textureHeight
     *
     * @param x             The X-Axis screen coordinate.
     * @param y             The Y-Axis screen coordinate.
     * @param z             The Z-Axis screen coordinate.
     * @param width         The width of the rectangle.
     * @param height        The height of the rectangle.
     * @param textureWidth  The width of the texture.
     * @param textureHeight The height of the texture.
     */
    public static void drawTexture(PoseStack matrixStack, int x, int y, int z, int width, int height, int textureWidth, int textureHeight) {
        GuiComponent.blit(matrixStack, x, y, z, 0, 0, width, height, textureWidth, textureHeight);
    }

    public static void drawContinuousTexturedBox(ResourceLocation res, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
                                                 int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel) {
        GuiUtils.bindTexture(res);
        GuiUtils.drawContinuousTexturedBox(x, y, u, v, width, height, textureWidth, textureHeight, topBorder, bottomBorder, leftBorder, rightBorder, zLevel);
    }

    public static void drawContinuousTexturedBox(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
                                                 int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

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
        GuiUtils.drawTexturedModalRect(x, y, u, v, leftBorder, topBorder, zLevel);

        // Top Right
        GuiUtils.drawTexturedModalRect(x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder, zLevel);

        // Bottom Left
        GuiUtils.drawTexturedModalRect(x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder, zLevel);

        // Bottom Right
        GuiUtils.drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder, zLevel);

        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); i++) {
            // Top Border
            GuiUtils.drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y, u + leftBorder, v, (i == xPasses ? remainderWidth : fillerWidth), topBorder, zLevel);

            // Bottom Border
            GuiUtils.drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder, zLevel);

            // Throw in some filler for good measure
            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
                GuiUtils.drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight), zLevel);
            }
        }

        // Side Borders
        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
            // Left Border
            GuiUtils.drawTexturedModalRect(x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);

            // Right Border
            GuiUtils.drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, rightBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }
    }

    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel) {
        final float uScale = 1f / 0x100;
        final float vScale = 1f / 0x100;

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder wr = tessellator.getBuilder();
        wr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        wr.vertex(x, y + height, zLevel).uv(u * uScale, ((v + height) * vScale)).endVertex();
        wr.vertex(x + width, y + height, zLevel).uv((u + width) * uScale, ((v + height) * vScale)).endVertex();
        wr.vertex(x + width, y, zLevel).uv((u + width) * uScale, (v * vScale)).endVertex();
        wr.vertex(x, y, zLevel).uv(u * uScale, (v * vScale)).endVertex();
        tessellator.end();
    }

    public static void bindAndDrawTexture(ResourceLocation resourceLocation, PoseStack matrixStack, int x, int y, int z, int width, int height, int textureWidth, int textureHeight) {
        GuiUtils.bindTexture(resourceLocation);
        GuiUtils.drawTexture(matrixStack, x, y, z, width, height, textureWidth, textureHeight);
    }

    public static void bindAndDrawScaledTexture(ResourceLocation resourceLocation, PoseStack matrixStack, int x, int y, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        GuiUtils.bindTexture(resourceLocation);
        GuiUtils.bindAndDrawScaledTexture(matrixStack, x, y, width, height, regionWidth, regionHeight, textureWidth, textureHeight);
    }

    public static void bindAndDrawScaledTexture(PoseStack matrixStack, int x, int y, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        // This is "drawTexture" in fabric.
        GuiComponent.blit(matrixStack, x, y, width, height, 0, 0, regionWidth, regionHeight, textureWidth, textureHeight);
    }

    public static void setButtonText(ExtendedButton button, String message) {
        button.setMessage(Utils.createTextComponent(message));
    }

    /**
     * Draws the background icon for an item, using a texture from stats.png with the given coords
     */
    public static void drawItemBackground(int x, int y) {
        GuiUtils.bindTexture(GuiComponent.STATS_ICON_LOCATION);
        float f = 0.0078125F;
        int i = 18;
        int j = 18;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder vertexBuffer = tesselator.getBuilder();
        vertexBuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        vertexBuffer.vertex(x, (y + i), 0).uv(0, ((float) j * f)).endVertex();
        vertexBuffer.vertex((x + i), (y + j), 0).uv(((float) j * f), ((float) j * 0.0078125F)).endVertex();
        vertexBuffer.vertex((x + i), y, 0).uv(((float) j * f), 0).endVertex();
        vertexBuffer.vertex(x, y, 0).uv(0, 0).endVertex();
        tesselator.end();
    }

    /**
     * Draws a string on the screen.
     *
     * @param text  The text to draw.
     * @param x     The X-Coordinates of the string to start.
     * @param y     The Y-Coordinates of the string to start.
     * @param color The color of the text.
     * @return Some integer value.
     */
    public static int drawString(PoseStack matrixStack, String text, float x, float y, int color) {
        return GuiUtils.getFontRenderer().draw(matrixStack, text, x, y, color);
    }

    /**
     * Draws a string on the screen with word wrapping.
     *
     * @param str       The text to draw.
     * @param x         The X-Coordinates of the string to start.
     * @param y         The Y-Coordinates of the string to start.
     * @param wrapWidth The maximum width before wrapping begins.
     * @param textColor The color of the text.
     */
    public static void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
        GuiUtils.getFontRenderer().drawWordWrap(Utils.createTextComponent(str), x, y, wrapWidth, textColor);
    }

    public static List<FormattedCharSequence> getSplitString(String str, int wrapWidth) {
        return GuiUtils.getFontRenderer().split(Utils.createTextComponent(str), wrapWidth);
    }

    public static List<FormattedCharSequence> getSplitString(FormattedText str, int wrapWidth) {
        return GuiUtils.getFontRenderer().split(str, wrapWidth);
    }

    public static Font getFontRenderer() {
        return GuiUtils.getMinecraft().font;
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public static void fillGradient(Matrix4f matrix4f, BufferBuilder bufferBuilder, int i, int j, int k, int l, int m, int n, int o) {
        float f = (float) (n >> 24 & 255) / 255.0F;
        float g = (float) (n >> 16 & 255) / 255.0F;
        float h = (float) (n >> 8 & 255) / 255.0F;
        float p = (float) (n & 255) / 255.0F;
        float q = (float) (o >> 24 & 255) / 255.0F;
        float r = (float) (o >> 16 & 255) / 255.0F;
        float s = (float) (o >> 8 & 255) / 255.0F;
        float t = (float) (o & 255) / 255.0F;
        bufferBuilder.vertex(matrix4f, (float) k, (float) j, (float) m).color(g, h, p, f).endVertex();
        bufferBuilder.vertex(matrix4f, (float) i, (float) j, (float) m).color(g, h, p, f).endVertex();
        bufferBuilder.vertex(matrix4f, (float) i, (float) l, (float) m).color(r, s, t, q).endVertex();
        bufferBuilder.vertex(matrix4f, (float) k, (float) l, (float) m).color(r, s, t, q).endVertex();
    }

    public static void renderTooltip(PoseStack poseStack, ItemStack itemStack, int mouseX, int mouseY, int screenHeight, int screenWidth) {
        renderTooltip(poseStack, getTooltipFromItem(itemStack), itemStack.getTooltipImage(), mouseX, mouseY, screenHeight, screenWidth);
    }

    public static void renderTooltip(PoseStack poseStack, List<Component> list, Optional<TooltipComponent> optional, int mouseX, int mouseY, int screenHeight, int screenWidth) {
        List<ClientTooltipComponent> clientTooltipComponents = list.stream()
                .map(Component::getVisualOrderText)
                .map(ClientTooltipComponent::create).collect(Collectors.toList());

        optional.ifPresent((tooltipComponent) -> {
            clientTooltipComponents.add(1, ClientTooltipComponent.create(tooltipComponent));
        });

        GuiUtils.renderTooltipInternal(poseStack, clientTooltipComponents, mouseX, mouseY, screenHeight, screenWidth);
    }

    public static List<Component> getTooltipFromItem(ItemStack itemStack) {
        return itemStack.getTooltipLines(
                GuiUtils.getMinecraft().player,
                GuiUtils.getMinecraft().options.advancedItemTooltips
                        ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
    }

    private static void renderTooltipInternal(PoseStack poseStack, List<ClientTooltipComponent> list, int mouseX, int mouseY, int screenHeight, int screenWidth) {
        if (!list.isEmpty()) {
            int k = 0;
            int l = list.size() == 1 ? -2 : 0;

            ClientTooltipComponent clientTooltipComponent;
            for (Iterator var7 = list.iterator(); var7.hasNext(); l += clientTooltipComponent.getHeight()) {
                clientTooltipComponent = (ClientTooltipComponent) var7.next();
                int m = clientTooltipComponent.getWidth(GuiUtils.getFontRenderer());
                if (m > k) {
                    k = m;
                }
            }

            int n = mouseX + 12;
            int o = mouseY - 12;
            if (n + k > screenWidth) {
                n -= 28 + k;
            }

            if (o + l + 6 > screenHeight) {
                o = screenHeight - l - 6;
            }

            poseStack.pushPose();

            float f = GuiUtils.getMinecraft().getItemRenderer().blitOffset;
            GuiUtils.getMinecraft().getItemRenderer().blitOffset = 400.0F;
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferBuilder = tesselator.getBuilder();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            Matrix4f matrix4f = poseStack.last().pose();
            fillGradient(matrix4f, bufferBuilder, n - 3, o - 4, n + k + 3, o - 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, n - 3, o + l + 3, n + k + 3, o + l + 4, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, n - 3, o - 3, n + k + 3, o + l + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, n - 4, o - 3, n - 3, o + l + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, n + k + 3, o - 3, n + k + 4, o + l + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, n - 3, o - 3 + 1, n - 3 + 1, o + l + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient(matrix4f, bufferBuilder, n + k + 2, o - 3 + 1, n + k + 3, o + l + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient(matrix4f, bufferBuilder, n - 3, o - 3, n + k + 3, o - 3 + 1, 400, 1347420415, 1347420415);
            fillGradient(matrix4f, bufferBuilder, n - 3, o + l + 2, n + k + 3, o + l + 3, 400, 1344798847, 1344798847);
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            bufferBuilder.end();
            BufferUploader.end(bufferBuilder);
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            poseStack.translate(0.0D, 0.0D, 400.0D);
            int u = o;

            int v;
            ClientTooltipComponent clientTooltipComponent2;
            for (v = 0; v < list.size(); ++v) {
                clientTooltipComponent2 = list.get(v);
                clientTooltipComponent2.renderText(GuiUtils.getFontRenderer(), n, u, matrix4f, bufferSource);
                u += clientTooltipComponent2.getHeight() + (v == 0 ? 2 : 0);
            }

            bufferSource.endBatch();
            poseStack.popPose();
            u = o;

            for (v = 0; v < list.size(); ++v) {
                clientTooltipComponent2 = list.get(v);
                clientTooltipComponent2.renderImage(GuiUtils.getFontRenderer(), n, u, poseStack, GuiUtils.getMinecraft().getItemRenderer(), 400);
                u += clientTooltipComponent2.getHeight() + (v == 0 ? 2 : 0);
            }

            GuiUtils.getMinecraft().getItemRenderer().blitOffset = f;
        }
    }
}
