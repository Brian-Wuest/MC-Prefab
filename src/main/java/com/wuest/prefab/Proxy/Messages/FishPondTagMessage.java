package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * 
 * @author WuestMan
 *
 */
public class FishPondTagMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish this tag message from other messages in the mod.
	 * @param writeToNBTTagCompound The NBTTagCompound to write the data too.
	 */
	public FishPondTagMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public FishPondTagMessage()
	{
		super();
	}
}
