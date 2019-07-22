package com.wuest.prefab.Proxy.Messages.Handlers;

import java.util.function.Supplier;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Config.ServerModConfiguration;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * 
 * @author WuestMan
 *
 */
public class ConfigSyncHandler
{
	public static void handle(final ConfigSyncMessage message, Supplier<NetworkEvent.Context> ctx)
	{
		World recipientWorld = null;

		NetworkEvent.Context context = ctx.get();
		
		if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT)
		{
			recipientWorld = Minecraft.getInstance().world;
		}
		else if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER)
		{
			recipientWorld = (ServerWorld) context.getSender().getServerWorld();
		}

		context.enqueueWork(new Runnable()
		{
			@Override
			public void run()
			{
				// This is client side. Update the configuration.
				((ClientProxy) Prefab.proxy).serverConfiguration = ServerModConfiguration.getFromNBTTagCompound(message.getMessageTag());

				ServerModConfiguration config = ((ClientProxy) Prefab.proxy).getServerConfiguration();
			}
		});

		context.setPacketHandled(true);
	}
}