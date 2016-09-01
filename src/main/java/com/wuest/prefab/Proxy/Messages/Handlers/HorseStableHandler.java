package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.HorseStableConfiguration;
import com.wuest.prefab.Items.ItemHorseStable;
import com.wuest.prefab.Proxy.Messages.HorseStableTagMessage;

import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HorseStableHandler implements
IMessageHandler<HorseStableTagMessage, IMessage>
{
	@Override
	public IMessage onMessage(final HorseStableTagMessage message,
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
				HorseStableConfiguration configuration = (new HorseStableConfiguration()).ReadFromNBTTagCompound(message.getMessageTag());
				ItemHorseStable.BuildHouse(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.worldObj, configuration);
			}
		});

		// no response in this case
		return null;
	}
}