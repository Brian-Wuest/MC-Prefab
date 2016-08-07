package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

public class MonsterMasherTagMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish the house tag message from other messages in the mod.
	 */
	public MonsterMasherTagMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public MonsterMasherTagMessage()
	{
		super();
	}
}
