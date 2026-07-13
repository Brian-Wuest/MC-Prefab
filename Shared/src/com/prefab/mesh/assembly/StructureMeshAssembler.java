package com.prefab.mesh.assembly;

import com.prefab.mesh.geometry.PrefabMeshData;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class responsible for taking multiple individual block meshes
 * and assembling them into a single, optimized mesh suitable for rendering an entire
 * chunk preview.
 * This process requires careful management of vertex offsets to ensure indices
 * remain valid after concatenation.
 */
public class StructureMeshAssembler {

    /**
     * Assembles raw meshes from all components in the list into one final
     * PrefabMeshData object.
     *
     * @param rawMeshes A list of individual mesh data objects (one per
     *                  block/sub-block).
     * @return The combined, optimized MeshData for the chunk, or null if no geometry
     * is present.
     */
    public static PrefabMeshData assemble(List<PrefabMeshData> rawMeshes) {
        if (rawMeshes == null || rawMeshes.isEmpty()) {
            return null;
        }

        // --- PASS 1: Vertex Concatenation and Offset Tracking ---

        // Use ArrayLists for dynamic growth, converting to arrays at the end.
        List<Float> totalVertices = new ArrayList<>();
        List<Integer> offsets = new ArrayList<>(); // Stores the starting vertex index for each raw mesh

        int currentOffset = 0;

        for (PrefabMeshData rawMesh : rawMeshes) {
            if (rawMesh == null || rawMesh.vertexCount() == 0) {
                continue;
            }

            // A. Track Starting Index Offset
            offsets.add(currentOffset);

            // B. Concatenate Vertices
            // Assuming getVertices() returns the full list of floats [x1, y1, z1, x2, y2, z2...]
            for (float vertex : rawMesh.vertices()) {
                totalVertices.add(vertex);
            }

            // C. Update Offset: The number of vertices added is simply the size of the current mesh's vertex list.
            int numVertices = rawMesh.vertexCount();
            currentOffset += numVertices;
        }


        // --- PASS 2: Index Remapping (The Core Logic) ---

        List<Integer> newIndices = new ArrayList<>();

        for (int i = 0; i < rawMeshes.size(); i++) {
            PrefabMeshData rawMesh = rawMeshes.get(i);
            if (rawMesh == null || rawMesh.indexCount() == 0) continue;

            // Retrieve the starting global vertex index for this mesh's indices
            int meshStartOffset = offsets.get(i);

            // Iterate through original indices and apply the offset
            for (int originalIndex : rawMesh.indices()) {
                // Global Index = Mesh Start Offset + Original Local Index
                int globalIndex = meshStartOffset + originalIndex;
                newIndices.add(globalIndex);
            }
        }

        // --- Final Output Construction ---
        if (!totalVertices.isEmpty() && !newIndices.isEmpty()) {
            // --- PASS 2: Index Remapping (The Core Logic) ---
            List<Integer> indices = new ArrayList<>();

            for (int i = 0; i < rawMeshes.size(); i++) {
                PrefabMeshData rawMesh = rawMeshes.get(i);
                if (rawMesh == null || rawMesh.indexCount() == 0) {
                    continue;
                }

                for (float vertex : rawMesh.vertices()) {
                    totalVertices.add(vertex);
                }

                // C. Update Offset: The number of vertices added is simply the size of the current mesh's vertex list.
                int numVertices = rawMesh.vertexCount();
                currentOffset += numVertices;

                // Retrieve the starting global vertex index for this mesh's indices
                int meshStartOffset = offsets.get(i);

                // Iterate through original indices and apply the offset
                for (int originalIndex : rawMesh.indices()) {
                    // Global Index = Mesh Start Offset + Original Local Index
                    int globalIndex = meshStartOffset + originalIndex;
                    indices.add(globalIndex);
                }
            }

            // --- Final Output Construction ---
            if (totalVertices.isEmpty() || indices.isEmpty()) {
                return null; // Nothing to draw
            }

            float[] finalVertices = listToFloatArray(totalVertices);
            int[] finalIndices = newIndexListToArray(indices);

            // Return the combined mesh data structure
            // Arguments assumed: (vertices, indices, vertexCount, indexCount, source_count)
            return new PrefabMeshData(finalVertices, null, finalIndices,
                    totalVertices.size() / 3, newIndices.size());
        }

        return null;
    }

    /**
     * Helper method to convert List<Integer> to int[].
     */
    private static int[] newIndexListToArray(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * Helper method to convert List<Float> to float[]. (FIXED)
     */
    private static float[] listToFloatArray(List<Float> list) {
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

}

/*
public class StructureMeshAssembler {
    public static PrefabMeshData assemble(Collection<PrefabMeshData> meshList) {
        if (meshList == null || meshList.isEmpty()) {
            System.out.println("Assembler: No meshes provided to assemble.");
            return null;
        }

        // 1. Deduplication Map: Maps a unique vertex coordinate tuple (X, Y, Z) to its index in the final buffer.
        Map<Long, Integer> vertexToIndex = new HashMap<>();
        List<Float> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        // Temporary storage for all normals and UVs if needed later (omitted for simplicity here)
        float[] combinedNormals = null; // Placeholder
        int totalVertices = 0;


        for (PrefabMeshData mesh : meshList) {
            if (mesh == null || mesh.vertexCount() == 0 || mesh.indexCount() == 0) continue;

            // Process vertices and indices for the current mesh
            for (int i = 0; i < mesh.vertexCount(); i++) {
                float x = mesh.vertices()[i * 3];
                float y = mesh.vertices()[i * 3 + 1];
                float z = mesh.vertices()[i * 3 + 2];

                // Create a unique key for the vertex (assuming float precision is sufficient)
                long key = generateVertexKey(x, y, z);

                if (!vertexToIndex.containsKey(key)) {
                    // New unique vertex found: add it to our master list and record its index.
                    vertices.add(x);
                    vertices.add(y);
                    vertices.add(z);
                    vertexToIndex.put(key, totalVertices++);
                }

                int currentVertexIndex = vertexToIndex.get(key);
                // Store the actual index for this specific vertex instance in the final mesh's indices list.
                indices.add(currentVertexIndex);
            }
        }


        if (totalVertices == 0) {
             return PrefabMeshData.empty();
        }

        System.out.println("Assembler: Successfully merged " + meshList.size() + " meshes into a single buffer.");

        // Convert Lists back to primitive arrays for the final record structure
        float[] finalVertices = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            finalVertices[i] = vertices.get(i);
        }

        int[] finalIndices = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            finalIndices[i] = indices.get(i);
        }


        return new PrefabMeshData(finalVertices, null, finalIndices, totalVertices, indices.size());
    }

    private static long generateVertexKey(float x, float y, float z) {
        // Simple hashing/scaling approach to combine three floats into one long key.
        // This is highly dependent on the expected coordinate range and precision of Minecraft's world coords.
        long key = 17; // Prime number seed
        key = 31 * key + (long)(x * 100);
        key = 31 * key + (long)(y * 100);
        key = 31 * key + (long)(z * 100);
        return key;
    }
}*/
