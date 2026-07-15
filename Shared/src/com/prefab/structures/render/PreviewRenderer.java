package com.prefab.structures.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.text2speech.Narrator;
import com.prefab.PrefabBase;
import com.prefab.PrefabClientBase;
import com.prefab.gui.GuiLangKeys;
import com.prefab.structures.base.Structure;
import com.prefab.structures.config.StructureConfiguration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PreviewRenderer {
    public static StructureConfiguration currentConfiguration;
    public static Structure currentStructure;
    public static boolean showedMessage = false;
    private static Minecraft mcInstance;
    private static int dimension;
    private static GpuTexture gpuTexture;
    private static GpuTextureView gpuTextureView;

    public static void setStructure(Structure structure, StructureConfiguration configuration) {
        PreviewRenderer.currentStructure = structure;
        PreviewRenderer.currentConfiguration = configuration;
        PreviewRenderer.showedMessage = false;
        //PreviewRenderer.needsRebuild = true;

        PreviewRenderer.mcInstance = Minecraft.getInstance();

        // Re-initialize GPU resources on setStructure call to ensure clean state for the new structure.
        if (gpuTexture != null) {
            gpuTexture.close();
            gpuTextureView.close();
        }
        PreviewRenderer.gpuTexture = RenderSystem.getDevice().createTexture("Structure Preview", 12, TextureFormat.RGBA8, 16, 16, 1, 1);
        PreviewRenderer.gpuTextureView = RenderSystem.getDevice().createTextureView(PreviewRenderer.gpuTexture);

        if (PreviewRenderer.mcInstance.level != null) {
            PreviewRenderer.dimension = PreviewRenderer.mcInstance.level.dimensionType().logicalHeight();
        }
    }

    public static void renderPreview(Player player,
                                     MultiBufferSource.BufferSource bufferSource,
                                     PoseStack matrixStack,
                                     float cameraX, float cameraY, float cameraZ) {
        if (PreviewRenderer.currentStructure != null
                && PreviewRenderer.dimension == player.level().dimensionType().logicalHeight()
                && PreviewRenderer.currentConfiguration != null
                && PrefabBase.serverConfiguration.enableStructurePreview) {
            try {
                BlockPos originalPos = PreviewRenderer.currentConfiguration.pos;
                BlockPos testBlockPos = originalPos.above().west();
                BlockState blockState = Blocks.REDSTONE_BLOCK.defaultBlockState();
                PoseStack poseStack = new PoseStack();

                BlockRenderDispatcher blockRenderer = PreviewRenderer.mcInstance.getBlockRenderer();
                VertexConsumer bufferBuilder = bufferSource.getBuffer(RenderType.entityTranslucent(TextureAtlas.LOCATION_BLOCKS));

                BlockStateModel model = blockRenderer.getBlockModel(blockState);

                int color = PreviewRenderer.mcInstance.getBlockColors()
                        .getColor(blockState, null, null, 0);
                float r = (float) (color >> 16 & 255) / 255.0F;
                float g = (float) (color >> 8 & 255) / 255.0F;
                float b = (float) (color & 255) / 255.0F;

                poseStack.pushPose();
                poseStack.translate(-cameraX, -cameraY, -cameraZ);
                poseStack.translate(testBlockPos.getX(), testBlockPos.getY(), testBlockPos.getZ());

                PreviewRenderer.renderModel(
                        poseStack.last(),
                        bufferBuilder,
                        model,
                        r, g, b,
                        0xF000F0,
                        OverlayTexture.NO_OVERLAY);

                poseStack.popPose();
                PreviewRenderer.showStructureMessage();
            } catch (Exception ex) {
                PrefabBase.logger.error("Error during structure preview rendering.", ex);
            }
        }
    }

    public static void renderModel(PoseStack.Pose pose, VertexConsumer vertexConsumer, BlockStateModel blockStateModel, float f, float g, float h, int i, int j) {
        for (BlockModelPart blockModelPart : blockStateModel.collectParts(RandomSource.create(42L))) {
            for (Direction direction : Direction.values()) {
                PreviewRenderer.renderQuadList(pose, vertexConsumer, f, g, h, blockModelPart.getQuads(direction), i, j);
            }
            PreviewRenderer.renderQuadList(pose, vertexConsumer, f, g, h, blockModelPart.getQuads(null), i, j);
        }
    }

    private static void renderQuadList(PoseStack.Pose pose, VertexConsumer vertexConsumer, float f, float g, float h, List<BakedQuad> list, int i, int j) {
        for (BakedQuad bakedQuad : list) {
            float m;
            float l;
            float k;
            if (bakedQuad.isTinted()) {
                k = Mth.clamp(f, 0.0f, 1.0f);
                l = Mth.clamp(g, 0.0f, 1.0f);
                m = Mth.clamp(h, 0.0f, 1.0f);
            } else {
                k = 1.0f;
                l = 1.0f;
                m = 1.0f;
            }
            vertexConsumer.putBulkData(pose, bakedQuad, k, l, m, 0.4f, i, j);
        }
    }

    private static void showStructureMessage() {
        if (!PreviewRenderer.showedMessage) {
            Narrator.getNarrator().clear();

            MutableComponent message = Component.translatable(GuiLangKeys.GUI_PREVIEW_NOTICE);
            message.setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));

            PreviewRenderer.mcInstance.gui.getChat().addMessage(message);

            message = Component.translatable(GuiLangKeys.GUI_BLOCK_CLICKED);
            message.setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW));
            PreviewRenderer.mcInstance.gui.getChat().addMessage(message);

            PreviewRenderer.showedMessage = true;
        }
    }
}
