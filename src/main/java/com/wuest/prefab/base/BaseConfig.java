package com.wuest.prefab.base;

import net.minecraft.nbt.NBTTagCompound;

/**
 * This abstract class is used as the base for NBTTagCompound configurations.
 * 
 * @author WuestMan
 */
public abstract class BaseConfig
{
	/**
	 * Writes to an NBTTagCombound given an existing NBTTagCompound.
	 * 
	 * @param compound The existing tag to add custom values too.
	 */
	public abstract void WriteToNBTCompound(NBTTagCompound compound);

	/**
	 * Reads data from an NBTTagCompound and creates an instance of the configuration class.
	 * 
	 * @param <T> The generic type which extends BaseConfig.
	 * @param compound The compound containing the data to create the configuration class.
	 * @return An instance containing the data held within the NBTTagCompound
	 */
	public abstract <T extends BaseConfig> T ReadFromNBTTagCompound(NBTTagCompound compound);

	/**
	 * Gets an NBTTagCompound which has this classes information in it.
	 * 
	 * @return A {@link NBTTagCompound} with this classes values.
	 */
	public NBTTagCompound GetNBTTagCompound()
	{
		NBTTagCompound compound = new NBTTagCompound();
		this.WriteToNBTCompound(compound);
		return compound;
	}
}