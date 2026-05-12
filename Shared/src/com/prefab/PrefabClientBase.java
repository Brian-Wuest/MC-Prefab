package com.prefab;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.prefab.structures.render.GhostShaders;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.TextureAtlas;

public class PrefabClientBase {

    /*public static final RenderType PREVIEW_LAYER_2 = RenderType.create(        "ghost_preview_shimmer",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            262144,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> GhostShaders.GHOST_SHIMMER_SHADER))
                    .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)   // ✔ depth test ON
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)         // ✔ depth write OFF
                    .setCullState(RenderStateShard.NO_CULL)                  // ✔ show all faces
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .createCompositeState(false)
    );*/

    /*public static class PreviewLayer2 extends RenderType {
        public PreviewLayer2() {
            super(PrefabBase.MODID + ".ghost_preview_shimmer", DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.QUADS,
                    786432, false,
                    false,
                    () -> {
                        Sheets.translucentCullBlockSheet().setupRenderState();
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.80F);
                    }, () -> {
                        Sheets.translucentCullBlockSheet().clearRenderState();
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    });
        }
    }*/

    /*public static final RenderType PREVIEW_LAYER = new PreviewLayer();*/

    public static final RenderType PREVIEW_LAYER_2 = new PreviewLayer();

    public static class PreviewLayer extends RenderType {
        public PreviewLayer() {
            super(PrefabBase.MODID + ".preview", DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.QUADS,
                    786432, false,
                    false,
                    () -> {
                        Sheets.translucentCullBlockSheet().setupRenderState();
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.87F);
                    }, () -> {
                        Sheets.translucentCullBlockSheet().clearRenderState();
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    });
        }
    }
}
