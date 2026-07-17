package com.prefab.structures.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.text2speech.Narrator;
import com.prefab.ClientModRegistryBase;
import com.prefab.PrefabBase;
import com.prefab.blocks.BlockStructureScanner;
import com.prefab.config.StructureScannerConfig;
import com.prefab.gui.GuiLangKeys;
import com.prefab.structures.base.BuildClear;
import com.prefab.structures.base.BuildShape;
import com.prefab.structures.base.Structure;
import com.prefab.structures.config.StructureConfiguration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;

@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public class StructureRenderHandler {
    // player's overlapping on structures and other things.
    public static StructureConfiguration currentConfiguration;
    public static Structure currentStructure;
    public static boolean showedMessage = false;
    private static int dimension;
    private static Minecraft mcInstance;

    /**
     * Resets the structure to show in the world.
     *
     * @param structure     The structure to show in the world, pass null to clear out the client.
     * @param configuration The configuration for this structure.
     */
    public static void setStructure(Structure structure, StructureConfiguration configuration) {
        StructureRenderHandler.currentStructure = structure;
        StructureRenderHandler.currentConfiguration = configuration;
        StructureRenderHandler.showedMessage = false;
        StructureRenderHandler.mcInstance = Minecraft.getInstance();

        if (StructureRenderHandler.mcInstance.level != null) {
            StructureRenderHandler.dimension = StructureRenderHandler.mcInstance.level.dimensionType().logicalHeight();
        }
    }

    public static void renderStructureStartPositionBox(Level worldIn, PoseStack matrixStack,
                                                       MultiBufferSource.BufferSource multiBufferSource,
                                                       float cameraX, float cameraY, float cameraZ) {
        if (StructureRenderHandler.currentStructure != null
                && StructureRenderHandler.dimension == Minecraft.getInstance().player.level().dimensionType().logicalHeight()
                && StructureRenderHandler.currentConfiguration != null
                && PrefabBase.serverConfiguration.enableStructurePreview) {
            BlockPos originalPos = StructureRenderHandler.currentConfiguration.pos.above();

            float blockXOffset = originalPos.getX();
            float blockZOffset = originalPos.getZ();
            float blockStartYOffset = originalPos.getY();

            StructureRenderHandler.drawBox(
                    matrixStack,
                    multiBufferSource,
                    blockXOffset,
                    blockZOffset,
                    blockStartYOffset,
                    cameraX,
                    cameraY,
                    cameraZ,
                    1,
                    1,
                    1);
        }
    }

    public static void drawBox(
            PoseStack matrixStack,
            MultiBufferSource multiBufferSource,
            float blockXOffset,
            float blockZOffset,
            float blockStartYOffset,
            float cameraX, float cameraY, float cameraZ,
            int xLength, int zLength, int height) {
        StructureRenderHandler.drawBox(matrixStack, multiBufferSource, blockXOffset, blockZOffset, blockStartYOffset,
                cameraX, cameraY, cameraZ, xLength, zLength, height, 1.0F, 1.0F, 0.0F, 1.0F, 2.0);
    }

    public static void drawBox(
            PoseStack matrixStack,
            MultiBufferSource multiBufferSource,
            float blockXOffset,
            float blockZOffset,
            float blockStartYOffset,
            float cameraX, float cameraY, float cameraZ,
            int xLength, int zLength, int height,
            float r, float g, float b, float a,
            double lineThickness) {

        Matrix4f matrix4f = matrixStack.last().pose();

        if (r <= -1.0F || r > 1.0F) {
            r = 1.0F;
        }

        if (g <= -1.0F || g > 1.0F) {
            g = 1.0F;
        }

        if (b <= -1.0F || b > 1.0F) {
            b = 0.0F;
        }

        if (a <= -1.0F || a > 1.0F) {
            a = 1.0F;
        }

        float translatedX = blockXOffset - cameraX;
        float translatedY = (float) (blockStartYOffset - cameraY + .02);
        float translatedYEnd = (float) (translatedY + height - .02);
        float translatedZ = blockZOffset - cameraZ;
        RenderType renderType = RenderType.debugLineStrip(lineThickness);

        // Draw the verticals of the box.
        VertexConsumer bufferBuilder = multiBufferSource.getBuffer(renderType);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedY, translatedZ).setColor(r, g, b, a);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedYEnd, translatedZ).setColor(r, g, b, a);

        bufferBuilder = multiBufferSource.getBuffer(renderType);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedY, translatedZ).setColor(r, g, b, a);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedYEnd, translatedZ).setColor(r, g, b, a);

        bufferBuilder = multiBufferSource.getBuffer(renderType);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedY, translatedZ + zLength).setColor(r, g, b, a);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedYEnd, translatedZ + zLength).setColor(r, g, b, a);

        bufferBuilder = multiBufferSource.getBuffer(renderType);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedY, translatedZ + zLength).setColor(r, g, b, a);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedYEnd, translatedZ + zLength).setColor(r, g, b, a);

        // Draw bottom horizontals.
        bufferBuilder = multiBufferSource.getBuffer(renderType);

        bufferBuilder.addVertex(matrix4f, translatedX, translatedY, translatedZ).setColor(r, g, b, a);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedY, translatedZ + zLength).setColor(r, g, b, a);

        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedY, translatedZ + zLength).setColor(r, g, b, a);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedY, translatedZ).setColor(r, g, b, a);

        bufferBuilder.addVertex(matrix4f, translatedX, translatedY, translatedZ).setColor(r, g, b, a);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedY, translatedZ).setColor(r, g, b, a);

        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedY, translatedZ + zLength).setColor(r, g, b, a);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedY, translatedZ + zLength).setColor(r, g, b, a);

        // Draw top horizontals
        bufferBuilder = multiBufferSource.getBuffer(renderType);

        bufferBuilder.addVertex(matrix4f, translatedX, translatedYEnd, translatedZ).setColor(r, g, b, a);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedYEnd, translatedZ + zLength).setColor(r, g, b, a);

        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedYEnd, translatedZ + zLength).setColor(r, g, b, a);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedYEnd, translatedZ).setColor(r, g, b, a);

        bufferBuilder.addVertex(matrix4f, translatedX, translatedYEnd, translatedZ).setColor(r, g, b, a);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedYEnd, translatedZ).setColor(r, g, b, a);

        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedYEnd, translatedZ + zLength).setColor(r, g, b, a);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedYEnd, translatedZ + zLength).setColor(r, g, b, a);
    }

    public static void renderScanningBoxes(PoseStack matrixStack,
                                           MultiBufferSource multiBufferSource,
                                           float cameraX, float cameraY, float cameraZ) {
        for (int i = 0; i < ClientModRegistryBase.structureScanners.size(); i++) {
            StructureScannerConfig config = ClientModRegistryBase.structureScanners.get(i);

            BlockPos pos = config.blockPos;
            boolean removeConfig = pos == null;

            // Make sure the block exists in the world at the block pos.
            if (pos != null) {
                removeConfig = !(Minecraft.getInstance().level.getBlockState(pos.relative(Direction.UP)).getBlock() instanceof BlockStructureScanner);
            }

            if (removeConfig) {
                ClientModRegistryBase.structureScanners.remove(i);
                i--;
                continue;
            }

            Direction leftDirection = config.direction.getCounterClockWise();

            BlockPos startingPosition = config.blockPos
                    .relative(leftDirection, config.blocksToTheLeft)
                    .relative(Direction.DOWN, config.blocksDown)
                    .relative(config.direction, config.blocksParallel);

            int xLength = config.blocksWide;
            int zLength = config.blocksLong;

            switch (config.direction) {
                case Direction.NORTH: {
                    zLength = -zLength;
                    startingPosition = startingPosition.relative(config.direction.getOpposite());
                    break;
                }

                case Direction.EAST: {
                    int tempWidth = xLength;
                    xLength = zLength;
                    zLength = tempWidth;
                    break;
                }

                case Direction.SOUTH: {
                    xLength = -xLength;
                    startingPosition = startingPosition.relative(config.direction.getCounterClockWise());
                    break;
                }

                case Direction.WEST: {
                    int tempLength = zLength;
                    zLength = -xLength;
                    xLength = -tempLength;

                    startingPosition = startingPosition.relative(config.direction.getOpposite());
                    startingPosition = startingPosition.relative(config.direction.getCounterClockWise());
                    break;
                }
            }

            StructureRenderHandler.drawBox(
                    matrixStack,
                    multiBufferSource,
                    startingPosition.getX(),
                    startingPosition.getZ(),
                    startingPosition.getY(),
                    cameraX,
                    cameraY,
                    cameraZ,
                    xLength,
                    zLength,
                    config.blocksTall);
        }
    }

    public static void renderDynamicallySizedOutlineBox(PoseStack matrixStack,
                                                        MultiBufferSource.BufferSource bufferSource,
                                                        float cameraX, float cameraY, float cameraZ,
                                                        BlockPos startingPosition,
                                                        int blocksWide,
                                                        int blocksLong,
                                                        int blocksHigh,
                                                        Direction facing,
                                                        float r,
                                                        float g,
                                                        float b,
                                                        float a) {

        int xLength = blocksWide;
        int zLength = blocksLong;

        switch (facing) {
            case Direction.NORTH: {
                zLength = -zLength;
                startingPosition = startingPosition.relative(facing.getOpposite());
                break;
            }

            case Direction.EAST: {
                int tempWidth = xLength;
                xLength = zLength;
                zLength = tempWidth;
                break;
            }

            case Direction.SOUTH: {
                xLength = -xLength;
                startingPosition = startingPosition.relative(facing.getCounterClockWise());
                break;
            }

            case Direction.WEST: {
                int tempLength = zLength;
                zLength = -xLength;
                xLength = -tempLength;

                startingPosition = startingPosition.relative(facing.getOpposite());
                startingPosition = startingPosition.relative(facing.getCounterClockWise());
                break;
            }
        }

        StructureRenderHandler.drawBox(
                matrixStack,
                bufferSource,
                startingPosition.getX(),
                startingPosition.getZ(),
                startingPosition.getY(),
                cameraX,
                cameraY,
                cameraZ,
                xLength,
                zLength,
                blocksHigh,
                r,
                g,
                b,
                a,
                2.5);

    }

    public static void renderPreview(Player player,
                                     MultiBufferSource.BufferSource bufferSource,
                                     PoseStack matrixStack,
                                     float cameraX, float cameraY, float cameraZ) {
        if (StructureRenderHandler.currentStructure != null
                && StructureRenderHandler.dimension == player.level().dimensionType().logicalHeight()
                && StructureRenderHandler.currentConfiguration != null
                && PrefabBase.serverConfiguration.enableStructurePreview) {
            try {
                BlockPos originalPos = StructureRenderHandler.currentConfiguration.pos;
                Structure structure = StructureRenderHandler.currentStructure;
                StructureConfiguration configuration = StructureRenderHandler.currentConfiguration;
                Direction originalFacing = configuration.houseFacing;
                BuildClear buildClear = structure.getClearSpace();
                BuildShape buildShape = buildClear.getShape();

                BlockPos startBlockPos = buildClear.getStartingPosition()
                        .getRelativePosition(originalPos, buildShape.getDirection(), originalFacing);

                StructureRenderHandler.renderDynamicallySizedOutlineBox(
                        matrixStack,
                        bufferSource,
                        cameraX,
                        cameraY,
                        cameraZ,
                        startBlockPos,
                        buildShape.getWidth(),
                        buildShape.getLength(),
                        buildShape.getHeight(),
                        originalFacing.getOpposite(),
                        1.0F,
                        1.0F,
                        0.0F,
                        1.0F);

                if (!StructureRenderHandler.showedMessage) {
                    Narrator.getNarrator().clear();

                    MutableComponent message = Component.translatable(GuiLangKeys.GUI_PREVIEW_NOTICE);
                    message.setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));

                    StructureRenderHandler.mcInstance.gui.getChat().addMessage(message);

                    message = Component.translatable(GuiLangKeys.GUI_BLOCK_CLICKED);
                    message.setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW));
                    StructureRenderHandler.mcInstance.gui.getChat().addMessage(message);

                    StructureRenderHandler.showedMessage = true;
                }
            } catch (Exception ex) {
                PrefabBase.logger.error("Error during structure preview rendering.", ex);
            }
        }
    }
}