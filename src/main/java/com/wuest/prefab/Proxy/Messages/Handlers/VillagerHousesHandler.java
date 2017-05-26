package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.VillagerHouseConfiguration;
import com.wuest.prefab.Items.ItemVillagerHouses;
import com.wuest.prefab.Proxy.Messages.VillagerHousesTagMessage;

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
public class VillagerHousesHandler implements
IMessageHandler<VillagerHousesTagMessage, IMessage>
{
	@Override
	public IMessage onMessage(final VillagerHousesTagMessage message,
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
				VillagerHouseConfiguration configuration = (new VillagerHouseConfiguration()).ReadFromNBTTagCompound(message.getMessageTag());
				ItemVillagerHouses.BuildHouse(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.worldObj, configuration);
			}
		});

		// no response in this case
		return null;
	}
}