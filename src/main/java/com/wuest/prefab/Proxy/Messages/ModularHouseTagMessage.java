package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The tag message for the modular house.
 * @author WuestMan
 *
 */
public class ModularHouseTagMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish this tag message from other messages in the mod.
	 * @param writeToNBTTagCompound The NBTTagCompound to write the data too.
	 */
	public ModularHouseTagMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public ModularHouseTagMessage()
	{
		super();
	}
}
