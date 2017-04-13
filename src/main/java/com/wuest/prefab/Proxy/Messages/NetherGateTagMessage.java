package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author WuestMan
 */
public class NetherGateTagMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish this tag message from other messages in the mod.
	 * @param writeToNBTTagCompound The NBTTagCompound to write the data too.
	 */
	public NetherGateTagMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public NetherGateTagMessage()
	{
		super();
	}
}
