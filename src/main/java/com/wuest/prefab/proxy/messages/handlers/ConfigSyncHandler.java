package com.wuest.prefab.proxy.messages.handlers;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.config.ServerModConfiguration;
import com.wuest.prefab.proxy.ClientProxy;
import com.wuest.prefab.proxy.messages.ConfigSyncMessage;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author WuestMan
 */
public class ConfigSyncHandler {
    public static void handle(final ConfigSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            // This is client side. Update the configuration.
            ((ClientProxy) Prefab.proxy).serverConfiguration = ServerModConfiguration.getFromNBTTagCompound(message.getMessageTag());
        });

        context.setPacketHandled(true);
    }
}