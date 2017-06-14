package com.wuest.prefab.Proxy.Messages.Handlers;

import com.wuest.prefab.Config.StructureConfiguration;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.Proxy.Messages.PlayerEntityTagMessage;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage.EnumStructureConfiguration;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
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
public class PlayerEntityHandler implements IMessageHandler<PlayerEntityTagMessage, IMessage>
{
	/**
	 * Initializes a new instance of the StructureHandler class.
	 */
	public PlayerEntityHandler()
	{
	}
	
	@Override
	public IMessage onMessage(final PlayerEntityTagMessage message, final MessageContext ctx) 
	{
		// Or Minecraft.getMinecraft() on the client.
		IThreadListener mainThread = Minecraft.getMinecraft();

		mainThread.addScheduledTask(new Runnable()
		{
			@Override
			public void run() 
			{
				// This is client side.
				NBTTagCompound newPlayerTag = Minecraft.getMinecraft().thePlayer.getEntityData();
				newPlayerTag.setTag(ModEventHandler.PLAYER_ENTITY_TAG, message.getMessageTag());
			}
		});

		// no response in this case
		return null;
	}
}
