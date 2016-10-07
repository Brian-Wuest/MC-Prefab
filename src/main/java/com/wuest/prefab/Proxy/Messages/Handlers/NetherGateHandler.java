package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.NetherGateConfiguration;
import com.wuest.prefab.Items.ItemNetherGate;
import com.wuest.prefab.Proxy.Messages.NetherGateTagMessage;

import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * 
 * @author WuestMan
 */
public class NetherGateHandler implements
IMessageHandler<NetherGateTagMessage, IMessage>
{
	@Override
	public IMessage onMessage(final NetherGateTagMessage message,
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
				NetherGateConfiguration configuration = (new NetherGateConfiguration()).ReadFromNBTTagCompound(message.getMessageTag());
				ItemNetherGate.BuildHouse(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.worldObj, configuration);
			}
		});

		// no response in this case
		return null;
	}
}