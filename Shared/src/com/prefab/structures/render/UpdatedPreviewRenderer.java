package com.prefab.structures.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.prefab.PrefabBase;
import com.prefab.structures.base.BuildBlock;
import com.prefab.structures.base.BuildClear;
import com.prefab.structures.base.BuildShape;
import com.prefab.structures.base.Structure;
import com.prefab.structures.config.StructureConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class UpdatedPreviewRenderer {
    public static StructureConfiguration currentConfiguration;
    public static Structure currentStructure;
    public static boolean showedMessage = false;
    private static Minecraft mcInstance;
    private static int dimension;
    private static GpuTexture gpuTexture;
    private static GpuTextureView gpuTextureView;

    public static void setStructure(Structure structure, StructureConfiguration configuration) {
        UpdatedPreviewRenderer.currentStructure = structure;
        UpdatedPreviewRenderer.currentConfiguration = configuration;
        UpdatedPreviewRenderer.showedMessage = false;

        UpdatedPreviewRenderer.mcInstance = Minecraft.getInstance();

        // Re-initialize GPU resources on setStructure call to ensure clean state for the new structure.
        if (gpuTexture != null) {
            gpuTexture.close();
            gpuTextureView.close();
        }

        UpdatedPreviewRenderer.gpuTexture = RenderSystem.getDevice().createTexture("Structure Preview", 12, TextureFormat.RGBA8, 16, 16, 1, 1);
        UpdatedPreviewRenderer.gpuTextureView = RenderSystem.getDevice().createTextureView(UpdatedPreviewRenderer.gpuTexture);

        if (UpdatedPreviewRenderer.mcInstance.level != null) {
            UpdatedPreviewRenderer.dimension = UpdatedPreviewRenderer.mcInstance.level.dimensionType().logicalHeight();
        }
    }

    public static void renderPreview(Player player,
                                     MultiBufferSource.BufferSource bufferSource,
                                     PoseStack matrixStack,
                                     float cameraX, float cameraY, float cameraZ) {
        if (UpdatedPreviewRenderer.currentStructure != null
                && UpdatedPreviewRenderer.dimension == player.level().dimensionType().logicalHeight()
                && UpdatedPreviewRenderer.currentConfiguration != null
                && PrefabBase.serverConfiguration.enableStructurePreview) {
            try {
                BlockPos originalPos = UpdatedPreviewRenderer.currentConfiguration.pos;
                Structure structure = UpdatedPreviewRenderer.currentStructure;
                StructureConfiguration configuration = UpdatedPreviewRenderer.currentConfiguration;
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

            } catch (Exception ex) {
                PrefabBase.logger.error("Error during structure preview rendering.", ex);
            } finally {
            }
        }
    }
}
