package com.wuest.prefab.structures.messages;

import com.wuest.prefab.blocks.entities.StructureScannerBlockEntity;
import com.wuest.prefab.config.StructureScannerConfig;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StructureScannerActionHandler {
    public static void handle(final StructureScannerActionMessage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            StructureScannerConfig config = (new StructureScannerConfig()).ReadFromCompoundNBT(message.getMessageTag());

            StructureScannerBlockEntity.ScanShape(config, context.getSender(), context.getSender().getLevel());
        });

        context.setPacketHandled(true);
    }
}
