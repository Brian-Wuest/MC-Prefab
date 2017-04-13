package com.wuest.prefab.Config;

import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author WuestMan
 *
 */
public class FishPondConfiguration extends StructureConfiguration
{
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	public FishPondConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		FishPondConfiguration config = new FishPondConfiguration();
		
		return (FishPondConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}
