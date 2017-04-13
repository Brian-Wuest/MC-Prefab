package com.wuest.prefab.Config;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author WuestMan
 * This class is the configuration class for the nether gate.
 */
public class NetherGateConfiguration extends StructureConfiguration
{
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	public NetherGateConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		NetherGateConfiguration config = new NetherGateConfiguration();
		
		return (NetherGateConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}