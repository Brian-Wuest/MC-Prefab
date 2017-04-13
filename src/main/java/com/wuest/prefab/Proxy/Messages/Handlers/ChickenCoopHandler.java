package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.ChickenCoopConfiguration;
import com.wuest.prefab.Items.ItemChickenCoop;
import com.wuest.prefab.Items.ItemWareHouse;
import com.wuest.prefab.Proxy.Messages.ChickenCoopTagMessage;

import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * 
 * @author WuestMan
 *
 */
public class ChickenCoopHandler implements
IMessageHandler<ChickenCoopTagMessage, IMessage>
{
	@Override
	public IMessage onMessage(final ChickenCoopTagMessage message,
			final MessageContext ctx) 
	{
		// Or Minecraft.getMinecraft() on the client.
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj; 

		mainThread.addScheduledTask(new Runnable() 
		{
			@Override
			public void run() 
			{
				// This is server side. Build the house.
				ChickenCoopConfiguration configuration = (new ChickenCoopConfiguration()).ReadFromNBTTagCompound(message.getMessageTag());
				ItemChickenCoop.BuildHouse(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.worldObj, configuration);
			}
		});

		// no response in this case
		return null;
	}
}