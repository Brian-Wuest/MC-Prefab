package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.BasicStructureConfiguration;
import com.wuest.prefab.Items.Structures.ItemBasicStructure;
import com.wuest.prefab.Items.Structures.ItemChickenCoop;
import com.wuest.prefab.Proxy.Messages.BasicStructureTagMessage;

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
public class BasicStructureHandler implements
IMessageHandler<BasicStructureTagMessage, IMessage>
{
	@Override
	public IMessage onMessage(final BasicStructureTagMessage message,
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
				BasicStructureConfiguration configuration = (new BasicStructureConfiguration()).ReadFromNBTTagCompound(message.getMessageTag());
				ItemBasicStructure.BuildHouse(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.worldObj, configuration);
			}
		});

		// no response in this case
		return null;
	}
}