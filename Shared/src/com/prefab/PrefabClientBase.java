package com.prefab;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;

import java.util.function.Function;

import static net.minecraft.client.renderer.RenderStateShard.*;

public class PrefabClientBase {
    public static final RenderStateShard.ShaderStateShard RENDER_TYPE_ENTITY_TRANSLUCENT_CULL_SHADER = new RenderStateShard.ShaderStateShard(
            CoreShaders.RENDERTYPE_ENTITY_TRANSLUCENT);

    // This is a re-implementation of the 1.21.1 Entity_TRANSLUCENT_CULL as it was removed in 1.21.2, but we need
    // This specific configuration
    public static final Function<ResourceLocation, RenderType> ENTITY_TRANSLUCENT_CULL = Util.memoize(
            p_286165_ -> {
                RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                        .setShaderState(RENDER_TYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(p_286165_, TriState.FALSE, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(true);

                return RenderType.create("entity_translucent_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS,
                        1536, true, true, rendertype$compositestate);
            }
    );
    public static final RenderType PREVIEW_LAYER_2 = new PreviewLayer();

    public static final RenderType getEntityTranslucentCull() {
        return ENTITY_TRANSLUCENT_CULL.apply(TextureAtlas.LOCATION_BLOCKS);
    }

    public static class PreviewLayer extends RenderType {
        public PreviewLayer() {
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
        }
    }
}
