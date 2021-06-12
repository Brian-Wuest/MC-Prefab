package com.wuest.prefab.structures.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.proxy.CommonProxy;
import com.wuest.prefab.structures.base.BuildBlock;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.StructureConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * @author WuestMan
 * This class was derived from Botania's MultiBlockRenderer.
 * Most changes are for extra comments for myself as well as to use my blocks class structure.
 * http://botaniamod.net/license.php
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public class StructureRenderHandler {
    // player's overlapping on structures and other things.
    public static StructureConfiguration currentConfiguration;
    public static Structure currentStructure;
    public static Direction assumedNorth;
    public static boolean rendering = false;
    public static boolean showedMessage = false;
    private static int dimension;
    private static int overlay = OverlayTexture.pack(5, 10);

    /**
     * Resets the structure to show in the world.
     *
     * @param structure     The structure to show in the world, pass null to clear out the client.
     * @param assumedNorth  The assumed norther facing for this structure.
     * @param configuration The configuration for this structure.
     */
    public static void setStructure(Structure structure, Direction assumedNorth, StructureConfiguration configuration) {
        StructureRenderHandler.currentStructure = structure;
        StructureRenderHandler.assumedNorth = assumedNorth;
        StructureRenderHandler.currentConfiguration = configuration;
        StructureRenderHandler.showedMessage = false;

        Minecraft mc = Minecraft.getInstance();

        if (mc.level != null) {
            StructureRenderHandler.dimension = mc.level.dimensionType().logicalHeight();
        }
    }

    /**
     * This is to render the currently bound structure.
     *
     * @param player The player to render the structure for.
     * @param src    The ray trace for where the player is currently looking.
     */
    public static void renderPlayerLook(PlayerEntity player, RayTraceResult src, MatrixStack matrixStack) {
        if (StructureRenderHandler.currentStructure != null
                && StructureRenderHandler.dimension == player.level.dimensionType().logicalHeight()
                && StructureRenderHandler.currentConfiguration != null
                && CommonProxy.proxyConfiguration.serverConfiguration.enableStructurePreview) {
            rendering = true;
            boolean didAny = false;

            IRenderTypeBuffer.Impl entityVertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource();
            ArrayList<Tuple<BlockState, BlockPos>> entityModels = new ArrayList<>();

            for (BuildBlock buildBlock : StructureRenderHandler.currentStructure.getBlocks()) {
                Block foundBlock = Registry.BLOCK.get(buildBlock.getResourceLocation());

                if (foundBlock != null) {
                    // Get the unique block state for this block.
                    BlockState blockState = foundBlock.defaultBlockState();
                    buildBlock = BuildBlock.SetBlockState(
                            StructureRenderHandler.currentConfiguration,
                            player.level,
                            StructureRenderHandler.currentConfiguration.pos,
                            StructureRenderHandler.assumedNorth,
                            buildBlock,
                            foundBlock,
                            blockState,
                            StructureRenderHandler.currentStructure);

                    // In order to get the proper relative position I also need the structure's original facing.
                    BlockPos pos = buildBlock.getStartingPosition().getRelativePosition(
                            StructureRenderHandler.currentConfiguration.pos,
                            StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection(),
                            StructureRenderHandler.currentConfiguration.houseFacing);

                    BlockRenderType blockRenderType = blockState.getRenderShape();

                    if (blockRenderType == BlockRenderType.ENTITYBLOCK_ANIMATED) {
                        if (ShaderHelper.hasIncompatibleMods) {
                            entityModels.add(new Tuple<>(buildBlock.getBlockState(), pos));
                        }

                        continue;
                    }

                    if (StructureRenderHandler.renderComponentInWorld(player.level, buildBlock, entityVertexConsumer, matrixStack, pos, blockRenderType)) {
                        didAny = true;
                    }
                }
            }

            ShaderHelper.useShader(ShaderHelper.alphaShader, shader -> {
                // getUniformLocation
                int alpha = GlStateManager._glGetUniformLocation(shader, "alpha");
                ShaderHelper.FLOAT_BUF.position(0);
                ShaderHelper.FLOAT_BUF.put(0, 0.4F);

                // uniform1
                GlStateManager._glUniform1(alpha, ShaderHelper.FLOAT_BUF);
            });

            // Draw function.
            entityVertexConsumer.endBatch(Atlases.translucentItemSheet());

            ShaderHelper.releaseShader();

            for (Tuple<BlockState, BlockPos> pair : entityModels) {
                BlockPos blockPos = pair.getSecond();
                StructureRenderHandler.renderBlock(matrixStack, new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), pair.getFirst(), entityVertexConsumer, BlockRenderType.ENTITYBLOCK_ANIMATED);
            }

            if (!didAny) {
                // Nothing was generated, tell the user this through a chat message and re-set the structure information.
                StructureRenderHandler.setStructure(null, Direction.NORTH, null);

                TranslationTextComponent message = new TranslationTextComponent(GuiLangKeys.GUI_PREVIEW_COMPLETE);
                message.setStyle(Style.EMPTY.withColor(TextFormatting.GREEN));
                player.sendMessage(message, player.getUUID());

            } else if (!StructureRenderHandler.showedMessage) {
                TranslationTextComponent message = new TranslationTextComponent(GuiLangKeys.GUI_PREVIEW_NOTICE);
                message.setStyle(Style.EMPTY.withColor(TextFormatting.GREEN));

                player.sendMessage(message, player.getUUID());
                StructureRenderHandler.showedMessage = true;
            }

            if (didAny) {
                StructureRenderHandler.RenderTest(player.getCommandSenderWorld(), matrixStack);
            }
        }
    }

    private static boolean renderComponentInWorld(World world, BuildBlock buildBlock, IRenderTypeBuffer entityVertexConsumer, MatrixStack matrixStack, BlockPos pos, BlockRenderType blockRenderType) {
        // Don't render this block if it's going to overlay a non-air/water block.
        BlockState targetBlock = world.getBlockState(pos);
        if (targetBlock.getMaterial() != Material.AIR && targetBlock.getMaterial() != Material.WATER) {
            return false;
        }

        StructureRenderHandler.doRenderComponent(buildBlock, pos, entityVertexConsumer, matrixStack, blockRenderType);

        if (buildBlock.getSubBlock() != null) {
            Block foundBlock = Registry.BLOCK.get(buildBlock.getSubBlock().getResourceLocation());
            BlockState blockState = foundBlock.defaultBlockState();

            BuildBlock subBlock = BuildBlock.SetBlockState(
                    StructureRenderHandler.currentConfiguration,
                    world, StructureRenderHandler.currentConfiguration.pos,
                    assumedNorth,
                    buildBlock.getSubBlock(),
                    foundBlock,
                    blockState,
                    StructureRenderHandler.currentStructure);

            BlockPos subBlockPos = subBlock.getStartingPosition().getRelativePosition(
                    StructureRenderHandler.currentConfiguration.pos,
                    StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection(),
                    StructureRenderHandler.currentConfiguration.houseFacing);

            BlockRenderType subBlockRenderType = subBlock.getBlockState().getRenderShape();

            return StructureRenderHandler.renderComponentInWorld(world, subBlock, entityVertexConsumer, matrixStack, subBlockPos, subBlockRenderType);
        }

        return true;
    }

    private static void doRenderComponent(BuildBlock buildBlock, BlockPos pos, IRenderTypeBuffer entityVertexConsumer, MatrixStack matrixStack, BlockRenderType blockRenderType) {
        BlockState state = buildBlock.getBlockState();
        StructureRenderHandler.renderBlock(matrixStack, new Vector3d(pos.getX(), pos.getY(), pos.getZ()), state, entityVertexConsumer, blockRenderType);
    }

    private static void renderBlock(MatrixStack matrixStack, Vector3d pos, BlockState state, IRenderTypeBuffer entityVertexConsumer, BlockRenderType blockRenderType) {
        Minecraft minecraft = Minecraft.getInstance();
        Vector3d projectedView = minecraft.getEntityRenderDispatcher().camera.getPosition();
        double renderPosX = projectedView.x();
        double renderPosY = projectedView.y();
        double renderPosZ = projectedView.z();

        // push
        matrixStack.pushPose();

        // Translate function.
        matrixStack.translate(-renderPosX, -renderPosY, -renderPosZ);

        BlockRendererDispatcher renderer = minecraft.getBlockRenderer();

        // Translate.
        matrixStack.translate(pos.x(), pos.y(), pos.z());

        IBakedModel bakedModel = renderer.getBlockModel(state);

        if (blockRenderType == BlockRenderType.MODEL) {
            // getColor function.
            int color = minecraft.getBlockColors().getColor(state, null, null, 0);
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;

            renderer.getModelRenderer().renderModel(
                    matrixStack.last(),
                    entityVertexConsumer.getBuffer(Atlases.translucentItemSheet()),
                    state,
                    bakedModel,
                    r,
                    g,
                    b,
                    0xF000F0,
                    OverlayTexture.NO_OVERLAY);
        } else if (blockRenderType == BlockRenderType.ENTITYBLOCK_ANIMATED) {
            ClientWorld world = Minecraft.getInstance().level;
            IModelData modelData = ModelDataManager.getModelData(world, new BlockPos(pos));

            if (modelData == null) {
                modelData = net.minecraftforge.client.model.data.EmptyModelData.INSTANCE;
            }

            IModelData model = renderer.getBlockModel(state).getModelData(world, new BlockPos(pos), state, modelData);

            renderer.renderBlock(
                    state,
                    matrixStack,
                    entityVertexConsumer,
                    0xF000F0,
                    StructureRenderHandler.overlay,
                    model);
        }

        // pop
        matrixStack.popPose();
    }

    private static void renderModel(MatrixStack matrixStack, Vector3d pos, BlockState state, IRenderTypeBuffer entityVertexConsumer) {

    }

    private static void RenderTest(World worldIn, MatrixStack matrixStack) {
        BlockPos originalPos = StructureRenderHandler.currentConfiguration.pos.above();
        // This makes the block north and in-line with the player's line of sight.
        double blockXOffset = originalPos.getX();
        double blockZOffset = originalPos.getZ();
        double blockStartYOffset = originalPos.getY();
        double blockEndYOffset = originalPos.above().getY();

        StructureRenderHandler.drawBox(matrixStack, blockXOffset, blockZOffset, blockStartYOffset, blockEndYOffset);
    }

    private static void drawBox(MatrixStack matrixStack, double blockXOffset, double blockZOffset, double blockStartYOffset, double blockEndYOffset) {
        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(matrixStack.last().pose());

        final Vector3d view = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();

        RenderSystem.enableDepthTest();
        RenderSystem.shadeModel(7425);
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuilder();

        RenderSystem.disableTexture();
        RenderSystem.disableBlend();

        double translatedX = blockXOffset - view.x();
        double translatedY = blockStartYOffset - view.y();
        double translatedYEnd = translatedY + 1;
        double translatedZ = blockZOffset - view.z();

        vertexBuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        RenderSystem.lineWidth(2.0f);

        // Draw the verticals of the box.
        for (int k = 1; k < 2; k += 1) {
            vertexBuffer.vertex(translatedX, translatedY, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexBuffer.vertex(translatedX, translatedYEnd, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

            vertexBuffer.vertex(translatedX + (double) k, translatedY, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexBuffer.vertex(translatedX + (double) k, translatedYEnd, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

            vertexBuffer.vertex(translatedX, translatedY, translatedZ + (double) k).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexBuffer.vertex(translatedX, translatedYEnd, translatedZ + (double) k).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

            vertexBuffer.vertex(translatedX + 1.0D, translatedY, translatedZ + (double) k).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexBuffer.vertex(translatedX + 1.0D, translatedYEnd, translatedZ + (double) k).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        }

        // All horizontals.
        for (double i1 = translatedY; i1 <= translatedYEnd; i1 += 1) {
            // RED
            vertexBuffer.vertex(translatedX, i1, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexBuffer.vertex(translatedX, i1, translatedZ + 1.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

            vertexBuffer.vertex(translatedX + 1.0D, i1, translatedZ + 1.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexBuffer.vertex(translatedX + 1.0D, i1, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

            // BLUE
            vertexBuffer.vertex(translatedX, i1, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexBuffer.vertex(translatedX + 1, i1, translatedZ).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

            // Purple
            vertexBuffer.vertex(translatedX + 1, i1, translatedZ + 1).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexBuffer.vertex(translatedX, i1, translatedZ + 1).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        }

        tessellator.end();

        RenderSystem.lineWidth(1.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
        RenderSystem.shadeModel(7424);

        RenderSystem.popMatrix();
    }
}
