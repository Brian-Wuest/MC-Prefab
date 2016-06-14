package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

public class HouseTagMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish the house tag message from other messages in the mod.
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
