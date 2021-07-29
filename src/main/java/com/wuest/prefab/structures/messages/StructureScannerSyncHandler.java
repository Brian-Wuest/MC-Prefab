package com.wuest.prefab.structures.messages;

import com.wuest.prefab.blocks.entities.StructureScannerBlockEntity;
import com.wuest.prefab.config.StructureScannerConfig;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StructureScannerSyncHandler {
    public static void handle(final StructureScannerSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            StructureScannerConfig config = (new StructureScannerConfig()).ReadFromCompoundNBT(message.getMessageTag());

            TileEntity blockEntity = context.getSender().getLevel().getBlockEntity(config.blockPos);

            if (blockEntity instanceof StructureScannerBlockEntity) {
                StructureScannerBlockEntity actualEntity = (StructureScannerBlockEntity) blockEntity;
                actualEntity.setConfig(config);
            }
        });

        context.setPacketHandled(true);
    }
}
