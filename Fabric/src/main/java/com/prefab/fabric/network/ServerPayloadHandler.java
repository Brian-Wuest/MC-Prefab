package com.prefab.fabric.network;

import com.prefab.blocks.entities.StructureScannerBlockEntity;
import com.prefab.config.StructureScannerConfig;
import com.prefab.network.payloads.ScanShapePayload;
import com.prefab.network.payloads.ScannerConfigPayload;
import com.prefab.network.payloads.StructurePayload;
import com.prefab.structures.config.StructureConfiguration;
import com.prefab.structures.messages.StructureTagMessage;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ServerPayloadHandler {
    public static void scannerConfigHandler(ScannerConfigPayload payload, ServerPlayNetworking.Context context) {
        // Packet processor, data will already have been de-serialized.
        context.player().getServer().execute(() -> {
            StructureScannerConfig config = payload.scannerInfo().ToConfig();

            // The GUI always goes down 1 block for it's processing, so we have to make sure we go UP a block.
            BlockEntity blockEntity = context.player().level().getBlockEntity(config.blockPos.above());

            if (blockEntity instanceof StructureScannerBlockEntity actualEntity) {
                actualEntity.setConfig(config);
            }
        });
    }

    public static void scannerScanHandler(ScanShapePayload payload, ServerPlayNetworking. Context context) {
        // Packet processor, data will already have been de-serialized.
        context.player().getServer().execute(() -> {
            StructureScannerConfig config = payload.scannerInfo().ToConfig();

            StructureScannerBlockEntity.ScanShape(config, context.player(), context.player().level());
        });
    }
    public static void structureBuilderHandler(StructurePayload payload, ServerPlayNetworking. Context context) {
        // Packet processor, data will already have been de-serialized.
        // Can only access the "attachedData" on the "network thread" which is here.
        StructureTagMessage.EnumStructureConfiguration structureConfig = payload.structureTagMessage().getStructureConfig();

        context.player().getServer().execute(() -> {
            // This is now on the "main" server thread and things can be done in the world!
            StructureConfiguration configuration = structureConfig.structureConfig.ReadFromCompoundTag(payload.structureTagMessage().getMessageTag());

            configuration.BuildStructure(context.player(), context.player().level());
        });
    }
}
