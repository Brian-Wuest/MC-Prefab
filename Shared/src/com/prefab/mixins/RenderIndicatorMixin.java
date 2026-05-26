package com.prefab.mixins;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.prefab.ClientModRegistryBase;
import com.prefab.PrefabClientBase;
import com.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class RenderIndicatorMixin {
    @Unique
    private MultiBufferSource.BufferSource previewBufferSource = MultiBufferSource.immediate(new ByteBufferBuilder(PrefabClientBase.PREVIEW_LAYER_2.bufferSize()));

    @Inject(method = "render", at = @At(value = "TAIL"))
    public void renderWorldLast(PoseStack matrices,
                                Frustum frustum,
                                MultiBufferSource.BufferSource vertexConsumers,
                                double cameraX,
                                double cameraY,
                                double cameraZ,
                                CallbackInfo ci) {
        Minecraft prefabIndicatorMinecraft = Minecraft.getInstance();

        PoseStack poseStack = new PoseStack();

        if (prefabIndicatorMinecraft.player != null && (!prefabIndicatorMinecraft.player.isCrouching())) {
            StructureRenderHandler.renderStructureStartPositionBox(prefabIndicatorMinecraft.level,
                    poseStack,
                    previewBufferSource,
                    (float) cameraX, (float) cameraY, (float) cameraZ);

            StructureRenderHandler.renderStructurePreview(prefabIndicatorMinecraft.player);

            previewBufferSource.endBatch(PrefabClientBase.PREVIEW_LAYER_2);
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
