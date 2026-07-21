package com.prefab.neoforge.network;

import com.prefab.network.ClientToServerTypes;
import com.prefab.network.INetworkWrapper;
import com.prefab.network.ServerToClientTypes;
import com.prefab.network.message.ScannerInfo;
import com.prefab.network.message.TagMessage;
import com.prefab.network.payloads.*;
import com.prefab.structures.messages.StructureTagMessage;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;

public class NetworkWrapper implements INetworkWrapper {
    @Override
    public <T> void sendToServer(ClientToServerTypes messageType, T message) {
        switch (messageType) {
            case ClientToServerTypes.STRUCTURE_BUILD -> {
                this.sendStructureTagMessage((StructureTagMessage) message);
                break;
            }

            case ClientToServerTypes.SCAN_SHAPE -> {
                this.sendScannerScanMessage((ScannerInfo) message);
                break;
            }

            case ClientToServerTypes.SCANNER_CONFIG_UPDATE -> {
                this.sendScannerConfigMessage((ScannerInfo) message);
                break;
            }
        }
    }

    @Override
    public <T> void sendToClient(ServerToClientTypes messageType, ServerPlayer targetEntity, T message) {
        switch (messageType) {
            case ServerToClientTypes.MOD_CONFIG_SYNC -> {
                this.sendModConfigSyncMessage((TagMessage) message, targetEntity);
                break;
            }

            case ServerToClientTypes.PLAYER_CONFIG_SYNC -> {
                this.sendPlayerConfigSyncMessage((TagMessage) message, targetEntity);
                break;
            }
        }
    }

    private void sendStructureTagMessage(StructureTagMessage message) {
        StructurePayload payload = new StructurePayload(message);

        ClientPacketDistributor.sendToServer(payload);
    }

    private void sendScannerConfigMessage(ScannerInfo message) {
        ScannerConfigPayload scannerConfigPayload = new ScannerConfigPayload(message);

        ClientPacketDistributor.sendToServer(scannerConfigPayload);
    }

    private void sendScannerScanMessage(ScannerInfo message) {
        ScanShapePayload scanShapePayload = new ScanShapePayload(message);

        ClientPacketDistributor.sendToServer(scanShapePayload);
    }

    private void sendModConfigSyncMessage(TagMessage message, ServerPlayer player) {
        ConfigSyncPayload configSyncPayload = new ConfigSyncPayload(message);
        PacketDistributor.sendToPlayer(player, configSyncPayload);
    }

    private void sendPlayerConfigSyncMessage(TagMessage message, ServerPlayer player) {
        PlayerConfigPayload playerConfigPayload = new PlayerConfigPayload(message);
        PacketDistributor.sendToPlayer(player, playerConfigPayload);
    }
}