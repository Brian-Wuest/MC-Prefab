package com.wuest.prefab.Proxy.Messages.Handlers;

import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.wuest.prefab.Config.HouseConfiguration;
import com.wuest.prefab.Items.ItemStartHouse;
import com.wuest.prefab.Proxy.Messages.HouseTagMessage;

public class HouseHandler implements
IMessageHandler<HouseTagMessage, IMessage>
{
	@Override
	public IMessage onMessage(final HouseTagMessage message,
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
				HouseConfiguration configuration = HouseConfiguration.ReadFromNBTTagCompound(message.getMessageTag());
				ItemStartHouse.BuildHouse(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.worldObj, configuration);
			}
		});

		// no response in this case
		return null;
	}
}
