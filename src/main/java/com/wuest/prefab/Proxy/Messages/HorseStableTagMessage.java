package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

public class HorseStableTagMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish the house tag message from other messages in the mod.
	 */
	public HorseStableTagMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public HorseStableTagMessage()
	{
		super();
	}
}
