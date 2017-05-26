package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.ProduceFarmConfiguration;
import com.wuest.prefab.Items.ItemProduceFarm;
import com.wuest.prefab.Proxy.Messages.ProduceFarmTagMessage;

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
public class ProduceFarmHandler implements
IMessageHandler<ProduceFarmTagMessage, IMessage>
{
	@Override
	public IMessage onMessage(final ProduceFarmTagMessage message,
			final MessageContext ctx) 
	{
		// Or Minecraft.getMinecraft() on the client.
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world; 

		mainThread.addScheduledTask(new Runnable() 
		{
			@Override
			public void run() 
			{
				// This is server side. Build the house.
				ProduceFarmConfiguration configuration = (new ProduceFarmConfiguration()).ReadFromNBTTagCompound(message.getMessageTag());
				ItemProduceFarm.BuildHouse(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.world, configuration);
			}
		});

		// no response in this case
		return null;
	}
}