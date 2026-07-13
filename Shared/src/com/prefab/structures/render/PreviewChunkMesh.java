package com.prefab.structures.render;

import com.mojang.blaze3d.buffers.GpuBuffer;

public record PreviewChunkMesh(PreviewChunkKey key, GpuBuffer vertexBuffer, int meshIndices) {
    public void close() {
        this.vertexBuffer.close();
    }
}