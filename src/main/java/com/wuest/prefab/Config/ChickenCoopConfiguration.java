package com.wuest.prefab.Config;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * 
 * @author WuestMan
 *
 */
public class ChickenCoopConfiguration extends StructureConfiguration
{
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	public ChickenCoopConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		ChickenCoopConfiguration config = new ChickenCoopConfiguration();
		
		return (ChickenCoopConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}