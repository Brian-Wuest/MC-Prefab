package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The ta message for the modular house.
 * @author WuestMan
 *
 */
public class ModularHouseTagMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish the house tag message from other messages in the mod.
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
