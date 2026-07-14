package com.prefab.structures.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.mojang.text2speech.Narrator;
import com.prefab.ClientModRegistryBase;
import com.prefab.PrefabBase;
import com.prefab.PrefabClientBase;
import com.prefab.blocks.BlockStructureScanner;
import com.prefab.config.StructureScannerConfig;
import com.prefab.gui.GuiLangKeys;
import com.prefab.mesh.assembly.StructureMeshAssembler;
import com.prefab.mesh.geometry.MeshGeometryCalculator;
import com.prefab.mesh.geometry.PrefabMeshData;
import com.prefab.structures.base.BuildBlock;
import com.prefab.structures.base.Structure;
import com.prefab.structures.config.StructureConfiguration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

import java.util.*;

@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public class StructureRenderHandler {

    // Cached meshes for the current preview structure/orientation
    private static final Map<PreviewChunkKey, PreviewChunkMesh> previewChunks = new HashMap<>();

    // player's overlapping on structures and other things.
    public static StructureConfiguration currentConfiguration;
    public static Structure currentStructure;
    public static boolean showedMessage = false;
    private static int dimension;
    private static Minecraft mcInstance;
    private static boolean needsRebuild = true;
    private static GpuTexture gpuTexture;
    private static GpuTextureView gpuTextureView;

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
        StructureRenderHandler.needsRebuild = true;

        StructureRenderHandler.mcInstance = Minecraft.getInstance();

        // Re-initialize GPU resources on setStructure call to ensure clean state for the new structure.
        if (gpuTexture != null) {
            gpuTexture.close();
            gpuTextureView.close();
        }
        StructureRenderHandler.gpuTexture = RenderSystem.getDevice().createTexture("Structure Preview", 12, TextureFormat.RGBA8, 16, 16, 1, 1);
        StructureRenderHandler.gpuTextureView = RenderSystem.getDevice().createTextureView(StructureRenderHandler.gpuTexture);

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

        Matrix4f matrix4f = matrixStack.last().pose();

        float translatedX = blockXOffset - cameraX;
        float translatedY = (float) (blockStartYOffset - cameraY + .02);
        float translatedYEnd = (float) (translatedY + height - .02);
        float translatedZ = blockZOffset - cameraZ;

        // Draw the verticals of the box.
        VertexConsumer bufferBuilder = multiBufferSource.getBuffer(RenderType.debugLineStrip(2.0));
        bufferBuilder.addVertex(matrix4f, translatedX, translatedY, translatedZ).setColor(1.0F, 1.0F, 0.0F, 1.0F);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedYEnd, translatedZ).setColor(1.0F, 1.0F, 0.0F, 1.0F);

        bufferBuilder = multiBufferSource.getBuffer(RenderType.debugLineStrip(2.0));
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedY, translatedZ).setColor(1.0F, 1.0F, 0.0F, 1.0F);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedYEnd, translatedZ).setColor(1.0F, 1.0F, 0.0F, 1.0F);

        bufferBuilder = multiBufferSource.getBuffer(RenderType.debugLineStrip(2.0));
        bufferBuilder.addVertex(matrix4f, translatedX, translatedY, translatedZ + zLength).setColor(1.0F, 1.0F, 0.0F, 1.0F);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedYEnd, translatedZ + zLength).setColor(1.0F, 1.0F, 0.0F, 1.0F);

        bufferBuilder = multiBufferSource.getBuffer(RenderType.debugLineStrip(2.0));
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedY, translatedZ + zLength).setColor(1.0F, 1.0F, 0.0F, 1.0F);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedYEnd, translatedZ + zLength).setColor(1.0F, 1.0F, 0.0F, 1.0F);

        // Draw bottom horizontals.
        bufferBuilder = multiBufferSource.getBuffer(RenderType.debugLineStrip(2.0));

        bufferBuilder.addVertex(matrix4f, translatedX, translatedY, translatedZ).setColor(1.0F, 1.0F, 0.0F, 1.0F);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedY, translatedZ + zLength).setColor(1.0F, 1.0F, 0.0F, 1.0F);

        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedY, translatedZ + zLength).setColor(1.0F, 1.0F, 0.0F, 1.0F);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedY, translatedZ).setColor(1.0F, 1.0F, 0.0F, 1.0F);

        bufferBuilder.addVertex(matrix4f, translatedX, translatedY, translatedZ).setColor(1.0F, 1.0F, 0.0F, 1.0F);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedY, translatedZ).setColor(1.0F, 1.0F, 0.0F, 1.0F);

        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedY, translatedZ + zLength).setColor(1.0F, 1.0F, 0.0F, 1.0F);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedY, translatedZ + zLength).setColor(1.0F, 1.0F, 0.0F, 1.0F);

        // Draw top horizontals
        bufferBuilder = multiBufferSource.getBuffer(RenderType.debugLineStrip(2.0));

        bufferBuilder.addVertex(matrix4f, translatedX, translatedYEnd, translatedZ).setColor(1.0F, 1.0F, 0.0F, 1.0F);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedYEnd, translatedZ + zLength).setColor(1.0F, 1.0F, 0.0F, 1.0F);

        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedYEnd, translatedZ + zLength).setColor(1.0F, 1.0F, 0.0F, 1.0F);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedYEnd, translatedZ).setColor(1.0F, 1.0F, 0.0F, 1.0F);

        bufferBuilder.addVertex(matrix4f, translatedX, translatedYEnd, translatedZ).setColor(1.0F, 1.0F, 0.0F, 1.0F);
        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedYEnd, translatedZ).setColor(1.0F, 1.0F, 0.0F, 1.0F);

        bufferBuilder.addVertex(matrix4f, translatedX + xLength, translatedYEnd, translatedZ + zLength).setColor(1.0F, 1.0F, 0.0F, 1.0F);
        bufferBuilder.addVertex(matrix4f, translatedX, translatedYEnd, translatedZ + zLength).setColor(1.0F, 1.0F, 0.0F, 1.0F);
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
                case NORTH: {
                    zLength = -zLength;
                    startingPosition = startingPosition.relative(config.direction.getOpposite());
                    break;
                }

                case EAST: {
                    int tempWidth = xLength;
                    xLength = zLength;
                    zLength = tempWidth;
                    break;
                }

                case SOUTH: {
                    xLength = -xLength;
                    startingPosition = startingPosition.relative(config.direction.getCounterClockWise());
                    break;
                }

                case WEST: {
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

    public static void renderStructurePreview(Player player, MultiBufferSource.BufferSource bufferSource) {
        if (StructureRenderHandler.currentStructure != null
                && StructureRenderHandler.dimension == player.level().dimensionType().logicalHeight()
                && StructureRenderHandler.currentConfiguration != null
                && PrefabBase.serverConfiguration.enableStructurePreview) {

            try {
                // Phase 1: Rebuild Meshes if necessary (Calculation/Stubbing)
                if (StructureRenderHandler.needsRebuild) {
                    rebuildPreviewMeshes(StructureRenderHandler.currentStructure, player, bufferSource);
                    StructureRenderHandler.needsRebuild = false;
                }

                Camera camera = StructureRenderHandler.mcInstance.gameRenderer.getMainCamera();

                // Setup view stack for world-relative positioning (Sticking the image to the player)
                PoseStack viewStack = new PoseStack();
                viewStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
                viewStack.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0F));
                viewStack.translate(
                        -camera.getPosition().x,
                        -camera.getPosition().y,
                        -camera.getPosition().z);

                // Phase 3: Optimized Rendering Pass (Drawing)
                renderPreviewChunks(viewStack);

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

    /**
     * Renders all cached preview meshes using the modern RenderPass API.
     */
    static void renderPreviewChunks(PoseStack poseStack) {
        if (previewChunks.isEmpty()) {
            return;
        }

        // Set up shader
        RenderPipeline shader = PrefabClientBase.ENTITY_TRANSLUCENT_CULL_PIPELINE;

        CommandEncoder commandEncoder =
                RenderSystem.getDevice().createCommandEncoder();

        for (PreviewChunkMesh mesh : previewChunks.values()) {
            GpuBuffer vertexBuffer = mesh.vertexBuffer();
            RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
            GpuBuffer gpuBuffer = autoStorageIndexBuffer.getBuffer(6 * mesh.meshIndices());

            try (RenderPass pass = commandEncoder.createRenderPass(() -> "Structure Preview",
                    StructureRenderHandler.gpuTextureView, OptionalInt.empty())) {

                RenderSystem.bindDefaultUniforms(pass);
                pass.bindSampler("Sampler0", RenderSystem.getShaderTexture(0));
                pass.setIndexBuffer(gpuBuffer, autoStorageIndexBuffer.type());
                pass.setVertexBuffer(0, vertexBuffer);

                // Set pipeline information along with any samplers and uniforms
                pass.setPipeline(shader);
                //pass.setPipeline(RenderPipelines.CLOUDS);
                pass.drawIndexed(0, 0, 6 * mesh.meshIndices(), 1);
            }
        }
    }

    private static void rebuildPreviewMeshes(Structure structure, Player player, MultiBufferSource.BufferSource bufferSource) {
        // Clear old meshes before rebuilding the cache for this frame/structure change.
        for (PreviewChunkMesh mesh : previewChunks.values()) {
            mesh.close();
        }

        previewChunks.clear();

        if (structure == null || StructureRenderHandler.currentConfiguration == null) {
            return;
        }

        Map<PreviewChunkKey, List<BuildBlock>> blocksByChunk = new HashMap<>();
        BlockRenderDispatcher blockRenderer = StructureRenderHandler.mcInstance.getBlockRenderer();

        for (BuildBlock blockInfo : structure.getBlocks()) {
            // Calculate the world-relative position for this specific instance of the block/subblock
            BlockPos rotatedPos = blockInfo.getStartingPosition().getRelativePosition(
                    StructureRenderHandler.currentConfiguration.pos,
                    StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection(),
                    StructureRenderHandler.currentConfiguration.houseFacing);

            int chunkX = Math.floorDiv(rotatedPos.getX(), 16);
            int chunkY = Math.floorDiv(rotatedPos.getY(), 16);
            int chunkZ = Math.floorDiv(rotatedPos.getZ(), 16);

            PreviewChunkKey key = new PreviewChunkKey(chunkX, chunkY, chunkZ);
            List<BuildBlock> blocks = blocksByChunk.computeIfAbsent(key, k -> new ArrayList<>());

            BlockState state = blockInfo.getBlockState() != null
                    ? blockInfo.getBlockState()
                    : BuiltInRegistries.BLOCK.getValue(blockInfo.getResourceLocation()).defaultBlockState();

            // Re-create the block object with the correct world position for assembly context
            BuildBlock block = BuildBlock.SetBlockState(
                    StructureRenderHandler.currentConfiguration,
                    player.level(),
                    StructureRenderHandler.currentConfiguration.pos,
                    blockInfo,
                    state.getBlock(),
                    state,
                    StructureRenderHandler.currentStructure);

            block.blockPos = rotatedPos;

            if (blockInfo.getSubBlock() != null) {
                BlockState subBlockState = blockInfo.getSubBlock().getBlockState() != null
                        ? blockInfo.getSubBlock().getBlockState()
                        : BuiltInRegistries.BLOCK.getValue(
                        blockInfo.getSubBlock().getResourceLocation()).defaultBlockState();

                // Re-create sub-block object with correct world position for assembly context
                BuildBlock subBlock = BuildBlock.SetBlockState(
                        StructureRenderHandler.currentConfiguration,
                        player.level(),
                        StructureRenderHandler.currentConfiguration.pos,
                        blockInfo.getSubBlock(),
                        subBlockState.getBlock(),
                        subBlockState,
                        StructureRenderHandler.currentStructure);

                subBlock.blockPos = blockInfo.getSubBlock().getStartingPosition().getRelativePosition(
                        StructureRenderHandler.currentConfiguration.pos,
                        StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection(),
                        StructureRenderHandler.currentConfiguration.houseFacing);

                block.setSubBlock(subBlock);
            }

            blocks.add(block);
        }

        // --- PHASE 2: Assemble Meshes ---
        Tesselator tesselator = Tesselator.getInstance();
        for (Map.Entry<PreviewChunkKey, List<BuildBlock>> entry : blocksByChunk.entrySet()) {
            PreviewChunkKey key = entry.getKey();
            List<BuildBlock> blocks = entry.getValue();

            if (blocks.isEmpty()) {
                continue;
            }

            int chunkOriginX = key.chunkX() * 16;
            int chunkOriginY = key.chunkY() * 16;
            int chunkOriginZ = key.chunkZ() * 16;

            // 1. Collect raw meshes from all components in the chunk
            List<PrefabMeshData> rawMeshes = new ArrayList<>();

            for (BuildBlock blockInfo : blocks) {
                PoseStack poseStack = new PoseStack();

                BufferBuilder bufferBuilder = (BufferBuilder) bufferSource.getBuffer(PrefabClientBase.PREVIEW_LAYER_2);
                //tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.NEW_ENTITY);

                // Calculate and collect mesh data for the main block body
                PrefabMeshData mainMesh = calculateMeshForBlock(blockInfo, poseStack, bufferBuilder, blockRenderer,
                        chunkOriginX, chunkOriginY, chunkOriginZ);

                if (mainMesh != null) {
                    rawMeshes.add(mainMesh);
                }

                // Calculate and collect mesh data for the sub-block body
                if (blockInfo.getSubBlock() != null) {
                    PrefabMeshData subMesh = calculateMeshForBlock(blockInfo.getSubBlock(), poseStack, bufferBuilder,
                            blockRenderer, chunkOriginX, chunkOriginY, chunkOriginZ);

                    if (subMesh != null) {
                        rawMeshes.add(subMesh);
                    }
                }
            }

            // 2. Assemble the final mesh from all raw components
            PrefabMeshData finalMesh = StructureMeshAssembler.assemble(rawMeshes);

            if (finalMesh != null && finalMesh.vertexCount() > 0) {
                GpuBuffer buffer = RenderSystem.getDevice().createBuffer(key::toString, 32, finalMesh.vertexCount());

                // Store the single optimized mesh in our cache for Phase 3 rendering.
                previewChunks.put(key, new PreviewChunkMesh(key, buffer, finalMesh.indexCount()));
            }
        }
    }

    private static PrefabMeshData calculateMeshForBlock(
            BuildBlock blockInfo,
            PoseStack poseStack,
            BufferBuilder bufferBuilder,
            BlockRenderDispatcher blockRenderer,
            int chunkOriginX, int chunkOriginY, int chunkOriginZ) {

        if (blockInfo.getBlockState() == null || blockInfo.getBlockState().isAir()) {
            return null;
        }

        // --- PHASE 1: DECOUPLE MESH GENERATION START ---
        PrefabMeshData meshData = MeshGeometryCalculator.calculateBlockMesh(
                blockInfo.blockPos,
                blockInfo.getBlockState());

        if (meshData == null) {
            return null;
        }

        BlockPos pos = blockInfo.blockPos;
        BlockState blockState = blockInfo.getBlockState();

        double lx = pos.getX() - chunkOriginX;
        double ly = pos.getY() - chunkOriginY;
        double lz = pos.getZ() - chunkOriginZ;

        poseStack.pushPose();
        poseStack.translate(lx, ly, lz);

        BlockStateModel model = blockRenderer.getBlockModel(blockState);

        int color = StructureRenderHandler.mcInstance.getBlockColors()
                .getColor(blockState, null, null, 0);
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        blockRenderer.getModelRenderer().renderModel(
                poseStack.last(),
                bufferBuilder,
                model,
                r, g, b,
                0xF000F0,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();

        return meshData;
    }
}