package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author WuestMan
 *
 */
public class ConfigSyncMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish the configuration sync message from other messages in the mod.
	 * @param writeToNBTTagCompound The NBTTagCompound to write the data too.
	 */
	public ConfigSyncMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public ConfigSyncMessage()
	{
		super();
	}
}
