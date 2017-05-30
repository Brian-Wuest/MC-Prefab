package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.ModularHouseConfiguration;
import com.wuest.prefab.Items.Structures.ItemModularHouse;
import com.wuest.prefab.Proxy.Messages.ModularHouseTagMessage;

import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * The handler for the modular house message.
 * @author WuestMan
 *
 */
public class ModularHouseHandler implements
IMessageHandler<ModularHouseTagMessage, IMessage>
{
	@Override
	public IMessage onMessage(final ModularHouseTagMessage message,
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
				ModularHouseConfiguration configuration = (new ModularHouseConfiguration()).ReadFromNBTTagCompound(message.getMessageTag());
				ItemModularHouse.BuildHouse(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.worldObj, configuration);
			}
		});

		// no response in this case
		return null;
	}
}