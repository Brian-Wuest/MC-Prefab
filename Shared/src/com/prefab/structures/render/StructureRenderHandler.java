package com.prefab.structures.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.mojang.text2speech.Narrator;
import com.prefab.ClientModRegistryBase;
import com.prefab.PrefabBase;
import com.prefab.PrefabClientBase;
import com.prefab.Triple;
import com.prefab.blocks.BlockStructureScanner;
import com.prefab.config.StructureScannerConfig;
import com.prefab.gui.GuiLangKeys;
import com.prefab.structures.base.BuildBlock;
import com.prefab.structures.base.Structure;
import com.prefab.structures.config.StructureConfiguration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;

/**
 * @author WuestMan
 * This class was inspired from Botania's AstrolabePreviewHandler.
 * <a href="http://botaniamod.net/license.php">...</a>
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public class StructureRenderHandler {
    private static final Direction[] DIRECTIONS = Direction.values();
    // player's overlapping on structures and other things.
    public static StructureConfiguration currentConfiguration;
    public static Structure currentStructure;
    public static boolean showedMessage = false;
    private static int dimension;
    private static HashMap<Integer, Integer> stateColor;
    private static HashMap<Integer, Triple<Float, Float, Float>> colorRGB;
    private static Minecraft mcInstance;
    private static HashMap<Integer, ArrayList<List<BakedQuad>>> blockModelQuads;

    // Cached meshes for the current preview structure/orientation
    private static Map<PreviewChunkKey, PreviewChunkMesh> previewChunks = new HashMap<>();

    // Metadata to know when cache is valid
    private static BlockPos lastOrigin = null;
    private static boolean needsRebuild = true;

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
        StructureRenderHandler.stateColor = new HashMap<>(40000, 1);
        StructureRenderHandler.colorRGB = new HashMap<>(40000, 1);
        StructureRenderHandler.blockModelQuads = new HashMap<>(40000, 1);
        StructureRenderHandler.needsRebuild = true;

        StructureRenderHandler.mcInstance = Minecraft.getInstance();

        if (StructureRenderHandler.mcInstance.level != null) {
            StructureRenderHandler.dimension = StructureRenderHandler.mcInstance.level.dimensionType().logicalHeight();
        }
    }

    public static void RenderTest(Level worldIn, PoseStack matrixStack, MultiBufferSource multiBufferSource, float cameraX, float cameraY, float cameraZ) {
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

    public static void basicBlockRenderExample(Player player, PoseStack poseStack, BufferBuilder buffer, double cameraX, double cameraY, double cameraZ) {
        if (StructureRenderHandler.currentStructure != null
                && StructureRenderHandler.dimension == player.level().dimensionType().logicalHeight()
                && StructureRenderHandler.currentConfiguration != null
                && PrefabBase.serverConfiguration.enableStructurePreview) {

            Level world = player.level();

            try {
                BlockState state = Blocks.REDSTONE_BLOCK.defaultBlockState();
                BlockPos blockPos = StructureRenderHandler.currentConfiguration.pos.relative(Direction.SOUTH, 2).above(2);
                BlockRenderDispatcher brd = StructureRenderHandler.mcInstance.getBlockRenderer();
                BakedModel blockModel = brd.getBlockModel(state);
                ModelBlockRenderer modelBlockRenderer = brd.getModelRenderer();
                int color = StructureRenderHandler.mcInstance.getBlockColors().getColor(state, null, null, 0);
                float r = (float) (color >> 16 & 255) / 255.0F;
                float g = (float) (color >> 8 & 255) / 255.0F;
                float b = (float) (color & 255) / 255.0F;

                // Translate the pose properly...maybe
                PoseStack.Pose originalPose = poseStack.poseStack.peekLast();
                float scaleValue = blockPos.getY() + 1.3F;
                PoseStack.Pose lastPose = new PoseStack.Pose(originalPose);

                lastPose.pose().translate((float) -cameraX, (float) -cameraY, (float) -cameraZ);
                lastPose.pose().translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());

                StructureRenderHandler.renderModel(lastPose, buffer, state, blockModel, r, g, b, 0xF000F0, OverlayTexture.NO_OVERLAY, state.hashCode());
            } catch (Exception ex) {
                PrefabBase.logger.error(ex);
            }
        }
    }

    public static void newerRenderPlayerLook(Player player
    ) {
        if (StructureRenderHandler.currentStructure != null
                && StructureRenderHandler.dimension == player.level().dimensionType().logicalHeight()
                && StructureRenderHandler.currentConfiguration != null
                && PrefabBase.serverConfiguration.enableStructurePreview) {

            try {
                if (StructureRenderHandler.needsRebuild) {
                    rebuildPreviewMeshes(StructureRenderHandler.currentStructure, player);
                    StructureRenderHandler.needsRebuild = false;
                }

                Camera camera = StructureRenderHandler.mcInstance.gameRenderer.getMainCamera();

                PoseStack viewStack = new PoseStack();
                viewStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
                viewStack.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0F));
                viewStack.translate(-camera.getPosition().x,
                        -camera.getPosition().y,
                        -camera.getPosition().z);

                renderPreviewChunks(viewStack);

                if (!StructureRenderHandler.showedMessage) {
                    Minecraft mc = Minecraft.getInstance();

                    // Stop narrator from continuing narrating what was in the structure GUI
                    Narrator.getNarrator().clear();

                    MutableComponent message = Component.translatable(GuiLangKeys.GUI_PREVIEW_NOTICE);
                    message.setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));

                    mc.gui.getChat().addMessage(message);

                    message = Component.translatable(GuiLangKeys.GUI_BLOCK_CLICKED);
                    message.setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW));
                    mc.gui.getChat().addMessage(message);

                    StructureRenderHandler.showedMessage = true;
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

        if (structure == null || StructureRenderHandler.currentConfiguration == null) {
            return;
        }

        Map<PreviewChunkKey, List<BuildBlock>> blocksByChunk = new HashMap<>();

        for (BuildBlock blockInfo : structure.getBlocks()) {
            BlockPos rotatedPos = blockInfo.getStartingPosition().getRelativePosition(
                    StructureRenderHandler.currentConfiguration.pos,
                    StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection(),
                    StructureRenderHandler.currentConfiguration.houseFacing);

            int cx = Math.floorDiv(rotatedPos.getX(), 16);
            int cy = Math.floorDiv(rotatedPos.getY(), 16);
            int cz = Math.floorDiv(rotatedPos.getZ(), 16);

            PreviewChunkKey key = new PreviewChunkKey(cx, cy, cz);
            List<BuildBlock> blocks = blocksByChunk.computeIfAbsent(key, k -> new ArrayList<>());

            BlockState state = blockInfo.getBlockState() != null
                    ? blockInfo.getBlockState()
                    : BuiltInRegistries.BLOCK.get(blockInfo.getResourceLocation()).defaultBlockState();

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
                        ? blockInfo.getSubBlock().getBlockState() : BuiltInRegistries.BLOCK.get(blockInfo.getSubBlock().getResourceLocation()).defaultBlockState();

                BuildBlock subBlock = BuildBlock.SetBlockState(
                        StructureRenderHandler.currentConfiguration,
                        player.level(),
                        StructureRenderHandler.currentConfiguration.pos,
                        blockInfo.getSubBlock(),
                        subBlockState.getBlock(),
                        subBlockState,
                        StructureRenderHandler.currentStructure);

                subBlock.blockPos = subBlock.getStartingPosition().getRelativePosition(
                        StructureRenderHandler.currentConfiguration.pos,
                        StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection(),
                        StructureRenderHandler.currentConfiguration.houseFacing);

                block.setSubBlock(subBlock);
            }

            blocks.add(block);
        }

        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();

        for (Map.Entry<PreviewChunkKey, List<BuildBlock>> entry : blocksByChunk.entrySet()) {
            PreviewChunkKey key = entry.getKey();
            List<BuildBlock> blocks = entry.getValue();
            if (blocks.isEmpty()) continue;

            int chunkOriginX = key.cx * 16;
            int chunkOriginY = key.cy * 16;
            int chunkOriginZ = key.cz * 16;

            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

            Tesselator tesselator = Tesselator.getInstance();
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

            MeshData meshData = bufferBuilder.build();
            if (meshData == null || meshData.drawState() == null || meshData.drawState().vertexCount() == 0) {
                continue;
            }

            VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
            vertexBuffer.bind();
            vertexBuffer.upload(meshData);
            VertexBuffer.unbind();

            AABB bounds = new AABB(minX, minY, minZ, maxX, maxY, maxZ);
            previewChunks.put(key, new PreviewChunkMesh(key, vertexBuffer, bounds));
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
                poseStack, bufferBuilder, blockRenderer,
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
                    poseStack, bufferBuilder, blockRenderer,
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
            BufferBuilder bufferBuilder,
            BlockRenderDispatcher blockRenderer,
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

        BakedModel model = blockRenderer.getBlockModel(state);

        int color = StructureRenderHandler.mcInstance.getBlockColors().getColor(state, null, null, 0);
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        blockRenderer.getModelRenderer().renderModel(
                poseStack.last(),
                bufferBuilder,
                state,
                model,
                r, g, b,
                0xF000F0,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();

        return true;
    }

    private static void renderPreviewChunks(PoseStack poseStack) {
        if (previewChunks.isEmpty()) {
            return;
        }

        // Set up shader
        RenderType renderType = PrefabClientBase.PREVIEW_LAYER;
        ShaderInstance shader = GameRenderer.getRendertypeEntityTranslucentShader();
        RenderSystem.setShader(() -> shader);
        RenderSystem.setShaderTexture(5, TextureAtlas.LOCATION_BLOCKS);

        Matrix4f projMatrix = RenderSystem.getProjectionMatrix();

        for (PreviewChunkMesh mesh : previewChunks.values()) {
            poseStack.pushPose();

            poseStack.translate(
                    mesh.key.cx * 16,
                    mesh.key.cy * 16,
                    mesh.key.cz * 16
            );

            // Comment begin for debug
            Matrix4f poseMatrix = poseStack.last().pose();

            mesh.vertexBuffer.bind();
            renderType.setupRenderState();
            mesh.vertexBuffer.drawWithShader(poseMatrix, projMatrix, shader);
            renderType.clearRenderState();
            VertexBuffer.unbind();
            // comment end for debug

            poseStack.popPose();
        }
    }

    //------------------------------------------------------------------------------

    public static void renderModel(PoseStack.Pose pose, VertexConsumer vertexConsumer, @Nullable BlockState blockState, BakedModel bakedModel, float f, float g, float h, int i, int j, int blockStateHash) {
        RandomSource randomSource = RandomSource.create();
        long l = 42L;

        if (!StructureRenderHandler.blockModelQuads.containsKey(blockStateHash)) {
            // Render the quads like normal then add them to the hash set for the next pass.
            ArrayList<List<BakedQuad>> bakedQuads = new ArrayList<>();

            for (Direction direction : DIRECTIONS) {
                randomSource.setSeed(42L);

                List<BakedQuad> quadList = bakedModel.getQuads(blockState, direction, randomSource);
                StructureRenderHandler.renderQuadList(pose, vertexConsumer, f, g, h, quadList, i, j);
                bakedQuads.add(quadList);
            }

            randomSource.setSeed(42L);

            List<BakedQuad> quadList = bakedModel.getQuads(blockState, null, randomSource);
            StructureRenderHandler.renderQuadList(pose, vertexConsumer, f, g, h, quadList, i, j);

            bakedQuads.add(quadList);
            StructureRenderHandler.blockModelQuads.put(blockStateHash, bakedQuads);

            return;
        }

        // Render the cached baked quads.
        ArrayList<List<BakedQuad>> bakedQuads = StructureRenderHandler.blockModelQuads.get(blockStateHash);

        for (List<BakedQuad> quadList : bakedQuads) {
            StructureRenderHandler.renderQuadList(pose, vertexConsumer, f, g, h, quadList, i, j);
        }
    }

    private static void renderQuadList(PoseStack.Pose pose, VertexConsumer vertexConsumer, float f, float g, float h, List<BakedQuad> list, int i, int j) {
        BakedQuad bakedQuad;
        float k;
        float l;
        float m;

        for (BakedQuad quad : list) {
            if (quad.isTinted()) {
                k = Mth.clamp(f, 0.0F, 1.0F);
                l = Mth.clamp(g, 0.0F, 1.0F);
                m = Mth.clamp(h, 0.0F, 1.0F);
            } else {
                k = 1.0F;
                l = 1.0F;
                m = 1.0F;
            }

            StructureRenderHandler.putBulkData(vertexConsumer, pose, quad, k, l, m, i, j);
        }
    }

    private static void putBulkData(VertexConsumer vertexConsumer, PoseStack.Pose pose, BakedQuad bakedQuad, float k, float l, float m, int i, int j) {
        int[] js = bakedQuad.getVertices();
        Vec3i vec3i = bakedQuad.getDirection().getNormal();
        Matrix4f matrix4f = pose.pose();
        Vector3f vector3f = pose.transformNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ(), new Vector3f());
        int trimmedLength = js.length / 8;
        int baseColor = (int) (255.0F);

        for (int counter = 0; counter < trimmedLength; ++counter) {
            float betterO = Float.intBitsToFloat(js[counter * 8]);
            float betterP = Float.intBitsToFloat(js[(counter * 8) + 1]);
            float betterQ = Float.intBitsToFloat(js[(counter * 8) + 2]);

            float u = 1.0F * k * 255.0F;
            float v = 1.0F * l * 255.0F;
            float w = 1.0F * m * 255.0F;
            float betterT = Float.intBitsToFloat(js[(counter * 8) + 4]);
            float betterZ = Float.intBitsToFloat(js[(counter * 8) + 5]);

            int x = FastColor.ARGB32.color(baseColor, (int) u, (int) v, (int) w);

            Vector3f vector3f2 = matrix4f.transformPosition(betterO, betterP, betterQ, new Vector3f());
            vertexConsumer.addVertex(vector3f2.x(), vector3f2.y(), vector3f2.z(), x, betterT, betterZ, j, i, vector3f.x(), vector3f.y(), vector3f.z());
        }
    }

    public record PreviewChunkKey(int cx, int cy, int cz) {
    }

    /**
     * @param bounds for frustum culling
     */
    public record PreviewChunkMesh(PreviewChunkKey key, VertexBuffer vertexBuffer, AABB bounds) {

        public void close() {
                this.vertexBuffer.close();
            }
        }

}
