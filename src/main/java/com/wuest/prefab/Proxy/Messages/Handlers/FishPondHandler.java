package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.FishPondConfiguration;
import com.wuest.prefab.Items.Structures.ItemFishPond;
import com.wuest.prefab.Proxy.Messages.FishPondTagMessage;

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
public class FishPondHandler implements
IMessageHandler<FishPondTagMessage, IMessage>
{
	@Override
	public IMessage onMessage(final FishPondTagMessage message,
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
				FishPondConfiguration configuration = (new FishPondConfiguration()).ReadFromNBTTagCompound(message.getMessageTag());
				ItemFishPond.BuildHouse(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.worldObj, configuration);
			}
		});

		// no response in this case
		return null;
	}
}