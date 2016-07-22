package com.wuest.prefab.Proxy.Messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ProduceFarmTagMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish the house tag message from other messages in the mod.
	 */
	public ProduceFarmTagMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public ProduceFarmTagMessage()
	{
		super();
	}

}