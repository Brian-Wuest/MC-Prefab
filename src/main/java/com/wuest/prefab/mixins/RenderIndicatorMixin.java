package com.wuest.prefab.mixins;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class RenderIndicatorMixin {
    @Inject(method = "render", at = @At(value = "TAIL"))
    public void renderWorldLast(MatrixStack matrices, IRenderTypeBuffer.Impl vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();

        if (!mc.player.isCrouching()) {
            StructureRenderHandler.renderClickedBlock(mc.level, matrices, cameraX, cameraY, cameraZ);
        }
    }
}
