package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.WareHouseConfiguration;
import com.wuest.prefab.Items.ItemWareHouse;
import com.wuest.prefab.Proxy.Messages.WareHouseTagMessage;

import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This class is used to handle the warehouse message from client to server.
 * @author WuestMan
 */
public class WareHouseHandler implements
IMessageHandler<WareHouseTagMessage, IMessage>
{
	@Override
	public IMessage onMessage(final WareHouseTagMessage message,
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
				WareHouseConfiguration configuration = WareHouseConfiguration.ReadFromNBTTagCompound(message.getMessageTag());
				ItemWareHouse.BuildHouse(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.worldObj, configuration);
			}
		});

		// no response in this case
		return null;
	}
}
