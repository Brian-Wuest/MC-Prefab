package com.prefab.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import com.prefab.ClientModRegistryBase;
import com.prefab.ModRegistryBase;
import com.prefab.PrefabBase;
import com.prefab.network.payloads.PlayerConfigPayload;
import com.prefab.network.payloads.ConfigSyncPayload;
import com.prefab.fabric.network.ClientPayloadHandler;


import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class ClientModRegistry {
    public static KeyMapping keyBinding;
    public static KeyMapping.Category category;

    public static void registerModComponents() {
        ClientModRegistry.registerKeyBindings();

        ClientModRegistry.registerBlockLayers();

        ClientModRegistry.registerServerToClientMessageHandlers();
    }

    private static void registerServerToClientMessageHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.PACKET_TYPE, ClientPayloadHandler::ModConfigHandler);

        ClientPlayNetworking.registerGlobalReceiver(PlayerConfigPayload.PACKET_TYPE, ClientPayloadHandler::PlayerConfigHandler);
    }

    private static void registerBlockLayers() {
        BlockRenderLayerMap.putBlock(ModRegistryBase.GlassStairs, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModRegistryBase.GlassSlab, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModRegistryBase.PaperLantern, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModRegistryBase.Boundary, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModRegistryBase.Phasic, ChunkSectionLayer.CUTOUT);

        BlockRenderLayerMap.putBlock(ModRegistryBase.GrassStairs, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModRegistryBase.DirtStairs, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModRegistryBase.GrassSlab, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModRegistryBase.DirtSlab, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModRegistryBase.GrassWall, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModRegistryBase.DirtWall, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModRegistryBase.LightSwitch, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModRegistryBase.DarkLamp, ChunkSectionLayer.CUTOUT);
    }

    public static void registerKeyBindings() {
        category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(PrefabBase.MODID, "structure_preview"));
        // TODO: Create translation keys.
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "Build Current Structure", // The translation key of the keybinding's name
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                category // The translation key of the keybinding's category.
        ));
    }
}
