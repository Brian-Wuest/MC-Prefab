package com.prefab.mesh.geometry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

/**
 * Utility class responsible for calculating raw mesh data (vertices, indices)
 * from block geometry without performing any rendering calls or relying on the RenderSystem state.
 */
public class MeshGeometryCalculator {

    private static final String LOG_PREFIX = "MeshCalc";

    /**
     * Calculates and returns a pure MeshData object for a single block instance.
     * This method is designed to be stateless regarding OpenGL/Minecraft rendering context.
     *
     * @param pos The world position of the block.
     * @param state The BlockState defining the geometry.
     * @return A MeshData record containing raw vertex and index arrays, or null if calculation fails (e.g., air).
     */
    public static PrefabMeshData calculateBlockMesh(BlockPos pos, BlockState state) {
        if (state == null || state.isAir()) {
            return null;
        }

        // The block's bounding box is assumed to be 1x1x1 relative to its position in the world grid.
        float x = pos.getX();
        float y = pos.getY();
        float z = pos.getZ();

        // Vertices for a unit cube (8 corners)
        float[] vertices = new float[]{
                x, y, z,     // 0: Bottom-Front-Left
                (x + 1), y, z, // 1: Bottom-Front-Right
                x, (y + 1), z, // 2: Top-Front-Left
                (x + 1), (y + 1), z, // 3: Top-Front-Right

                x, y, (z + 1),   // 4: Bottom-Back-Left
                (x + 1), y, (z + 1), // 5: Bottom-Back-Right
                x, (y + 1), (z + 1), // 6: Top-Back-Left
                (x + 1), (y + 1), (z + 1)  // 7: Top-Back-Right
        };

        // Indices defining the 12 triangles (6 faces * 2 triangles/face = 12 indices total).
        int[] indices = new int[]{
                0, 3, 2, // Front face (Left to Right)
                4, 5, 7, // Back face (Left to Right)
                // Bottom face:
                0, 1, 5,
                0, 5, 4,
                // Top face:
                2, 3, 7,
                2, 7, 6,
                // Left face:
                0, 2, 6,
                0, 6, 4,
                // Right face:
                1, 3, 7,
                1, 7, 5,
                // Bottom edges (already covered by bottom/top faces above) - we only need the main six.
        };

        return new PrefabMeshData(vertices, null, indices, vertices.length / 3, indices.length);
    }

    /**
     * Calculates and returns a pure MeshData object for a sub-block instance.
     */
    public static PrefabMeshData calculateSubBlockMesh(BlockPos pos, BlockState state) {
        if (state == null || state.isAir()) {
            return null;
        }

        System.out.println(LOG_PREFIX + ": Calculating mesh data for sub-block at " + pos);

        // Reusing the main block logic for simplicity here.
        return calculateBlockMesh(pos, state);
    }
}