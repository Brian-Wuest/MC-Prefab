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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

import java.util.*;

import static com.prefab.PrefabClientBase.PREVIEW_LAYER_2;

/**
 * @author WuestMan
 * This class was inspired from Botania's AstrolabePreviewHandler.
 * <a href="http://botaniamod.net/license.php">...</a>
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public class StructureRenderHandler1 {

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
        StructureRenderHandler1.currentStructure = structure;
        StructureRenderHandler1.currentConfiguration = configuration;
        StructureRenderHandler1.showedMessage = false;
        StructureRenderHandler1.needsRebuild = true;

        StructureRenderHandler1.mcInstance = Minecraft.getInstance();

        StructureRenderHandler1.gpuTexture = RenderSystem.getDevice().createTexture("Structure Preview", 12, TextureFormat.RGBA8, 16, 16, 1, 1);
        StructureRenderHandler1.gpuTextureView = RenderSystem.getDevice().createTextureView(StructureRenderHandler1.gpuTexture);

        if (StructureRenderHandler1.mcInstance.level != null) {
            StructureRenderHandler1.dimension = StructureRenderHandler1.mcInstance.level.dimensionType().logicalHeight();
        }
    }

    public static void renderStructureStartPositionBox(Level worldIn, PoseStack matrixStack,
                                                       MultiBufferSource.BufferSource multiBufferSource,
                                                       float cameraX, float cameraY, float cameraZ) {
        if (StructureRenderHandler1.currentStructure != null
                && StructureRenderHandler1.dimension == Minecraft.getInstance().player.level().dimensionType().logicalHeight()
                && StructureRenderHandler1.currentConfiguration != null
                && PrefabBase.serverConfiguration.enableStructurePreview) {
            BlockPos originalPos = StructureRenderHandler1.currentConfiguration.pos.above();

            float blockXOffset = originalPos.getX();
            float blockZOffset = originalPos.getZ();
            float blockStartYOffset = originalPos.getY();

            StructureRenderHandler1.drawBox(
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
            float cameraX,
            float cameraY,
            float cameraZ,
            int xLength,
            int zLength,
            int height) {

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
                                           float cameraX,
                                           float cameraY,
                                           float cameraZ) {
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

            // Based on direction, width and length may be need to be modified;

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

            StructureRenderHandler1.drawBox(
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

    public static void renderStructurePreview(Player player
    ) {
        if (StructureRenderHandler1.currentStructure != null
                && StructureRenderHandler1.dimension == player.level().dimensionType().logicalHeight()
                && StructureRenderHandler1.currentConfiguration != null
                && PrefabBase.serverConfiguration.enableStructurePreview) {

            try {
                /*
                    If we need to re-build the meshes, do so now
                    This can happen if the player, cleared out a structure preview and wants to see a new one.
                    We do this so we only have to do the heavy lifting (building the rendered meshes) once.
                    And when it comes time to actually show them to the player based on current world location
                    and camera rotation we can just put them in the relative space as they have already been rendered.
                */
                if (StructureRenderHandler1.needsRebuild) {
                    rebuildPreviewMeshes(StructureRenderHandler1.currentStructure, player);
                    StructureRenderHandler1.needsRebuild = false;
                }

                Camera camera = StructureRenderHandler1.mcInstance.gameRenderer.getMainCamera();

                /*
                    Note: This is what makes the structure "stick" in the world as the player moves!
                    This also means that when the player moves their mouse (camera) left and right, the structure
                    doesn't rotate around them like they are the "center of gravity" for the structure.
                    Again, the image sticks in place. Without this calculation everything is kind of
                    distorted and disorientating.
                */
                PoseStack viewStack = new PoseStack();
                viewStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
                viewStack.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0F));
                viewStack.translate(
                        -camera.getPosition().x,
                        -camera.getPosition().y,
                        -camera.getPosition().z);

                //renderPreviewChunks(viewStack);

                if (!StructureRenderHandler1.showedMessage) {
                    // Stop narrator from continuing narrating what was in the structure GUI
                    Narrator.getNarrator().clear();

                    MutableComponent message = Component.translatable(GuiLangKeys.GUI_PREVIEW_NOTICE);
                    message.setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));

                    StructureRenderHandler1.mcInstance.gui.getChat().addMessage(message);

                    message = Component.translatable(GuiLangKeys.GUI_BLOCK_CLICKED);
                    message.setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW));
                    StructureRenderHandler1.mcInstance.gui.getChat().addMessage(message);

                    StructureRenderHandler1.showedMessage = true;
                }
            } catch (Exception ex) {
                PrefabBase.logger.error(ex);
            }
        }
    }

    private static void rebuildPreviewMeshes(Structure structure,
                                             Player player) {
        for (PreviewChunkMesh mesh : previewChunks.values()) {
            mesh.close();
        }

        previewChunks.clear();

        if (structure == null || StructureRenderHandler1.currentConfiguration == null) {
            return;
        }

        Map<PreviewChunkKey, List<BuildBlock>> blocksByChunk = new HashMap<>();

        for (BuildBlock blockInfo : structure.getBlocks()) {
            BlockPos rotatedPos = blockInfo.getStartingPosition().getRelativePosition(
                    StructureRenderHandler1.currentConfiguration.pos,
                    StructureRenderHandler1.currentStructure.getClearSpace().getShape().getDirection(),
                    StructureRenderHandler1.currentConfiguration.houseFacing);

            int chunkX = Math.floorDiv(rotatedPos.getX(), 16);
            int chunkY = Math.floorDiv(rotatedPos.getY(), 16);
            int chunkZ = Math.floorDiv(rotatedPos.getZ(), 16);

            PreviewChunkKey key = new PreviewChunkKey(chunkX, chunkY, chunkZ);
            List<BuildBlock> blocks = blocksByChunk.computeIfAbsent(key, k -> new ArrayList<>());

            BlockState state = blockInfo.getBlockState() != null
                    ? blockInfo.getBlockState()
                    : BuiltInRegistries.BLOCK.getValue(blockInfo.getResourceLocation()).defaultBlockState();

            BuildBlock block = BuildBlock.SetBlockState(
                    StructureRenderHandler1.currentConfiguration,
                    player.level(),
                    StructureRenderHandler1.currentConfiguration.pos,
                    blockInfo,
                    state.getBlock(),
                    state,
                    StructureRenderHandler1.currentStructure);

            block.blockPos = rotatedPos;

            if (blockInfo.getSubBlock() != null) {
                BlockState subBlockState = blockInfo.getSubBlock().getBlockState() != null
                        ? blockInfo.getSubBlock().getBlockState()
                        : BuiltInRegistries.BLOCK.getValue(
                        blockInfo.getSubBlock().getResourceLocation()).defaultBlockState();

                BuildBlock subBlock = BuildBlock.SetBlockState(
                        StructureRenderHandler1.currentConfiguration,
                        player.level(),
                        StructureRenderHandler1.currentConfiguration.pos,
                        blockInfo.getSubBlock(),
                        subBlockState.getBlock(),
                        subBlockState,
                        StructureRenderHandler1.currentStructure);

                subBlock.blockPos = subBlock.getStartingPosition().getRelativePosition(
                        StructureRenderHandler1.currentConfiguration.pos,
                        StructureRenderHandler1.currentStructure.getClearSpace().getShape().getDirection(),
                        StructureRenderHandler1.currentConfiguration.houseFacing);

                block.setSubBlock(subBlock);
            }

            blocks.add(block);
        }

        BlockRenderDispatcher blockRenderer = StructureRenderHandler1.mcInstance.getBlockRenderer();
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

            // Note: Do something with this if I ever need to add frustrum.
            /*int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;*/

            BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.NEW_ENTITY);

            PoseStack poseStack = new PoseStack();
            boolean hasGeometry = false;

            for (BuildBlock blockInfo : blocks) {
                boolean modelHasGeometry = bakeBlockAndSubBlock(
                        blockInfo,
                        poseStack,
                        bufferBuilder,
                        blockRenderer,
                        chunkOriginX,
                        chunkOriginY,
                        chunkOriginZ);

                if (!modelHasGeometry) {
                    continue;
                }

                hasGeometry = true;
            }

            if (!hasGeometry) {
                continue;
            }

            try (MeshData meshData = bufferBuilder.buildOrThrow()) {
                if (meshData == null || meshData.drawState() == null || meshData.drawState().vertexCount() == 0) {
                    continue;
                }

                GpuBuffer buffer = RenderSystem.getDevice().createBuffer(key::toString, 32, meshData.vertexBuffer());
                previewChunks.put(key, new PreviewChunkMesh(key, buffer, meshData.drawState().indexCount()));
            }
        }
    }

    private static boolean bakeBlockAndSubBlock(
            BuildBlock blockInfo,
            PoseStack poseStack,
            BufferBuilder bufferBuilder,
            BlockRenderDispatcher blockRenderer,
            int chunkOriginX, int chunkOriginY, int chunkOriginZ
    ) {
        Player player = Minecraft.getInstance().player;
        BlockPos pos = blockInfo.blockPos;

        BlockState worldState = player.level().getBlockState(pos);
        Block block = worldState.getBlock();

        if (!worldState.isAir() && block != Blocks.WATER) {
            // Skip rendering this preview block
            return false;
        }

        // --- MAIN BLOCK ---
        boolean hasGeometry = bakeOne(blockInfo.blockPos, blockInfo.getBlockState(),
                poseStack, blockRenderer,
                bufferBuilder,
                chunkOriginX, chunkOriginY, chunkOriginZ);

        // --- SUB BLOCK (multi-block models) ---
        if (blockInfo.getSubBlock() != null && hasGeometry) {
            BlockPos subBlockPos = blockInfo.getSubBlock().blockPos;

            BlockState subBlockWorldState = player.level().getBlockState(subBlockPos);
            Block blockSubBlock = subBlockWorldState.getBlock();

            if (!subBlockWorldState.isAir() && blockSubBlock != Blocks.WATER) {
                // Skip rendering this preview block
                return false;
            }

            boolean hasSubBlockGeometry = bakeOne(blockInfo.getSubBlock().blockPos,
                    blockInfo.getSubBlock().getBlockState(),
                    poseStack, blockRenderer,
                    bufferBuilder,
                    chunkOriginX, chunkOriginY, chunkOriginZ);

            if (!hasSubBlockGeometry) {
                return false;
            }
        }

        return hasGeometry;
    }

    private static boolean bakeOne(
            BlockPos pos,
            BlockState state,
            PoseStack poseStack,
            BlockRenderDispatcher blockRenderer,
            BufferBuilder bufferBuilder,
            int chunkOriginX, int chunkOriginY, int chunkOriginZ
    ) {
        if (state == null || state.isAir()) {
            return false;
        }

        double lx = pos.getX() - chunkOriginX;
        double ly = pos.getY() - chunkOriginY;
        double lz = pos.getZ() - chunkOriginZ;

        poseStack.pushPose();
        poseStack.translate(lx, ly, lz);

        BlockStateModel model = blockRenderer.getBlockModel(state);

        int color = StructureRenderHandler1.mcInstance.getBlockColors()
                .getColor(state, null, null, 0);
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

        return true;
    }

    /*static void renderPreviewChunks(PoseStack poseStack) {
        if (previewChunks.isEmpty()) {
            return;
        }

        // Set up shader
        RenderType renderType = PREVIEW_LAYER_2;
        //RenderPipeline shader = RenderPipelines.ENTITY_TRANSLUCENT;
        RenderPipeline shader = PrefabClientBase.ENTITY_TRANSLUCENT_CULL_PIPELINE;

        RenderSystem.AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);

        CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();

        //GlProgram compiledShaderProgram = RenderSystem.setShader(shader);
        //RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);

        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        //Matrix4f projMatrix = RenderSystem.getProjectionMatrix();

        for (PreviewChunkMesh mesh : previewChunks.values()) {
            GpuBuffer gpuBuffer = mesh.vertexBuffer();
            matrix4fStack.pushMatrix();

            poseStack.pushPose();

            poseStack.translate(
                    mesh.key.chunkX * 16,
                    mesh.key.chunkY * 16,
                    mesh.key.chunkZ * 16
            );

            Matrix4f poseMatrix = poseStack.last().pose();

            try (RenderPass pass = commandEncoder.createRenderPass(() -> "Structure Preview",
                    StructureRenderHandler1.gpuTextureView,
                    OptionalInt.of(0xFFFFFFFF))) {

                // Set pipeline information along with any samplers and uniforms
                pass.setPipeline(shader);
                pass.setVertexBuffer(0, mesh.vertexBuffer());
                pass.setIndexBuffer(gpuBuffer, indices.type());
                pass.bindSampler("Sampler0", RenderSystem.getShaderTexture(0));

                // Then, draw everything to the screen
                pass.drawIndexed(0, 0, mesh.meshIndices(), 1);
            }
            // Render actual block
            poseStack.pushPose();

            // Translate the mesh's chunk relative coordinates to actual world coordinates.
            poseStack.translate(
                    mesh.key.chunkX * 16,
                    mesh.key.chunkY * 16,
                    mesh.key.chunkZ * 16
            );

            Matrix4f poseMatrix = poseStack.last().pose();

            renderType.setupRenderState();


            mesh.vertexBuffer.drawWithShader(poseMatrix, projMatrix, compiledShaderProgram);
            renderType.clearRenderState();
            //VertexBuffer.unbind();
            poseStack.popPose();
            matrix4fStack.popMatrix();
        }
    }*/

}
