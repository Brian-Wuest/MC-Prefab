package com.wuest.prefab.Config;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The configuration class for the modular house.
 * @author WuestMan
 *
 */
public class ModularHouseConfiguration extends StructureConfiguration
{
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	public ModularHouseConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		ModularHouseConfiguration config = new ModularHouseConfiguration();
		
		return (ModularHouseConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}
