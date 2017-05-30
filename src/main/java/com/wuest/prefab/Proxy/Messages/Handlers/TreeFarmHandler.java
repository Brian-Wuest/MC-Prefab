package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.TreeFarmConfiguration;
import com.wuest.prefab.Items.Structures.ItemTreeFarm;
import com.wuest.prefab.Proxy.Messages.TreeFarmTagMessage;

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
public class TreeFarmHandler implements
IMessageHandler<TreeFarmTagMessage, IMessage>
{
	@Override
	public IMessage onMessage(final TreeFarmTagMessage message,
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
				TreeFarmConfiguration configuration = (new TreeFarmConfiguration()).ReadFromNBTTagCompound(message.getMessageTag());
				ItemTreeFarm.BuildHouse(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.worldObj, configuration);
			}
		});

		// no response in this case
		return null;
	}
}