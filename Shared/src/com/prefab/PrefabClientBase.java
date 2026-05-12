package com.prefab;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;

public class PrefabClientBase {
    public static final RenderType PREVIEW_LAYER = new PreviewLayer();

    public static class PreviewLayer extends RenderType {
        public PreviewLayer() {
            super(PrefabBase.MODID + ".preview", DefaultVertexFormat.NEW_ENTITY,
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
    }
}
