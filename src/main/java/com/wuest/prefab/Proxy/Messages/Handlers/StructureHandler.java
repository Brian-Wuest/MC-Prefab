package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.StructureConfiguration;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage.EnumStructureConfiguration;

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
public class StructureHandler implements IMessageHandler<StructureTagMessage, IMessage>
{
	/**
	 * Initializes a new instance of the StructureHandler class.
	 */
	public StructureHandler()
	{
	}
	
	@Override
	public IMessage onMessage(final StructureTagMessage message, final MessageContext ctx) 
	{
		// Or Minecraft.getMinecraft() on the client.
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world; 

		mainThread.addScheduledTask(new Runnable()
		{
			@Override
			public void run() 
			{
				// This is server side. Build the structure.
				EnumStructureConfiguration structureConfig = message.getStructureConfig();
				
				StructureConfiguration configuration = structureConfig.structureConfig.ReadFromNBTTagCompound(message.getMessageTag());
				configuration.BuildStructure(ctx.getServerHandler().player, ctx.getServerHandler().player.world);
			}
		});

		// no response in this case
		return null;
	}

}
