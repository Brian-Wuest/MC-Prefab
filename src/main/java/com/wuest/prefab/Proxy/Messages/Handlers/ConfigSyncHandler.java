package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.HouseConfiguration;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Items.ItemStartHouse;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * 
 * @author WuestMan
 *
 */
public class ConfigSyncHandler implements
IMessageHandler<ConfigSyncMessage, IMessage>
{
	@Override
	public IMessage onMessage(final ConfigSyncMessage message,
			final MessageContext ctx) 
	{
		// Or Minecraft.getMinecraft() on the client.
		IThreadListener mainThread = null;
		
		if (ctx.side.isClient())
		{
			mainThread = Minecraft.getMinecraft();
		}
		else
		{
			mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
		} 

		mainThread.addScheduledTask(new Runnable() 
		{
			@Override
			public void run() 
			{
				// This is client side. Update the configuration.
				((ClientProxy)Prefab.proxy).serverConfiguration =  ModConfiguration.getFromNBTTagCompound(message.getMessageTag());
				
				ModConfiguration config = ((ClientProxy)Prefab.proxy).getServerConfiguration();
				 				
				// Show a message to this player if their version is old.
				if (config.showMessage && config.enableVersionCheckMessage)
				{
					Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new TextComponentString(config.versionMessage));
				}
			}
		});

		// no response in this case
		return null;
	}
}