package com.wuest.prefab.Proxy.Messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * This is the basic tag message for basic/custom structures.
 * @author WuestMan
 *
 */
public class BasicStructureTagMessage extends TagMessage 
{
	/**
	 * This class is just here to distinguish the house tag message from other messages in the mod.
	 */
	public BasicStructureTagMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public BasicStructureTagMessage()
	{
		super();
	}
}
