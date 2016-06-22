package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

/**
 * This class is just here to distinguish the house tag message from other messages in the mod.
 * @author WuestMan
 *
 */
public class WareHouseTagMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish the house tag message from other messages in the mod.
	 */
	public WareHouseTagMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public WareHouseTagMessage()
	{
		super();
	}
}