package com.prefab;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.platform.LogicOp;
import com.mojang.blaze3d.platform.PolygonMode;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.ScissorState;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector4f;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.BiFunction;

import static net.minecraft.client.renderer.RenderPipelines.ENTITY_SNIPPET;

public class PrefabClientBase {
    public static final RenderPipeline ENTITY_TRANSLUCENT_CULL_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(ENTITY_SNIPPET)
                    .withLocation("pipeline/entity_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1f)
                    .withSampler("Sampler1")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(true  )
                    .build());

    private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_TRANSLUCENT_CULL_FUNCTION = Util.memoize((resourceLocation, boolean_) -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false))
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setOverlayState(RenderStateShard.OVERLAY)
                .createCompositeState(boolean_);
        return RenderType.create("prefab_entity_translucent_ull", 1536, true, true, ENTITY_TRANSLUCENT_CULL_PIPELINE, compositeState);
    });

    public static RenderType entityTranslucentCullFunction(ResourceLocation resourceLocation, boolean bl) {
        return ENTITY_TRANSLUCENT_CULL_FUNCTION.apply(resourceLocation, bl);
    }

    public static RenderType entityTranslucentCull(ResourceLocation resourceLocation) {
        return entityTranslucentCullFunction(resourceLocation, true);
    }

    public static final RenderType PREVIEW_LAYER_2 = RenderType.create(
            // The name of the render type
            "prefab:entity_translucent_cull",
            // The size of the buffer
            // Or 4MB
            4194304,
            // Whether it effects crumbling that is applied to block entities
            false,
            // Whether the vertices should be sorted before upload
            true,
            // The pipeline to use
            ENTITY_TRANSLUCENT_CULL_PIPELINE,
            // Any additional composite state settings to apply
            RenderType.CompositeState.builder()
                    //.setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false))
                    .setLightmapState(RenderType.LIGHTMAP)
                    .setOverlayState(RenderType.NO_OVERLAY)
                    .createCompositeState(RenderType.OutlineProperty.NONE)
    );

    /*public static class PreviewLayer1 extends RenderType {
        private final RenderType.CompositeState state;
        public PreviewLayer1(RenderType.CompositeState state) {
            super(PrefabBase.MODID + ".preview", DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.QUADS,
                    786432, false,
                    false,
                    () -> {
                        getEntityTranslucentCull().setupRenderState();
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.77F);
                    }, () -> {
                        getEntityTranslucentCull().clearRenderState();
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    });

            this.state = state;
        }

        @Override
        public void draw(MeshData meshData) {
            this.setupRenderState();
            GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms()
                    .writeTransform(
                            RenderSystem.getModelViewMatrix(),
                            new Vector4f(1.0F, 1.0F, 1.0F, 1.0F),
                            RenderSystem.getModelOffset(),
                            RenderSystem.getTextureMatrix(),
                            RenderSystem.getShaderLineWidth()
                    );
            MeshData var3 = meshData;

            try {
                GpuBuffer gpuBuffer = this.renderPipeline.getVertexFormat().uploadImmediateVertexBuffer(meshData.vertexBuffer());
                GpuBuffer gpuBuffer2;
                VertexFormat.IndexType indexType;
                if (meshData.indexBuffer() == null) {
                    RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(meshData.drawState().mode());
                    gpuBuffer2 = autoStorageIndexBuffer.getBuffer(meshData.drawState().indexCount());
                    indexType = autoStorageIndexBuffer.type();
                } else {
                    gpuBuffer2 = this.renderPipeline.getVertexFormat().uploadImmediateIndexBuffer(meshData.indexBuffer());
                    indexType = meshData.drawState().indexType();
                }

                RenderTarget renderTarget = this.state.outputState.getRenderTarget();
                GpuTextureView gpuTextureView = RenderSystem.outputColorTextureOverride != null
                        ? RenderSystem.outputColorTextureOverride
                        : renderTarget.getColorTextureView();
                GpuTextureView gpuTextureView2 = renderTarget.useDepth
                        ? (RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : renderTarget.getDepthTextureView())
                        : null;

                try (RenderPass renderPass = RenderSystem.getDevice()
                        .createCommandEncoder()
                        .createRenderPass(() -> "Immediate draw for " + this.getName(), gpuTextureView, OptionalInt.empty(), gpuTextureView2, OptionalDouble.empty())) {
                    renderPass.setPipeline(this.renderPipeline);
                    ScissorState scissorState = RenderSystem.getScissorStateForRenderTypeDraws();
                    if (scissorState.enabled()) {
                        renderPass.enableScissor(scissorState.x(), scissorState.y(), scissorState.width(), scissorState.height());
                    }

                    RenderSystem.bindDefaultUniforms(renderPass);
                    renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
                    renderPass.setVertexBuffer(0, gpuBuffer);

                    for (int i = 0; i < 12; i++) {
                        GpuTextureView gpuTextureView3 = RenderSystem.getShaderTexture(i);
                        if (gpuTextureView3 != null) {
                            renderPass.bindSampler("Sampler" + i, gpuTextureView3);
                        }
                    }

                    renderPass.setIndexBuffer(gpuBuffer2, indexType);
                    renderPass.drawIndexed(0, 0, meshData.drawState().indexCount(), 1);
                }
            } catch (Throwable var17) {
                if (meshData != null) {
                    try {
                        var3.close();
                    } catch (Throwable var14) {
                        var17.addSuppressed(var14);
                    }
                }

                throw var17;
            }

            if (meshData != null) {
                meshData.close();
            }

            this.clearRenderState();
        }

        @Override
        public VertexFormat format() {
            return DefaultVertexFormat.NEW_ENTITY;
        }

        @Override
        public VertexFormat.Mode mode() {
            return VertexFormat.Mode.QUADS;
        }
    }*/
}
