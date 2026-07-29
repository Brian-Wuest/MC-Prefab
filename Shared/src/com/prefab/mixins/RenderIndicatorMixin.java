package com.prefab.mixins;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.prefab.ClientModRegistryBase;
import com.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class RenderIndicatorMixin {
    @Unique
    private final MultiBufferSource.BufferSource previewBufferSource = MultiBufferSource.immediate(
            new ByteBufferBuilder(RenderType.BIG_BUFFER_SIZE));

    @Inject(method = "emitGizmos", at = @At(value = "TAIL"))
    public void renderWorldLast(Frustum frustum,
                                double cameraX,
                                double cameraY,
                                double cameraZ,
                                float partialTicks,
                                CallbackInfo ci) {
        Minecraft prefabIndicatorMinecraft = Minecraft.getInstance();

        PoseStack poseStack = new PoseStack();

        if (prefabIndicatorMinecraft.player != null && (!prefabIndicatorMinecraft.player.isCrouching())) {
            poseStack.pushPose();
            StructureRenderHandler.renderStructureStartPositionBox(prefabIndicatorMinecraft.level,
                    poseStack,
                    previewBufferSource,
                    (float) cameraX, (float) cameraY, (float) cameraZ);

            poseStack.popPose();

            poseStack = new PoseStack();
            poseStack.pushPose();
            StructureRenderHandler.renderPreview(prefabIndicatorMinecraft.player, previewBufferSource, poseStack,
                    (float) cameraX, (float) cameraY, (float) cameraZ);
            poseStack.popPose();
        }

        // If there are structure scanners; run the rendering for them now.
        if (ClientModRegistryBase.structureScanners != null && !ClientModRegistryBase.structureScanners.isEmpty()) {
            StructureRenderHandler.renderScanningBoxes(poseStack, previewBufferSource,
                    (float) cameraX,
                    (float) cameraY,
                    (float) cameraZ
            );
        }
    }
}
