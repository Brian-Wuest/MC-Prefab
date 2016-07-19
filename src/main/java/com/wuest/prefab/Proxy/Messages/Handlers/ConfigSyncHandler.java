package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.HouseConfiguration;
import com.wuest.prefab.Items.ItemStartHouse;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;

import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ConfigSyncHandler implements
IMessageHandler<ConfigSyncMessage, IMessage>
{
	@Override
	public IMessage onMessage(final ConfigSyncMessage message,
			final MessageContext ctx) 
	{
		// Or Minecraft.getMinecraft() on the client.
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj; 

		mainThread.addScheduledTask(new Runnable() 
		{
			@Override
			public void run() 
			{
				// This is client side. Update the configuration.
				Prefab.proxy.proxyConfiguration.UpdateFromNBTTagCompound(message.getMessageTag());
			}
		});

		// no response in this case
		return null;
	}
}