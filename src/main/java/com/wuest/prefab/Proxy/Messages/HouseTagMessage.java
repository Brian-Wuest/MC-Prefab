package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author WuestMan
 *
 */
public class HouseTagMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish this tag message from other messages in the mod.
	 * @param writeToNBTTagCompound The NBTTagCompound to write the data too.
	 */
	public HouseTagMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public HouseTagMessage()
	{
		super();
	}
}
