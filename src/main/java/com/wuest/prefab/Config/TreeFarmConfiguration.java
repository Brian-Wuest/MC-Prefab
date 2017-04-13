package com.wuest.prefab.Config;

import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author WuestMan
 *
 */
public class TreeFarmConfiguration extends StructureConfiguration 
{
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	public TreeFarmConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		TreeFarmConfiguration config = new TreeFarmConfiguration();
		
		return (TreeFarmConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}
