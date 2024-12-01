package com.prefab.fabric.events;

import com.prefab.ModRegistryBase;
import com.prefab.PrefabBase;
import com.prefab.network.ServerToClientTypes;
import com.prefab.config.ModConfiguration;
import com.prefab.items.ItemSickle;
import com.prefab.network.message.TagMessage;
import com.prefab.registries.ModRegistries;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;

public class ServerEvents {
    /**
     * Determines the affected blocks by redstone power.
     */
    public static ArrayList<BlockPos> RedstoneAffectedBlockPositions = new ArrayList<>();

    static {
        ServerEvents.RedstoneAffectedBlockPositions = new ArrayList<>();
    }

    public static void registerServerEvents() {
        ServerEvents.serverStarting();

        ServerEvents.serverStarted();

        ServerEvents.playerJoinedServer();

        StructureEventHandler.registerStructureServerSideEvents();
    }

    private static void serverStarting() {
        ServerLifecycleEvents.SERVER_STARTING.register((server -> {
            // Only do this for server-side.
            ModRegistryBase.serverModRegistries = new ModRegistries();
        }));
    }

    private static void serverStarted() {
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            // Get the server configuration.
            // This will be pushed to the player when they join the world.
            ModConfiguration config = AutoConfig.getConfigHolder(ModConfiguration.class).getConfig();

            // Make sure the static mod configuration object is separate from the object loaded from the file system.
            // This way we don't have issues when players swap between servers and local worlds.
            CompoundTag tag = config.writeCompoundTag();
            PrefabBase.serverConfiguration = new ModConfiguration();
            PrefabBase.serverConfiguration.readFromTag(tag);

            // Do this when the server starts so that all appropriate tags are used.
            ItemSickle.setEffectiveBlocks();
        });
    }

    private static void playerJoinedServer() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, serverWorld) -> {
            if (entity instanceof ServerPlayer) {
                // Send the message to the client.
                TagMessage message = new TagMessage(PrefabBase.serverConfiguration.writeCompoundTag());
                PrefabBase.networkWrapper.sendToClient(ServerToClientTypes.MOD_CONFIG_SYNC, (ServerPlayer) entity, message);
            }
        });
    }
}
