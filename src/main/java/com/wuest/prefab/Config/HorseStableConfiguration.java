package com.wuest.prefab.Config;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The horse stable configuration.
 * @author WuestMan
 */
public class HorseStableConfiguration extends StructureConfiguration
{
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	public HorseStableConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		HorseStableConfiguration config = new HorseStableConfiguration();
		
		return (HorseStableConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}
