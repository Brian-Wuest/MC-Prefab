package com.prefab.mesh.geometry;

/**
 * Represents raw mesh data calculated purely from block geometry without any rendering calls.
 * This decouples the calculation (Phase 1) from the drawing mechanism (Phase 3).
 */
public record PrefabMeshData(float[] vertices, float[] normals, int[] indices, int vertexCount, int indexCount) {

    /** Creates an empty mesh data container for initialization purposes. */
    public static PrefabMeshData empty() {
        return new PrefabMeshData(new float[0], new float[0], new int[0], 0, 0);
    }
}