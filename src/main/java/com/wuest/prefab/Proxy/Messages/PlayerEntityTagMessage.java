package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

/**
 * This message is used to sync up server saved player information to the client.
 * @author WuestMan
 *
 */
public class PlayerEntityTagMessage extends TagMessage
{
	/**
	 * Initializes a new instance of the PlayerEntityTagMessage class.
	 */
	public PlayerEntityTagMessage()
	{
	}

	/**
	 * Initializes a new instance of the PlayerEntityTagMessage class.
	 * @param tagMessage The message to send.
	 */
	public PlayerEntityTagMessage(NBTTagCompound tagMessage)
	{
		super(tagMessage);
	}
}