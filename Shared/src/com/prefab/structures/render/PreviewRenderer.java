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
import com.prefab.structures.base.BuildBlock;
import com.prefab.structures.base.Structure;
import com.prefab.structures.config.StructureConfiguration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class PreviewRenderer {
    public static StructureConfiguration currentConfiguration;
    public static Structure currentStructure;
    public static boolean showedMessage = false;
    private static Minecraft mcInstance;
    private static int dimension;
    private static GpuTexture gpuTexture;
    private static GpuTextureView gpuTextureView;
    private static boolean needsRebuild = true;
    private static HashMap<PreviewChunkKey, ArrayList<BakedQuadWithColor>> renderedQuads = new HashMap<>();

    public static void setStructure(Structure structure, StructureConfiguration configuration) {
        PreviewRenderer.currentStructure = structure;
        PreviewRenderer.currentConfiguration = configuration;
        PreviewRenderer.showedMessage = false;
        PreviewRenderer.needsRebuild = true;
        PreviewRenderer.renderedQuads = new HashMap<>();

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
                // Determine if the blocks need to be pre-rendered.
                if (PreviewRenderer.needsRebuild) {
                    PreviewRenderer.rebuildBakedQuads();
                    PreviewRenderer.needsRebuild = false;
                }

                // Actually draw the blocks
                PoseStack poseStack = new PoseStack();
                //VertexConsumer bufferBuilder = bufferSource.getBuffer(RenderType.entityTranslucent(TextureAtlas.LOCATION_BLOCKS));
                VertexConsumer bufferBuilder = bufferSource.getBuffer(PrefabClientBase.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS));

                for (Entry<PreviewChunkKey, ArrayList<BakedQuadWithColor>> entry : renderedQuads.entrySet()) {
                    PreviewChunkKey key = entry.getKey();
                    poseStack.pushPose();
                    poseStack.translate(-cameraX, -cameraY, -cameraZ);
                    poseStack.translate(key.chunkX(), key.chunkY(), key.chunkZ());

                    entry.getValue().forEach(quad -> {
                        bufferBuilder.putBulkData(poseStack.last(), quad.bakedQuad, quad.r, quad.g,
                                quad.b, 0.4f, 0xF000F0, OverlayTexture.NO_OVERLAY);
                    });

                    /*PreviewRenderer.renderModel(
                            poseStack.last(),
                            bufferBuilder,
                            model,
                            r, g, b,
                            0xF000F0,
                            OverlayTexture.NO_OVERLAY);*/

                    poseStack.popPose();
                }

                PreviewRenderer.showStructureMessage();
            } catch (Exception ex) {
                PrefabBase.logger.error("Error during structure preview rendering.", ex);
            }
        }
    }

    private static void rebuildBakedQuads() {
        ArrayList<BuildBlock> buildBlocks = new ArrayList<>();
        BlockRenderDispatcher blockRenderer = PreviewRenderer.mcInstance.getBlockRenderer();

        BlockPos originalPos = PreviewRenderer.currentConfiguration.pos;
        Structure structure = PreviewRenderer.currentStructure;
        Direction structureBuildDirection = structure.getClearSpace().getShape().getDirection();
        StructureConfiguration configuration = PreviewRenderer.currentConfiguration;
        Direction originalFacing = configuration.houseFacing;
        Level level = PreviewRenderer.mcInstance.level;

        for (BuildBlock blockInfo : structure.getBlocks()) {
            BlockPos rotatedPos = blockInfo.getStartingPosition().getRelativePosition(
                    originalPos,
                    structureBuildDirection,
                    originalFacing);

            BlockState state = blockInfo.getBlockState() != null
                    ? blockInfo.getBlockState()
                    : BuiltInRegistries.BLOCK.getValue(blockInfo.getResourceLocation()).defaultBlockState();

            if (state.getRenderShape() != RenderShape.MODEL) {
                continue;
            }

            BuildBlock block = BuildBlock.SetBlockState(
                    configuration,
                    level,
                    originalPos,
                    blockInfo,
                    state.getBlock(),
                    state,
                    structure);

            block.blockPos = rotatedPos;
            buildBlocks.add(block);

            if (blockInfo.getSubBlock() != null) {
                BuildBlock subBlockInfo = blockInfo.getSubBlock();

                BlockState subBlockState = subBlockInfo.getBlockState() != null
                        ? subBlockInfo.getBlockState()
                        : BuiltInRegistries.BLOCK.getValue(
                        subBlockInfo.getResourceLocation()).defaultBlockState();

                // Re-create sub-block object with correct world position for assembly context
                BuildBlock subBlock = BuildBlock.SetBlockState(
                        configuration,
                        level,
                        originalPos,
                        subBlockInfo,
                        subBlockState.getBlock(),
                        subBlockState,
                        structure);

                subBlock.blockPos = subBlockInfo.getStartingPosition().getRelativePosition(
                        originalPos,
                        structureBuildDirection,
                        originalFacing);

                block.setSubBlock(subBlock);
                buildBlocks.add(subBlock);
            }
        }
/*        BuildBlock initialBlock = new BuildBlock();
        initialBlock.blockPos = originalPos.above().west();
        initialBlock.setBlockState(Blocks.REDSTONE_BLOCK.defaultBlockState());
        buildBlocks.add(initialBlock);

        BuildBlock otherBlock = new BuildBlock();
        otherBlock.blockPos = originalPos.above().north();
        otherBlock.setBlockState(Blocks.STONE_BRICKS.defaultBlockState());
        buildBlocks.add(otherBlock);*/

        for (BuildBlock block : buildBlocks) {
            BlockStateModel model = blockRenderer.getBlockModel(block.getBlockState());
            PreviewRenderer.bakeQuads(model, block);
        }
    }

    private static void bakeQuads(BlockStateModel blockStateModel, BuildBlock block) {
        BlockPos pos = block.blockPos;
        ArrayList<BakedQuadWithColor> quads = new ArrayList<>();
        int color = PreviewRenderer.mcInstance.getBlockColors()
                .getColor(block.getBlockState(), null, null, 0);
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        for (BlockModelPart blockModelPart : blockStateModel.collectParts(RandomSource.create(42L))) {
            for (Direction direction : Direction.values()) {
                for (BakedQuad quad : blockModelPart.getQuads(direction)) {
                    BakedQuadWithColor bakedQuadWithColor = PreviewRenderer.getBakedQuad(quad, r, g, b);
                    bakedQuadWithColor.block = block;
                    quads.add(bakedQuadWithColor);
                }
            }

            for (BakedQuad quad : blockModelPart.getQuads(null)) {
                BakedQuadWithColor bakedQuadWithColor = PreviewRenderer.getBakedQuad(quad, r, g, b);
                bakedQuadWithColor.block = block;
                quads.add(bakedQuadWithColor);
            }
        }

        renderedQuads.put(new PreviewChunkKey(pos.getX(), pos.getY(), pos.getZ()), quads);
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

    private static BakedQuadWithColor getBakedQuad(BakedQuad bakedQuad, float r, float g, float b) {
        BakedQuadWithColor bakedQuadWithColor = new BakedQuadWithColor();
        bakedQuadWithColor.bakedQuad = bakedQuad;

        if (bakedQuad.isTinted()) {
            bakedQuadWithColor.r = Mth.clamp(r, 0.0f, 1.0f);
            bakedQuadWithColor.g = Mth.clamp(g, 0.0f, 1.0f);
            bakedQuadWithColor.b = Mth.clamp(b, 0.0f, 1.0f);
        } else {
            bakedQuadWithColor.r = 1.0f;
            bakedQuadWithColor.g = 1.0f;
            bakedQuadWithColor.b = 1.0f;
        }

        return bakedQuadWithColor;
    }

    // Not used.
    public static void renderModel(PoseStack.Pose pose, VertexConsumer vertexConsumer, BlockStateModel blockStateModel, float f, float g, float h, int i, int j) {
        for (BlockModelPart blockModelPart : blockStateModel.collectParts(RandomSource.create(42L))) {
            for (Direction direction : Direction.values()) {
                PreviewRenderer.renderQuadList(pose, vertexConsumer, f, g, h, blockModelPart.getQuads(direction), i, j);
            }
            PreviewRenderer.renderQuadList(pose, vertexConsumer, f, g, h, blockModelPart.getQuads(null), i, j);
        }
    }

    // Not Used
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

    private static class BakedQuadWithColor {
        public BuildBlock block;
        public BakedQuad bakedQuad;
        public float r;
        public float g;
        public float b;
    }
}
