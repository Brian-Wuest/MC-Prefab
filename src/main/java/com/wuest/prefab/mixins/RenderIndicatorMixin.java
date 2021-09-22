package com.wuest.prefab.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class RenderIndicatorMixin {
    @Inject(method = "render", at = @At(value = "TAIL"))
    public void renderWorldLast(PoseStack matrices, MultiBufferSource.BufferSource vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null && (!mc.player.isCrouching())) {
            StructureRenderHandler.renderClickedBlock(mc.level, matrices, cameraX, cameraY, cameraZ);
        }

        // It there are structure scanners; run the rendering for them now.
        if (Prefab.proxy.structureScanners != null && Prefab.proxy.structureScanners.size() != 0) {
            StructureRenderHandler.renderScanningBoxes(matrices, cameraX, cameraY, cameraZ);
        }
    }
}
