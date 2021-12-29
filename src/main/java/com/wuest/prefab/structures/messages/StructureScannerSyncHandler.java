package com.wuest.prefab.structures.messages;

import com.wuest.prefab.blocks.entities.StructureScannerBlockEntity;
import com.wuest.prefab.config.StructureScannerConfig;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StructureScannerSyncHandler {
    public static void handle(final StructureScannerSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            StructureScannerConfig config = (new StructureScannerConfig()).ReadFromCompoundTag(message.getMessageTag());

            BlockEntity blockEntity = context.getSender().getLevel().getBlockEntity(config.blockPos);

            if (blockEntity instanceof StructureScannerBlockEntity) {
                StructureScannerBlockEntity actualEntity = (StructureScannerBlockEntity) blockEntity;
                actualEntity.setConfig(config);
            }
        });

        context.setPacketHandled(true);
    }
}
