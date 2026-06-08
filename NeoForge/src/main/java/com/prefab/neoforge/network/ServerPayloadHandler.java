package com.prefab.neoforge.network;

import com.prefab.blocks.entities.StructureScannerBlockEntity;
import com.prefab.config.StructureScannerConfig;
import com.prefab.network.payloads.ScanShapePayload;
import com.prefab.network.payloads.ScannerConfigPayload;
import com.prefab.network.payloads.StructurePayload;
import com.prefab.structures.config.StructureConfiguration;
import com.prefab.structures.messages.StructureTagMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    public static void scannerConfigHandler(final ScannerConfigPayload payload, final IPayloadContext context) {
        // Do something with the data, on the main thread here if needed, otherwise execute it on the server.
        context.player().getServer().execute(() -> {
            StructureScannerConfig config = payload.scannerInfo().ToConfig();

            // The GUI always goes down 1 block for it's processing, so we have to make sure we go UP a block.
            BlockEntity blockEntity = context.player().level().getBlockEntity(config.blockPos.above());

            if (blockEntity instanceof StructureScannerBlockEntity actualEntity) {
                actualEntity.setConfig(config);
            }
        });
    }

    public static void scannerScanHandler(ScanShapePayload payload, IPayloadContext context) {
        // Packet processor, data will already have been de-serialized.
        context.player().getServer().execute(() -> {
            StructureScannerConfig config = payload.scannerInfo().ToConfig();
            ServerPlayer serverPlayer = (ServerPlayer)context.player();

            StructureScannerBlockEntity.ScanShape(config, serverPlayer, serverPlayer.serverLevel());
        });
    }

    public static void structureBuilderHandler(StructurePayload payload, IPayloadContext context) {
        // Packet processor, data will already have been de-serialized.
        // Can only access the "attachedData" on the "network thread" which is here.
        StructureTagMessage.EnumStructureConfiguration structureConfig = payload.structureTagMessage().getStructureConfig();

        context.player().getServer().execute(() -> {
            // This is now on the "main" server thread and things can be done in the world!
            StructureConfiguration configuration = structureConfig.structureConfig.ReadFromCompoundTag(payload.structureTagMessage().getMessageTag());
            ServerPlayer serverPlayer = (ServerPlayer) context.player();

            configuration.BuildStructure(serverPlayer, serverPlayer.serverLevel());
        });
    }
}
