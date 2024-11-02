package com.wuest.prefab.structures.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.blocks.BlockStructureScanner;
import com.wuest.prefab.config.StructureScannerConfig;
import com.wuest.prefab.proxy.CommonProxy;
import com.wuest.prefab.structures.base.BuildBlock;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.StructureConfiguration;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.client.model.data.ModelData;

import java.util.HashMap;

/**
 * @author WuestMan
 * This class was derived from Botania's AstrolabePreviewHandler.
 * Most changes are for extra comments for myself as well as to use my blocks class structure.
 * <a href="http://botaniamod.net/license.php">...</a>
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public class StructureRenderHandler {
    // player's overlapping on structures and other things.
    public static StructureConfiguration currentConfiguration;
    public static Structure currentStructure;
    public static boolean showedMessage = false;
    private static int dimension;

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

        Minecraft mc = Minecraft.getInstance();

        if (mc.level != null) {
            StructureRenderHandler.dimension = mc.level.dimensionType().logicalHeight();
        }
    }

    public static void renderClickedBlock(
            double cameraX,
            double cameraY,
            double cameraZ) {
        if (StructureRenderHandler.currentStructure != null
                && StructureRenderHandler.dimension == Minecraft.getInstance().player.level().dimensionType().logicalHeight()
                && StructureRenderHandler.currentConfiguration != null
                && CommonProxy.proxyConfiguration.serverConfiguration.enableStructurePreview) {
            BlockPos originalPos = StructureRenderHandler.currentConfiguration.pos.above();
            // This makes the block north and in-line with the player's line of sight.
            double blockXOffset = originalPos.getX();
            double blockZOffset = originalPos.getZ();
            double blockStartYOffset = originalPos.getY();

            StructureRenderHandler.drawBox(
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

    private static void drawBox(
            double blockXOffset,
            double blockZOffset,
            double blockStartYOffset,
            double cameraX,
            double cameraY,
            double cameraZ,
            int xLength,
            int zLength,
            int height) {
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();

        RenderSystem.disableBlend();

        double translatedX = blockXOffset - cameraX;
        double translatedY = blockStartYOffset - cameraY + .02;
        double translatedYEnd = translatedY + height - .02D;
        double translatedZ = blockZOffset - cameraZ;

        RenderSystem.lineWidth(2.0f);

        // Draw the verticals of the box.
        bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(translatedX, translatedY, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(translatedX, translatedYEnd, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        tessellator.end();

        bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(translatedX + xLength, translatedY, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(translatedX + xLength, translatedYEnd, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        tessellator.end();

        bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(translatedX, translatedY, translatedZ + zLength).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(translatedX, translatedYEnd, translatedZ + zLength).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        tessellator.end();

        bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(translatedX + xLength, translatedY, translatedZ + zLength).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(translatedX + xLength, translatedYEnd, translatedZ + zLength).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        tessellator.end();

        // Draw bottom horizontals.
        bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        bufferBuilder.vertex(translatedX, translatedY, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(translatedX, translatedY, translatedZ + zLength).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

        bufferBuilder.vertex(translatedX + xLength, translatedY, translatedZ + zLength).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(translatedX + xLength, translatedY, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

        bufferBuilder.vertex(translatedX, translatedY, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(translatedX + xLength, translatedY, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

        bufferBuilder.vertex(translatedX + xLength, translatedY, translatedZ + zLength).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(translatedX, translatedY, translatedZ + zLength).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        tessellator.end();

        // Draw top horizontals
        bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        bufferBuilder.vertex(translatedX, translatedYEnd, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(translatedX, translatedYEnd, translatedZ + zLength).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

        bufferBuilder.vertex(translatedX + xLength, translatedYEnd, translatedZ + zLength).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(translatedX + xLength, translatedYEnd, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

        bufferBuilder.vertex(translatedX, translatedYEnd, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(translatedX + xLength, translatedYEnd, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

        bufferBuilder.vertex(translatedX + xLength, translatedYEnd, translatedZ + zLength).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(translatedX, translatedYEnd, translatedZ + zLength).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        tessellator.end();

        RenderSystem.lineWidth(1.0F);
        RenderSystem.enableBlend();
    }

    public static void renderScanningBoxes(double cameraX,
                                           double cameraY,
                                           double cameraZ) {
        if (Prefab.proxy.structureScanners.isEmpty()) {
            return;
        }

        for (int i = 0; i < Prefab.proxy.structureScanners.size(); i++) {
            StructureScannerConfig config = Prefab.proxy.structureScanners.get(i);

            BlockPos pos = config.blockPos;
            boolean removeConfig = false;
            removeConfig = pos == null;

            // Make sure the block exists in the world at the block pos.
            if (pos != null) {
                removeConfig = !(Minecraft.getInstance().level.getBlockState(pos).getBlock() instanceof BlockStructureScanner);
            }

            if (removeConfig) {
                Prefab.proxy.structureScanners.remove(i);
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

    public static void newRenderPlayerLook(Player player, PoseStack ms, VertexConsumer buffer, double cameraX, double cameraY, double cameraZ) {
        if (StructureRenderHandler.currentStructure != null
                && StructureRenderHandler.dimension == player.level().dimensionType().logicalHeight()
                && StructureRenderHandler.currentConfiguration != null
                && CommonProxy.proxyConfiguration.serverConfiguration.enableStructurePreview) {

            Level world = player.level();

            Vec3 cameraPosition = new Vec3(cameraX, cameraY, cameraZ);
            Direction playerViewDirection = player.getDirection();
            Vec3 playerViewVector = player.getViewVector(1.0F);
            HashMap<Integer, BakedModel> modelMap = new HashMap<>();
            HashMap<Integer, Integer> stateColor = new HashMap<>();

            for (BuildBlock buildBlock : StructureRenderHandler.currentStructure.getBlocks()) {

                Block foundBlock = buildBlock.getBlockState() != null ? buildBlock.getBlockState().getBlock() : BuiltInRegistries.BLOCK.get(buildBlock.getResourceLocation());

                if (foundBlock != null) {
                    // In order to get the proper relative position I also need the structure's original facing.
                    if (buildBlock.blockPos == null) {
                        buildBlock.blockPos = buildBlock.getStartingPosition().getRelativePosition(
                                StructureRenderHandler.currentConfiguration.pos,
                                StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection(),
                                StructureRenderHandler.currentConfiguration.houseFacing);
                    }

                    BlockPos buildBlockPos = buildBlock.blockPos;

                    // Don't render this block if it's going to overlay a non-air/water block.
                    BlockState targetBlock = world.getBlockState(buildBlockPos);

                    if (targetBlock.getBlock() != Blocks.AIR && targetBlock.getBlock() != Blocks.WATER) {
                        continue;
                    }

                    if (buildBlock.centerOfBlock == null) {
                        buildBlock.centerOfBlock = Vec3.atCenterOf(buildBlockPos);
                    }

                    Vec3 vectorBetweenPlayerAndBlock = new Vec3(
                            buildBlockPos.getX() - player.getX(),
                            buildBlockPos.getY() - player.getEyeY(),
                            buildBlockPos.getZ() - player.getZ());

                    vectorBetweenPlayerAndBlock.normalize();

                    BlockHitResult hitResult = Shapes.block().clip(cameraPosition, buildBlock.centerOfBlock, buildBlockPos);

                    // Note: The hit direction is in reference to the "Block"'s point of view, not the player.
                    if (hitResult == null || (hitResult.getDirection() != Direction.UP && hitResult.getDirection() != Direction.DOWN
                            && hitResult.getDirection() == playerViewDirection)) {
                        // Never hit the block in the first place or it's behind them so continue.
                        continue;
                    }

                    // Calculate the "line" between the block and the player's view.
                    // This is the same way that Endermen determine if a player is looking at them.
                    // This avoids using "Frustum" as it's finicky and prone to change with Minecraft's rendering changes.
                    double lineBetweenPlayerViewBlock = playerViewVector.normalize().dot(vectorBetweenPlayerAndBlock);
                    double result = 1.0 - 0.025 / vectorBetweenPlayerAndBlock.length();
                    boolean boolCheck = lineBetweenPlayerViewBlock > result;

                    if (!boolCheck) {
                        continue;
                    }

                    if (buildBlock.getBlockState() == null) {
                        // Get the unique block state for this block.
                        BlockState blockState = foundBlock.defaultBlockState();

                        buildBlock = BuildBlock.SetBlockState(
                                StructureRenderHandler.currentConfiguration,
                                StructureRenderHandler.currentConfiguration.pos,
                                buildBlock,
                                foundBlock,
                                blockState,
                                StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection());
                    }

                    StructureRenderHandler.renderBlockAt(ms, buffer, buildBlock.getBlockState(), buildBlockPos, buildBlock.hashCode(), modelMap, stateColor);

                    // Render the sub-block if there is any.
                    if (buildBlock.getSubBlock() != null) {
                        BuildBlock subBuildBlock = buildBlock.getSubBlock();

                        Block foundSubBlock = subBuildBlock.getBlockState() != null ? subBuildBlock.getBlockState().getBlock() : BuiltInRegistries.BLOCK.get(subBuildBlock.getResourceLocation());

                        if (subBuildBlock.getBlockState() == null) {
                            BlockState subBlockState = foundSubBlock.defaultBlockState();

                            subBuildBlock = BuildBlock.SetBlockState(
                                    StructureRenderHandler.currentConfiguration,
                                    StructureRenderHandler.currentConfiguration.pos,
                                    buildBlock.getSubBlock(),
                                    foundSubBlock,
                                    subBlockState,
                                    StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection());
                        }

                        if (subBuildBlock.blockPos == null) {
                            subBuildBlock.blockPos = subBuildBlock.getStartingPosition().getRelativePosition(
                                    StructureRenderHandler.currentConfiguration.pos,
                                    StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection(),
                                    StructureRenderHandler.currentConfiguration.houseFacing);
                        }

                        StructureRenderHandler.renderBlockAt(ms, buffer, subBuildBlock.getBlockState(), subBuildBlock.blockPos, subBuildBlock.hashCode(), modelMap, stateColor);
                    }
                }
            }
        }
    }

    private static void renderBlockAt(PoseStack ms, VertexConsumer buffer, BlockState state, BlockPos pos, int buildBlockHash, HashMap<Integer, BakedModel> modelMap, HashMap<Integer, Integer> colorMap) {
        if (state.getRenderShape() != RenderShape.INVISIBLE && state.getRenderShape() == RenderShape.MODEL) {
            Minecraft minecraft = Minecraft.getInstance();
            Camera camera = minecraft.getEntityRenderDispatcher().camera;
            double renderPosX = camera.getPosition().x();
            double renderPosY = camera.getPosition().y();
            double renderPosZ = camera.getPosition().z();

            ms.pushPose();
            ms.translate(-renderPosX, -renderPosY, -renderPosZ);

            BlockRenderDispatcher brd = minecraft.getBlockRenderer();
            ms.translate(pos.getX(), pos.getY(), pos.getZ());

            // Get these values out of the saved hashmaps if possible.
            BakedModel model = modelMap.computeIfAbsent(buildBlockHash, x -> brd.getBlockModel(state));
            int color = colorMap.computeIfAbsent(state.hashCode(), x -> minecraft.getBlockColors().getColor(state, null, null, 0));

            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;

            // Always use entity translucent layer so blending is turned on
            brd.getModelRenderer().renderModel(ms.last(), buffer, state, model, r, g, b, 0xF000F0, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, (RenderType)null);

            ms.popPose();
        }
    }
}
