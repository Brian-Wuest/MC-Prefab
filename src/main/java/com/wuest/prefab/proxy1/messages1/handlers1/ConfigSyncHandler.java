package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.ServerModConfiguration;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;
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